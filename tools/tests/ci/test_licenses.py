# SPDX-FileCopyrightText: 2026 Gauja contributors
# SPDX-License-Identifier: AGPL-3.0-or-later
import importlib.util
from pathlib import Path
import unittest
import tempfile
import json

spec = importlib.util.spec_from_file_location("licenses", Path(__file__).resolve().parents[2] / "ci/check-licenses.py")
licenses = importlib.util.module_from_spec(spec)
spec.loader.exec_module(licenses)

class LicenseTests(unittest.TestCase):
    def setUp(self):
        self.policy = {"allow": ["MIT", "Apache-2.0"], "allow-build-only": ["EPL-1.0"]}

    def test_unknown_and_denied_fail_closed(self):
        for values in ([], ["UNKNOWN"], ["GPL-2.0-only"], ["MIT", "Proprietary"]):
            self.assertFalse(licenses.accepted(values, "runtime", self.policy))

    def test_build_permission_never_leaks_to_runtime(self):
        self.assertTrue(licenses.accepted(["EPL-1.0"], "build", self.policy))
        self.assertFalse(licenses.accepted(["EPL-1.0"], "runtime", self.policy))

    def test_exact_build_exception_cannot_expand_scope_or_version(self):
        policy = dict(self.policy, **{"build-exceptions": {"example:tool:1": "MPL-1.1"}})
        self.assertTrue(licenses.accepted(["MPL-1.1"], "build", policy, "example:tool:1"))
        self.assertFalse(licenses.accepted(["MPL-1.1"], "runtime", policy, "example:tool:1"))
        self.assertFalse(licenses.accepted(["MPL-1.1"], "build", policy, "example:tool:2"))
        self.assertFalse(licenses.accepted([], "build", policy, "example:tool:1"))

    def test_unrecognized_license_text_is_not_guessed(self):
        self.assertEqual(licenses.swift_license("Consult our commercial license"), [])

    def test_compound_exception_requires_exact_licenses_and_build_scope(self):
        values = ["MIT", "LicenseRef-Example-Public-Domain"]
        policy = dict(self.policy, **{"build-exceptions": {"example:tool:1": values}})
        self.assertTrue(licenses.accepted(list(reversed(values)), "build", policy, "example:tool:1"))
        self.assertFalse(licenses.accepted(values, "runtime", policy, "example:tool:1"))
        self.assertFalse(licenses.accepted(values, "build", policy, "example:tool:2"))
        self.assertFalse(licenses.accepted(values[1:], "build", policy, "example:tool:1"))
        self.assertFalse(licenses.accepted(values + ["UNKNOWN"], "build", policy, "example:tool:1"))
        policy["deny"] = [values[1]]
        self.assertFalse(licenses.accepted(values, "build", policy, "example:tool:1"))

    def test_empty_and_partial_resolved_graphs_fail(self):
        with tempfile.TemporaryDirectory() as temporary:
            root = Path(temporary)
            report = root / "apps/android/build/reports/module-graph.json"
            report.parent.mkdir(parents=True)
            for graph in ([], [{"name": ":app", "edges": []}]):
                report.write_text(json.dumps(graph))
                with self.assertRaises(ValueError):
                    list(licenses.android(root))
            report = root / "apps/ios/DerivedData/SourcePackages/workspace-state.json"
            report.parent.mkdir(parents=True)
            report.write_text(json.dumps({"object": {"dependencies": []}}))
            with self.assertRaises(ValueError):
                list(licenses.ios(root))

if __name__ == "__main__":
    unittest.main()
