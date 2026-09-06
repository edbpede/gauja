#!/usr/bin/env python3
# SPDX-FileCopyrightText: 2026 Gauja contributors
# SPDX-License-Identifier: AGPL-3.0-or-later
"""Run a real generated-client consumer against the recorded public server responses."""
import argparse
from http.server import BaseHTTPRequestHandler, ThreadingHTTPServer
import os
from pathlib import Path
import subprocess
import threading

ROOT = Path(__file__).resolve().parents[2]
RESPONSES = {
    "/api/v1/status?checkUpdateAvailable=false": "public/getStatus.json",
    "/api/v1/settings/public": "settings/getSettingsPublic.json",
}


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("platform", choices=["android", "ios"])
    args = parser.parse_args()
    requests = []
    class Handler(BaseHTTPRequestHandler):
        def log_message(self, *args):
            pass

        def do_GET(self):
            requests.append(self.path)
            if self.path not in RESPONSES or self.headers.get("Cookie") or self.headers.get("Authorization"):
                self.send_error(400)
                return
            self.send_response(200)
            self.send_header("Content-Type", "application/json")
            self.end_headers()
            self.wfile.write((ROOT / "api/fixtures/3.4.1" / RESPONSES[self.path]).read_bytes())

    with ThreadingHTTPServer(("127.0.0.1", 0), Handler) as server:
        worker = threading.Thread(target=server.serve_forever, daemon=True)
        worker.start()
        try:
            env = dict(os.environ, GAUJA_CONTRACT_SERVER=f"http://127.0.0.1:{server.server_port}")
            if args.platform == "android":
                command = [str(ROOT / "apps/android/gradlew"), "--project-dir", str(ROOT / "apps/android"),
                           ":core:data:testDebugUnitTest", "--tests", "*pinnedContainerContract", "--rerun", "--quiet"]
            else:
                command = ["swift", "test", "--package-path", str(ROOT / "apps/ios/Packages/Data"), "--filter", "pinnedContainerContract"]
            subprocess.run(command, env=env, check=True)
            if requests != list(RESPONSES):
                raise ValueError("Expected both public operations, in order, exactly once")
        finally:
            server.shutdown()
            worker.join()
    print(f"recorded-contract: {args.platform} passed")


if __name__ == "__main__":
    main()
