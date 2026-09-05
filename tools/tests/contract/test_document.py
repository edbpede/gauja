# SPDX-FileCopyrightText: 2026 Gauja contributors
# SPDX-License-Identifier: AGPL-3.0-or-later
import json
from pathlib import Path
import sys
import shutil
import subprocess
import tempfile
import unittest

ROOT = Path(__file__).resolve().parents[3]
sys.path.insert(0, str(ROOT / "tools/contract"))
from document import read_document
from operations import assign_operation_ids
from overlays import apply_overlays
from validate import load_contract, validate_refs


class ContractTests(unittest.TestCase):
    def test_pinned_contract_and_overlays(self):
        spec = load_contract(ROOT / "api")
        self.assertEqual(len(spec["paths"]), 163)
        schema = spec["components"]["schemas"]
        self.assertEqual(schema["WatchlistRequest"]["required"], ["tmdbId", "mediaType"])
        self.assertEqual(schema["WatchProviders"]["type"], "object")
        self.assertEqual(schema["WatchProviders"]["properties"]["flatrate"]["type"], "array")
        self.assertEqual(schema["PersonDetails"]["properties"]["gender"]["type"], "integer")
        self.assertIn("mediaServerLogin", schema["PublicSettings"]["properties"])
        self.assertEqual(set(spec["paths"]["/watchlist"]["post"]["responses"]), {"201"})

    def test_duplicate_yaml_keys_fail(self):
        with tempfile.TemporaryDirectory() as temp:
            path = Path(temp) / "bad.yml"
            path.write_text("type: string\ntype: number\n")
            with self.assertRaises(ValueError):
                read_document(path)

    def test_yaml_on_and_dates_remain_strings(self):
        with tempfile.TemporaryDirectory() as temp:
            path = Path(temp) / "sample.yml"
            path.write_text("on: true\ndate: 2026-09-05\n")
            self.assertEqual(read_document(path), {"on": True, "date": "2026-09-05"})

    def test_yaml_boolean_spellings(self):
        with tempfile.TemporaryDirectory() as temp:
            path = Path(temp) / "sample.yml"
            path.write_text("values: [true, True, TRUE, false, False, FALSE, on, off, yes, no]\n")
            self.assertEqual(read_document(path)["values"], [True, True, True, False, False, False, "on", "off", "yes", "no"])

    def test_local_root_and_array_references(self):
        document = {"items": [{"type": "string"}], "a/b~c": {"type": "object"}}
        for ref in ["#", "#/items/0", "#/a~1b~0c"]:
            with self.subTest(ref=ref):
                validate_refs({"$ref": ref}, document)
        for ref in ["#/items/1", "#/items/-1", "#/items/00", "#/items/x", "#/missing", "https://example.invalid/spec"]:
            with self.subTest(ref=ref), self.assertRaises(ValueError):
                validate_refs({"$ref": ref}, document)

    def test_compat_endpoint_requires_an_operation(self):
        with tempfile.TemporaryDirectory() as temp:
            api = Path(temp) / "api"
            shutil.copytree(ROOT / "api", api)
            spec = read_document(api / "seerr-api.yml")
            spec["paths"]["/blocklist"] = {"summary": "No callable operation"}
            (api / "seerr-api.yml").write_text(json.dumps(spec))
            with self.assertRaisesRegex(ValueError, "blocklist: endpoint absent"):
                load_contract(api)

    def test_json_schema_errors_have_controlled_cli_diagnostics(self):
        with tempfile.TemporaryDirectory() as temp:
            api = Path(temp) / "api"
            shutil.copytree(ROOT / "api", api)
            for filename, invalid in [("compat.json", "{}"), ("compat.schema.json", '{"type": "invalid"}')]:
                path = api / filename
                original = path.read_text()
                path.write_text(invalid)
                result = subprocess.run([sys.executable, str(ROOT / "tools/contract/check.py"), "--api", str(api)], capture_output=True, text=True)
                path.write_text(original)
                with self.subTest(filename=filename):
                    self.assertEqual(result.returncode, 1)
                    self.assertTrue(result.stderr.startswith("contract: "), result.stderr)
                    self.assertNotIn("Traceback", result.stderr)

    def test_ids_include_method_and_parameter_marker(self):
        spec = {"paths": {"/user/{userId}": {"get": {}, "post": {}}}}
        assign_operation_ids(spec)
        self.assertEqual(spec["paths"]["/user/{userId}"]["get"]["operationId"], "getUserByUserId")
        self.assertNotEqual(spec["paths"]["/user/{userId}"]["get"], spec["paths"]["/user/{userId}"]["post"])

    def test_operation_id_collision_fails(self):
        spec = {"paths": {"/a-b": {"get": {}}, "/a/b": {"get": {}}}}
        with self.assertRaises(ValueError):
            assign_operation_ids(spec)

    def test_non_string_operation_ids_fail_validation(self):
        for name in [None, 42, [], {}]:
            with self.subTest(name=name), self.assertRaisesRegex(ValueError, "Invalid or duplicate operation ID"):
                assign_operation_ids({"paths": {"/a": {"get": {"operationId": name}}}})

    def overlay(self, action, citation=True):
        with tempfile.TemporaryDirectory() as temp:
            prefix = "# https://github.com/seerr-team/seerr/pull/3425\n" if citation else ""
            (Path(temp) / "change.yml").write_text(prefix + json.dumps({"overlay": "1.0.0", "actions": [action]}))
            return apply_overlays({"paths": {"/a": {"get": {}}}, "values": [1, 2, 3]}, Path(temp))

    def test_array_updates_append_and_removes_descend(self):
        self.assertEqual(self.overlay({"target": "$.values", "update": [4]})["values"], [1, 2, 3, 4])
        self.assertEqual(self.overlay({"target": "$.values[*]", "remove": True})["values"], [])

    def test_root_update_and_removal(self):
        self.assertEqual(self.overlay({"target": "$", "update": {"info": {"title": "Updated"}}})["info"], {"title": "Updated"})
        with self.assertRaisesRegex(ValueError, "cannot remove the document"):
            self.overlay({"target": "$", "remove": True})

    def test_unmatched_uncited_and_invented_routes_fail(self):
        for action, citation in [
            ({"target": "$.missing", "update": {}}, True),
            ({"target": "$.values", "update": []}, False),
            ({"target": "$.paths", "update": {"/new": {"get": {}}}}, True),
        ]:
            with self.subTest(action=action), self.assertRaises(ValueError):
                self.overlay(action, citation)


if __name__ == "__main__":
    unittest.main()
