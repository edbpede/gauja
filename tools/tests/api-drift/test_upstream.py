# SPDX-FileCopyrightText: 2026 Gauja contributors
# SPDX-License-Identifier: AGPL-3.0-or-later
import importlib.util
import io
from pathlib import Path
import tempfile
import unittest
from unittest.mock import patch

ROOT = Path(__file__).resolve().parents[3]
spec = importlib.util.spec_from_file_location("upstream", ROOT / "tools/api-drift/check-upstream.py")
module = importlib.util.module_from_spec(spec)
spec.loader.exec_module(module)


class UpstreamTests(unittest.TestCase):
    def test_exact_bytes_required_and_invalid_pin_never_requested(self):
        with tempfile.TemporaryDirectory() as temp:
            api = Path(temp)
            (api / "UPSTREAM_COMMIT").write_text("a" * 40 + "\n# Fetched: 2026-09-05\n")
            (api / "seerr-api.yml").write_bytes(b"spec\n")
            (api / "LICENSE.upstream").write_bytes(b"license\n")
            with patch("sys.argv", ["check-upstream", "--api", temp]):
                with patch.object(module.urllib.request, "urlopen", side_effect=[io.BytesIO(b"spec\n"), io.BytesIO(b"license\n")]):
                    module.main()
                with patch.object(module.urllib.request, "urlopen", return_value=io.BytesIO(b"changed")):
                    with self.assertRaises(SystemExit) as failure:
                        module.main()
                    self.assertEqual(failure.exception.code, 1)
                (api / "UPSTREAM_COMMIT").write_text("invalid\n")
                with patch.object(module.urllib.request, "urlopen") as request:
                    with self.assertRaises(SystemExit):
                        module.main()
                    request.assert_not_called()


if __name__ == "__main__":
    unittest.main()
