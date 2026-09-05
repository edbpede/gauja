# SPDX-FileCopyrightText: 2026 Gauja contributors
# SPDX-License-Identifier: AGPL-3.0-or-later
from contextlib import redirect_stderr
import importlib.util
import urllib.error
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
    def test_empty_pin_has_controlled_diagnostic_without_network(self):
        with tempfile.TemporaryDirectory() as temp:
            (Path(temp) / "UPSTREAM_COMMIT").write_text("")
            with patch("sys.argv", ["check-upstream", "--api", temp]), patch.object(module.urllib.request, "urlopen") as request:
                stderr = io.StringIO()
                with redirect_stderr(stderr), self.assertRaises(SystemExit) as failure:
                    module.main()
                self.assertEqual(failure.exception.code, 1)
                self.assertEqual(stderr.getvalue(), "upstream: invalid SHA\n")
                request.assert_not_called()

    def test_network_errors_have_controlled_diagnostics(self):
        with tempfile.TemporaryDirectory() as temp:
            (Path(temp) / "UPSTREAM_COMMIT").write_text("a" * 40 + "\n")
            errors = [urllib.error.URLError("offline"), urllib.error.HTTPError("url", 404, "Not Found", {}, None), TimeoutError("timed out")]
            for error in errors:
                with self.subTest(error=error), patch("sys.argv", ["check-upstream", "--api", temp]), patch.object(module.urllib.request, "urlopen", side_effect=error):
                    stderr = io.StringIO()
                    with redirect_stderr(stderr), self.assertRaises(SystemExit) as failure:
                        module.main()
                    self.assertEqual(failure.exception.code, 1)
                    self.assertTrue(stderr.getvalue().startswith("upstream: "), stderr.getvalue())
                    self.assertNotIn("Traceback", stderr.getvalue())

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
