# SPDX-FileCopyrightText: 2026 Gauja contributors
# SPDX-License-Identifier: AGPL-3.0-or-later
from pathlib import Path
import sys
import tempfile
import unittest

ROOT = Path(__file__).resolve().parents[3]
sys.path.insert(0, str(ROOT / "tools/codegen"))
from outputs import publish
from redaction import kotlin_sources, swift_descriptions
from wire import wire_document


class GenerationTests(unittest.TestCase):
    def test_wire_lowering_retains_canonical_enum(self):
        source = {"type": "string", "enum": ["movie", "tv"]}
        wire = wire_document(source)
        self.assertEqual(source["enum"], ["movie", "tv"])
        self.assertNotIn("enum", wire)
        self.assertEqual(wire["x-gauja-known-values"], source["enum"])

    def test_unspecified_numbers_use_double(self):
        self.assertEqual(wire_document({"type": "number"})["format"], "double")
        self.assertNotIn("format", wire_document({"type": "integer"}))

    def test_missing_changed_extra_outputs_fail_without_mutation(self):
        with tempfile.TemporaryDirectory() as temp:
            source, dest = Path(temp) / "source", Path(temp) / "dest"
            source.mkdir(); dest.mkdir()
            (source / "Client.kt").write_text("expected")
            for actual in [None, "changed", "expected"]:
                if actual:
                    (dest / "Client.kt").write_text(actual)
                if actual == "expected":
                    (dest / "extra.kt").write_text("stale")
                with self.assertRaises(ValueError):
                    publish(source, dest, True)
                self.assertEqual((dest / "Client.kt").read_text() if actual else None, actual)
            publish(source, dest, False)
            self.assertFalse((dest / "extra.kt").exists())
            publish(source, dest, True)

    def test_redaction_is_generated_not_a_hand_edit(self):
        with tempfile.TemporaryDirectory() as temp:
            directory = Path(temp)
            path = directory / "Auth.kt"
            path.write_text('data class Auth (\n    val password: String\n)\n')
            kotlin_sources(directory)
            self.assertIn('override fun toString(): String = "[REDACTED]"', path.read_text())
            self.assertIn("GENERATED", path.read_text())

    def test_swift_admin_credentials_are_redacted(self):
        with tempfile.TemporaryDirectory() as temp:
            directory = Path(temp)
            for field in ["adminPass", "authPass", "authHeader", "pushoverUserKey"]:
                (directory / "Types.swift").write_text(f"public struct Settings {{\n    public var {field}: String\n}}\n")
                swift_descriptions(directory)
                self.assertIn("extension Settings:", (directory / "RedactedDescriptions.swift").read_text())

    def test_swift_nested_namespaces_and_frozen_enums(self):
        with tempfile.TemporaryDirectory() as temp:
            directory = Path(temp)
            (directory / "Types.swift").write_text('extension Components {\n    public enum Schemas {\n        public struct User {\n            public var plexToken: String\n        }\n    }\n}\npublic enum Operations {\n    public enum auth {\n        @frozen public enum Body {\n            public struct Payload {\n                public var password: String\n            }\n        }\n    }\n}\n')
            swift_descriptions(directory)
            text = (directory / "RedactedDescriptions.swift").read_text()
            self.assertIn("extension Components.Schemas.User:", text)
            self.assertIn("extension Operations.auth.Body.Payload:", text)


if __name__ == "__main__":
    unittest.main()
