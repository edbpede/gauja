# SPDX-FileCopyrightText: 2026 Gauja contributors
# SPDX-License-Identifier: AGPL-3.0-or-later
import importlib.util
import json
from pathlib import Path
import unittest
from unittest.mock import patch

spec = importlib.util.spec_from_file_location("smoke", Path(__file__).resolve().parents[2] / "ci/ios-smoke.py")
smoke = importlib.util.module_from_spec(spec)
spec.loader.exec_module(smoke)

class SimulatorTests(unittest.TestCase):
    def test_missing_floor_is_a_failure(self):
        with self.assertRaises(ValueError):
            smoke.select_runtimes([{"name": "iOS 26.5", "version": "26.5", "isAvailable": True}])

    def test_available_floor_and_latest_sdk_family(self):
        data = [{"name": "iOS " + version, "version": version, "isAvailable": available}
                for version, available in [("18.0", True), ("18.6", True), ("26.5", True), ("26.6", False)]]
        self.assertEqual([v["version"] for v in smoke.select_runtimes(data)], ["18.6", "26.5"])

    def test_boot_failure_stops_testing_and_removes_simulator(self):
        runtimes = {"runtimes": [{"name": "iOS " + version, "version": version, "isAvailable": True,
                                  "identifier": "runtime-" + version}
                                 for version in ("18.6", "26.5")]}

        def run(command, **kwargs):
            if "bootstatus" in command:
                raise smoke.subprocess.CalledProcessError(1, command)

        with patch.object(smoke.subprocess, "check_output", side_effect=[json.dumps(runtimes), "simulator-id"]), \
                patch.object(smoke.subprocess, "run", side_effect=run) as commands:
            with self.assertRaises(smoke.subprocess.CalledProcessError):
                smoke.main()
        invocations = [call.args[0] for call in commands.call_args_list]
        self.assertFalse(any(command[0] == "xcodebuild" for command in invocations))
        self.assertEqual(invocations[-1], ["xcrun", "simctl", "delete", "simulator-id"])

if __name__ == "__main__":
    unittest.main()
