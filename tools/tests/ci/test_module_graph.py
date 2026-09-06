# SPDX-FileCopyrightText: 2026 Gauja contributors
# SPDX-License-Identifier: AGPL-3.0-or-later
import importlib.util
from pathlib import Path
import unittest
import tempfile

spec = importlib.util.spec_from_file_location("graph", Path(__file__).resolve().parents[2] / "ci/module_graph.py")
graph = importlib.util.module_from_spec(spec)
spec.loader.exec_module(graph)

class GraphTests(unittest.TestCase):
    def test_forbidden_edges(self):
        for source, target in [("feature/servers", "feature/auth"), ("core/model", "core/network"),
                               ("core/api", "core/common"), ("core/data", "app"),
                               ("feature/servers", "core/api"), ("core/network", "core/data")]:
            self.assertFalse(graph.allowed(source, target), (source, target))

    def test_data_boundary_and_test_only_utilities(self):
        self.assertTrue(graph.allowed("core/data", "core/api"))
        self.assertTrue(graph.allowed("feature/servers", "core/testing", test=True))
        self.assertFalse(graph.allowed("feature/servers", "core/testing"))

    def test_real_source_imports_reject_cross_feature_and_platform_dependencies(self):
        fixtures = {
            "android/feature/servers/src/main/kotlin/Bad.kt": "import app.gauja.feature.auth.AuthScreen",
            "android/core/model/src/main/kotlin/Bad.kt": "import android.content.Context",
            "ios/Packages/Features/Servers/Sources/Bad.swift": "internal import Auth",
            "ios/Packages/Features/Auth/Sources/Good.swift": "import Foundation",
            "ios/Packages/Model/Sources/Bad.swift": "import SwiftData",
        }
        with tempfile.TemporaryDirectory() as temporary:
            root = Path(temporary)
            for name, content in fixtures.items():
                path = root / "apps" / name
                path.parent.mkdir(parents=True, exist_ok=True)
                path.write_text(content)
            for platform in ("android", "ios"):
                failures = list(graph.imports(root, platform))
                self.assertEqual(len(failures), 2, failures)
                self.assertTrue(any("cross-feature" in failure for failure in failures))
                self.assertTrue(any("platform import" in failure for failure in failures))

if __name__ == "__main__":
    unittest.main()
