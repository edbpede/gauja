# SPDX-FileCopyrightText: 2026 Gauja contributors
# SPDX-License-Identifier: AGPL-3.0-or-later
import argparse
import importlib.util
from pathlib import Path
import subprocess
import sys
import tempfile
import unittest
from unittest.mock import patch
from unittest.mock import Mock

spec = importlib.util.spec_from_file_location("android_smoke", Path(__file__).resolve().parents[2] / "ci/android-smoke.py")
smoke = importlib.util.module_from_spec(spec)
spec.loader.exec_module(smoke)

APP = '<manifest xmlns:android="http://schemas.android.com/apk/res/android" package="app.gauja"><application><activity android:name="app.gauja.MainActivity" android:exported="true"/></application></manifest>'
TEST = '<manifest xmlns:android="http://schemas.android.com/apk/res/android" package="app.gauja.test"><instrumentation android:name="app.gauja.HiltTestRunner" android:targetPackage="app.gauja"/></manifest>'
CASE = '<testcase classname="app.gauja.ServerCheckTest" name="injectedProbeRendersDomainResult">{}</testcase>'


class AndroidSmokeTests(unittest.TestCase):
    def setUp(self):
        self.temporary = tempfile.TemporaryDirectory()
        self.addCleanup(self.temporary.cleanup)
        self.directory = Path(self.temporary.name)
        args = argparse.Namespace(evidence_dir=self.directory, avd="gauja-fixture", serial="emulator-5554",
                                  api="37.0", memory_mib=4096)
        self.runner = smoke.Smoke(args)

    def test_requires_exact_successful_hilt_body(self):
        path = self.directory / "TEST-result.xml"
        path.write_text('<testsuite tests="1">' + CASE.format("") + '</testsuite>')
        smoke.validate_results(self.directory)
        for body in ("", CASE.format('<skipped/>'), CASE.format('<failure/>'), CASE.format('<error/>'),
                     CASE.format("").replace("injectedProbeRendersDomainResult", "anotherTest"), CASE.format("") * 2):
            with self.subTest(body=body):
                path.write_text('<testsuite>' + body + '</testsuite>')
                with self.assertRaises(ValueError):
                    smoke.validate_results(self.directory)
        path.write_text('<testsuite failures="1">' + CASE.format("") + '</testsuite>')
        with self.assertRaises(ValueError):
            smoke.validate_results(self.directory)

    def test_missing_and_malformed_results_fail(self):
        with self.assertRaises(ValueError):
            smoke.validate_results(self.directory)
        (self.directory / "TEST-broken.xml").write_text("not XML")
        with self.assertRaises(smoke.ET.ParseError):
            smoke.validate_results(self.directory)

    def test_manifest_requires_real_target_and_launcher(self):
        smoke.validate_manifests(APP, TEST)
        for test in (TEST.replace('targetPackage="app.gauja"', 'targetPackage="app.gauja.test"'),
                     TEST.replace('android:targetPackage="app.gauja"', ''),
                     TEST.replace("HiltTestRunner", "OtherRunner")):
            with self.assertRaises(ValueError):
                smoke.validate_manifests(APP, test)
        with self.assertRaises(ValueError):
            smoke.validate_manifests(APP.replace('exported="true"', 'exported="false"'), TEST)

    def healthy_adb(self, *command, **kwargs):
        if command[:2] == ("shell", "pidof"):
            return "491" if command[-1] == "surfaceflinger" else "700"
        if command[:3] == ("shell", "service", "check"):
            return f"Service {command[-1]}: found"
        if command[-1] == "get-current-user":
            return "0"
        if command[0] == "shell" and command[1].startswith("printf gauja"):
            return "gauja"
        return "ok"

    def test_health_requires_stable_services_and_storage(self):
        with patch.object(self.runner, "adb", side_effect=self.healthy_adb):
            self.runner.health()
            self.runner.health()
            self.runner.identities = ("490", "700")
            with self.assertRaisesRegex(RuntimeError, "restarted"):
                self.runner.health()

    def test_missing_package_or_activity_service_fails(self):
        for service in ("package", "activity"):
            def adb(*command, **kwargs):
                if command == ("shell", "service", "check", service):
                    return f"Service {service}: not found"
                return self.healthy_adb(*command, **kwargs)
            with patch.object(self.runner, "adb", side_effect=adb):
                with self.assertRaisesRegex(RuntimeError, "Missing service"):
                    self.runner.health()

    def test_early_crash_and_disconnected_storage_are_latched(self):
        for message in ("Assertion failed: !rcEnc->featureInfo()->hasReadColorBufferDma",
                        "Transport endpoint is not connected"):
            (self.directory / "logcat.log").write_text(message)
            with self.assertRaisesRegex(RuntimeError, "Critical"):
                self.runner.crashes()

    def test_hanging_adb_command_is_bounded(self):
        with self.assertRaises(TimeoutError):
            self.runner.command([sys.executable, "-c", "import time; time.sleep(60)"], timeout=0.1)

    def test_emulator_exit_interrupts_device_wait(self):
        self.runner.emulator = Mock()
        self.runner.emulator.poll.return_value = 1
        with self.assertRaisesRegex(RuntimeError, "Emulator exited"):
            self.runner.command([sys.executable, "-c", "import time; time.sleep(60)"], watch_boot=True)

    def test_fresh_guest_need_not_have_a_download_directory(self):
        def adb(*command, **kwargs):
            if any("/sdcard/Download" in part for part in command):
                raise subprocess.CalledProcessError(1, command, "No such file or directory")
            return self.healthy_adb(*command, **kwargs)
        with patch.object(self.runner, "adb", side_effect=adb):
            self.runner.health()

    def test_boot_failure_and_cleanup_failure_preserve_primary_error(self):
        self.runner.evidence = self.directory / "result"
        with patch.object(self.runner, "boot", side_effect=TimeoutError("boot timeout")), \
                patch.object(self.runner, "tests") as tests, \
                patch.object(self.runner, "collect") as collect, \
                patch.object(self.runner, "cleanup", side_effect=RuntimeError("cleanup failed")):
            self.assertEqual(self.runner.run(), 1)
            tests.assert_not_called()
            collect.assert_called_once()
            self.assertEqual(self.runner.failure, "TimeoutError: boot timeout")
        self.assertTrue((self.runner.evidence / "status.json").is_file())

    def test_success_requires_tests_and_collection(self):
        self.runner.evidence = self.directory / "result"
        with patch.object(self.runner, "boot"), patch.object(self.runner, "tests") as tests, \
                patch.object(self.runner, "collect") as collect, patch.object(self.runner, "cleanup"):
            self.assertEqual(self.runner.run(), 0)
            tests.assert_called_once()
            collect.assert_called_once()


if __name__ == "__main__":
    unittest.main()
