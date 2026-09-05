# SPDX-FileCopyrightText: 2026 Gauja contributors
# SPDX-License-Identifier: AGPL-3.0-or-later
import importlib.util
from pathlib import Path
import tempfile
import unittest

ROOT = Path(__file__).resolve().parents[3]
spec = importlib.util.spec_from_file_location("boundary", ROOT / "tools/ci/check-api-boundary.py")
module = importlib.util.module_from_spec(spec)
spec.loader.exec_module(module)


class BoundaryTests(unittest.TestCase):
    def test_only_data_api_and_test_support_may_import(self):
        with tempfile.TemporaryDirectory() as temp:
            root = Path(temp)
            for name, source in [
                ("apps/android/core/data/Mapper.kt", "import app.gauja.core.api.models.User"),
                ("apps/android/feature/discover/Screen.kt", "val user: app.gauja.core.api.models.User"),
                ("apps/ios/Packages/Data/Mapper.swift", "import SeerrAPI"),
                ("apps/ios/Packages/Features/Discover/View.swift", "import SeerrAPI"),
                ("apps/ios/Packages/Data/Export.swift", "@_exported import SeerrAPI"),
            ]:
                path = root / name; path.parent.mkdir(parents=True, exist_ok=True); path.write_text(source)
            self.assertEqual(len(list(module.violations(root))), 3)


if __name__ == "__main__":
    unittest.main()
