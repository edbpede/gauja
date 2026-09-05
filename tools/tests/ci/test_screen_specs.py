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
            for change in [original.replace("**Offline:**", "**Unavailable:**"), original.replace("A04", "A99")]:
                path.write_text(change)
                with self.assertRaises(ValueError):
                    module.check(root)


if __name__ == "__main__":
    unittest.main()
