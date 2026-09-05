# SPDX-FileCopyrightText: 2026 Gauja contributors
# SPDX-License-Identifier: AGPL-3.0-or-later
import hashlib
import importlib.util
from pathlib import Path
import sys
import tempfile
import unittest
from unittest.mock import patch

ROOT = Path(__file__).resolve().parents[3]
sys.path.insert(0, str(ROOT / "tools/codegen"))
spec = importlib.util.spec_from_file_location("generator", ROOT / "tools/codegen/generate.py")
module = importlib.util.module_from_spec(spec)
spec.loader.exec_module(module)


class DownloadTests(unittest.TestCase):
    def test_failed_downloads_never_poison_cache_and_retry_recovers(self):
        pins = {"OPENAPI_GENERATOR_VERSION": "test", "GENERATOR_ARCHIVE_SHA256": hashlib.sha256(b"valid").hexdigest()}
        with tempfile.TemporaryDirectory() as temp:
            root = Path(temp)
            jar = root / ".cache/openapi-generator-test.jar"
            def interrupted(url, destination):
                Path(destination).write_bytes(b"partial")
                raise OSError("interrupted")
            def invalid(url, destination):
                Path(destination).write_bytes(b"wrong checksum")
            def valid(url, destination):
                Path(destination).write_bytes(b"valid")
            with patch.object(module, "ROOT", root), patch.object(module, "run"), patch.object(module, "kotlin_sources"):
                for download, error in [(interrupted, OSError), (invalid, ValueError)]:
                    with patch.object(module.urllib.request, "urlretrieve", side_effect=download):
                        with self.assertRaises(error):
                            module.android(root / "spec.json", root, pins)
                    self.assertFalse(jar.exists())
                    self.assertEqual(list(jar.parent.iterdir()), [])
                jar.write_bytes(b"previous failed download")
                with patch.object(module.urllib.request, "urlretrieve", side_effect=valid):
                    module.android(root / "spec.json", root, pins)
                self.assertEqual(jar.read_bytes(), b"valid")
                with patch.object(module.urllib.request, "urlretrieve") as download:
                    module.android(root / "spec.json", root, pins)
                    download.assert_not_called()


if __name__ == "__main__":
    unittest.main()
