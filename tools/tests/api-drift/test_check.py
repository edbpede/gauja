# SPDX-FileCopyrightText: 2026 Gauja contributors
# SPDX-License-Identifier: AGPL-3.0-or-later
import json
from pathlib import Path
import shutil
import subprocess
import tempfile
import unittest

ROOT = Path(__file__).resolve().parents[3]
SCRIPT = ROOT / "tools/api-drift/check-local.sh"


class DriftTests(unittest.TestCase):
    def setUp(self):
        self.temporary = tempfile.TemporaryDirectory()
        self.addCleanup(self.temporary.cleanup)
        self.root = Path(self.temporary.name)
        shutil.copytree(ROOT / "api", self.root / "api")
        self.git("init", "-q")
        self.git("config", "user.name", "Contract Fixture")
        self.git("config", "user.email", "contract@example.invalid")
        self.git("config", "core.hooksPath", "/dev/null")
        self.git("add", "api")
        self.git("commit", "-qm", "test: initial contract", "-s")

    def git(self, *args):
        return subprocess.check_output(["git", "-C", str(self.root), *args], stderr=subprocess.DEVNULL).decode().strip()

    def check(self, expected, *args):
        result = subprocess.run([str(SCRIPT), *args], cwd=self.root, capture_output=True, text=True)
        self.assertEqual(result.returncode, expected, result.stdout + result.stderr)

    def change(self, name, content):
        path = self.root / "api" / name
        path.write_text(content)
        self.git("add", "api/" + name)

    def test_clean_index_validates(self):
        self.check(0)

    def test_custom_directory_in_index_worktree_and_range(self):
        self.git("mv", "api", "custom-api")
        self.git("commit", "-qm", "test: relocate contract", "-s")
        self.check(0, "custom-api")
        self.check(0, str(self.root / "custom-api"), "--working-tree")
        base = self.git("rev-parse", "HEAD")
        path = self.root / "custom-api/seerr-api.yml"
        path.write_text(path.read_text() + "\n")
        self.git("add", "custom-api/seerr-api.yml")
        self.check(1, "custom-api")
        self.git("commit", "-qm", "test: unpaired custom spec", "-s")
        self.check(1, "custom-api", "--range", base, "HEAD")

    def test_external_directory_supports_working_tree_validation(self):
        with tempfile.TemporaryDirectory() as temp:
            api = Path(temp) / "api"
            shutil.copytree(self.root / "api", api)
            self.check(0, str(api), "--working-tree")
            (api / "compat.json").write_text("{}")
            self.check(1, str(api), "--working-tree")

    def test_custom_directory_from_nested_working_directory(self):
        directory = self.root / "nested"
        directory.mkdir()
        result = subprocess.run([str(SCRIPT), "../api"], cwd=directory, capture_output=True, text=True)
        self.assertEqual(result.returncode, 0, result.stdout + result.stderr)
        pin = self.root / "api/UPSTREAM_COMMIT"
        self.change("UPSTREAM_COMMIT", pin.read_text() + "\n")
        result = subprocess.run([str(SCRIPT), "../api"], cwd=directory, capture_output=True, text=True)
        self.assertEqual(result.returncode, 1)
        self.assertIn("must change together", result.stderr)

    def test_help_without_contract_environment(self):
        wrapper = self.root / "tools/api-drift/check-local.sh"
        wrapper.parent.mkdir(parents=True)
        shutil.copy2(SCRIPT, wrapper)
        for option in ["--help", "-h"]:
            result = subprocess.run([str(wrapper), option], capture_output=True, text=True)
            self.assertEqual(result.returncode, 0, result.stderr)
            self.assertIn("Usage:", result.stdout)

    def test_schema_errors_have_controlled_diagnostics(self):
        for filename, invalid in [("compat.json", "{}"), ("compat.schema.json", '{"type": "invalid"}')]:
            path = self.root / "api" / filename
            original = path.read_text()
            path.write_text(invalid)
            result = subprocess.run([str(SCRIPT), "--working-tree"], cwd=self.root, capture_output=True, text=True)
            path.write_text(original)
            with self.subTest(filename=filename):
                self.assertEqual(result.returncode, 1)
                self.assertTrue(result.stderr.startswith("api-drift: "), result.stderr)
                self.assertNotIn("Traceback", result.stderr)

    def test_spec_or_pin_alone_rejected(self):
        for name in ["seerr-api.yml", "UPSTREAM_COMMIT"]:
            with self.subTest(name=name):
                self.git("reset", "--hard", "HEAD")
                path = self.root / "api" / name
                self.change(name, path.read_text() + "\n")
                self.check(1)

    def test_invalid_staged_content_is_not_masked_by_worktree(self):
        original = (self.root / "api/compat.json").read_text()
        self.change("compat.json", "{}")
        (self.root / "api/compat.json").write_text(original)
        self.check(1)
        self.check(0, "--working-tree")

    def test_bad_versions_and_endpoint_rejected(self):
        for field, value in [("min", "03.4.1"), ("max", "1.0.0"), ("endpoint", "/invented")]:
            content = json.loads((ROOT / "api/compat.json").read_text())
            content["blocklist"][field] = value
            self.change("compat.json", json.dumps(content))
            self.check(1)

    def test_bad_pin_and_missing_output_fail(self):
        self.change("UPSTREAM_COMMIT", "short\n")
        self.check(1, "--working-tree")
        self.git("reset", "--hard", "HEAD")
        (self.root / "api/ENDPOINTS.md").unlink()
        self.check(1, "--working-tree")

    def test_ci_checks_each_commit_not_only_final_diff(self):
        base = self.git("rev-parse", "HEAD")
        path = self.root / "api/seerr-api.yml"
        self.change("seerr-api.yml", path.read_text() + "\n")
        self.git("commit", "-qm", "test: unpaired spec", "-s")
        self.change("UPSTREAM_COMMIT", "a" * 40 + "\n# Fetched: 2026-09-05\n")
        self.git("commit", "-qm", "test: unpaired pin", "-s")
        self.check(1, "--range", base, "HEAD")

    def test_ci_accepts_a_paired_commit(self):
        base = self.git("rev-parse", "HEAD")
        path = self.root / "api/seerr-api.yml"
        self.change("seerr-api.yml", path.read_text() + "\n")
        self.change("UPSTREAM_COMMIT", "a" * 40 + "\n# Fetched: 2026-09-05\n")
        self.git("commit", "-qm", "test: paired update", "-s")
        self.check(0, "--range", base, "HEAD")


if __name__ == "__main__":
    unittest.main()
