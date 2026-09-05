# SPDX-FileCopyrightText: 2026 Gauja contributors
# SPDX-License-Identifier: AGPL-3.0-or-later
import importlib.util
from pathlib import Path
import shutil
import tempfile
import unittest

ROOT = Path(__file__).resolve().parents[3]
spec = importlib.util.spec_from_file_location("screens", ROOT / "tools/ci/check-screen-specs.py")
module = importlib.util.module_from_spec(spec)
spec.loader.exec_module(module)


class ScreenTests(unittest.TestCase):
    def test_inventory_matches_detailed_contracts(self):
        self.assertEqual(module.check(ROOT), (95, 36, 41))

    def test_missing_state_and_bad_matrix_reference_fail(self):
        with tempfile.TemporaryDirectory() as temp:
            root = Path(temp)
            shutil.copytree(ROOT / "design/screens", root / "design/screens")
            path = root / "design/screens/auth/local.md"
            original = path.read_text()
            for change, message in [
                (original.replace("**Offline:**", "**Unavailable:**"), "missing state Offline"),
                (original.replace("A04", "A99"), "unknown matrix row"),
                (original.replace("## Content components", "## Components"), "missing Content components"),
            ]:
                path.write_text(change)
                with self.assertRaisesRegex(ValueError, message):
                    module.check(root)

    def test_inventory_deletion_and_duplicate_matrix_ids_fail(self):
        with tempfile.TemporaryDirectory() as temp:
            root = Path(temp)
            shutil.copytree(ROOT / "design/screens", root / "design/screens")
            inventory = root / "design/screens/INVENTORY.md"
            original = inventory.read_text()
            row = next(line for line in original.splitlines(keepends=True) if line.startswith("| `auth/local.md`"))
            inventory.write_text(original.replace(row, "").replace("**50** | **31** | **95**", "**49** | **31** | **94**"))
            with self.assertRaisesRegex(ValueError, "Inventory sizing differs from Phase 2 contract"):
                module.check(root)
            inventory.write_text(original)
            matrix = root / "design/screens/auth/MATRIX.md"
            matrix.write_text(matrix.read_text() + "\n| A01 | conflicting outcome |\n")
            with self.assertRaisesRegex(ValueError, "Duplicate auth matrix IDs"):
                module.check(root)


if __name__ == "__main__":
    unittest.main()
