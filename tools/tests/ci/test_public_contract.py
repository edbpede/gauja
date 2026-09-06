# SPDX-FileCopyrightText: 2026 Gauja contributors
# SPDX-License-Identifier: AGPL-3.0-or-later
from datetime import datetime, timezone
import importlib.util
from pathlib import Path
import unittest

spec = importlib.util.spec_from_file_location("public_contract", Path(__file__).resolve().parents[2] / "ci/public-contract.py")
contract = importlib.util.module_from_spec(spec)
spec.loader.exec_module(contract)

class PublicContractTests(unittest.TestCase):
    def test_imminent_or_past_sunset_fails(self):
        now = datetime(2026, 9, 5, tzinfo=timezone.utc)
        for value in ["Mon, 01 Jun 2026 00:00:00 GMT", "Thu, 01 Oct 2026 00:00:00 GMT"]:
            with self.assertRaises(ValueError):
                contract.check_sunset(value, now)
        contract.check_sunset("Mon, 01 Mar 2027 00:00:00 GMT", now)

    def test_public_identifiers_are_scrubbed_without_mutating_input(self):
        original = {"plexClientIdentifier": "instance", "vapidPublic": "key", "initialized": False}
        result = contract.scrub(original)
        self.assertNotEqual(result["plexClientIdentifier"], "instance")
        self.assertEqual(result["vapidPublic"], "REDACTED")
        self.assertFalse(result["initialized"])
        self.assertEqual(original["vapidPublic"], "key")

if __name__ == "__main__":
    unittest.main()
