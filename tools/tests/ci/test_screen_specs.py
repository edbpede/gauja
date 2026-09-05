# SPDX-FileCopyrightText: 2026 Gauja contributors
# SPDX-License-Identifier: AGPL-3.0-or-later
import importlib.util
from pathlib import Path
import shutil
import re
import tempfile
import unittest

ROOT = Path(__file__).resolve().parents[3]
spec = importlib.util.spec_from_file_location("screens", ROOT / "tools/ci/check-screen-specs.py")
module = importlib.util.module_from_spec(spec)
spec.loader.exec_module(module)


class ScreenTests(unittest.TestCase):
    def test_inventory_matches_detailed_contracts(self):
        rows, specs, cases = module.check(ROOT)
        self.assertGreater(rows, 0)
        self.assertGreater(specs, 0)
        self.assertGreater(cases, 0)

    def test_bad_matrix_reference_and_missing_acceptance_fail(self):
        with tempfile.TemporaryDirectory() as temp:
            root = Path(temp)
            shutil.copytree(ROOT / "design/screens", root / "design/screens")
            (root / "api").mkdir()
            shutil.copy2(ROOT / "api/coverage.json", root / "api/coverage.json")
            path = root / "design/screens/auth/local.md"
            original = path.read_text()
            for change, message in [
                (original.replace("A04", "A99"), "unknown matrix row"),
                (original.replace("## Acceptance criteria", "## Examples"), "missing Acceptance criteria"),
            ]:
                path.write_text(change)
                with self.assertRaisesRegex(ValueError, message):
                    module.check(root)

    def test_stale_totals_and_duplicate_matrix_ids_fail(self):
        with tempfile.TemporaryDirectory() as temp:
            root = Path(temp)
            shutil.copytree(ROOT / "design/screens", root / "design/screens")
            (root / "api").mkdir()
            shutil.copy2(ROOT / "api/coverage.json", root / "api/coverage.json")
            inventory = root / "design/screens/INVENTORY.md"
            original = inventory.read_text()
            row = next(line for line in original.splitlines(keepends=True) if line.startswith("| `auth/local.md`"))
            inventory.write_text(original.replace(row, ""))
            with self.assertRaisesRegex(ValueError, "Inventory sizing totals differ from rows"):
                module.check(root)
            inventory.write_text(original)
            matrix = root / "design/screens/auth/MATRIX.md"
            matrix.write_text(matrix.read_text() + "\n| A01 | conflicting outcome |\n")
            with self.assertRaisesRegex(ValueError, "Duplicate auth matrix IDs"):
                module.check(root)

    def test_inventory_can_grow_without_a_new_spec_yet(self):
        with tempfile.TemporaryDirectory() as temp:
            root = Path(temp)
            shutil.copytree(ROOT / "design/screens", root / "design/screens")
            (root / "api").mkdir()
            shutil.copy2(ROOT / "api/coverage.json", root / "api/coverage.json")
            inventory = root / "design/screens/INVENTORY.md"
            text = inventory.read_text()
            row = next(line for line in text.splitlines() if line.startswith("| `auth/local.md`"))
            total = next(line for line in text.splitlines() if line.startswith("| **Total**"))
            counts = [int(n) for n in re.findall(r"\*\*(\d+)\*\*", total)]
            counts["SML".index(row.split("|")[3].strip())] += 1
            counts[-1] += 1
            updated = "| **Total** | " + " | ".join(f"**{n}**" for n in counts) + " |"
            inventory.write_text(text.replace(row, row + "\n" + row.replace("auth/local.md", "settings/new.md"))
                                 .replace(total, updated))
            self.assertEqual(module.check(root)[0], module.check(ROOT)[0] + 1)

    def test_component_needs_only_applicable_states(self):
        with tempfile.TemporaryDirectory() as temp:
            root = Path(temp)
            shutil.copytree(ROOT / "design/screens", root / "design/screens")
            (root / "api").mkdir()
            shutil.copy2(ROOT / "api/coverage.json", root / "api/coverage.json")
            path = root / "design/screens/components/INVENTORY.md"
            path.write_text(path.read_text() + "\n## States\n\nSelected or unselected; no network state.\n")
            module.check(root)

    def test_section_contract_requires_its_own_acceptance(self):
        with tempfile.TemporaryDirectory() as temp:
            root = Path(temp)
            shutil.copytree(ROOT / "design/screens", root / "design/screens")
            (root / "api").mkdir()
            shutil.copy2(ROOT / "api/coverage.json", root / "api/coverage.json")
            path = root / "design/screens/components/INVENTORY.md"
            original = path.read_text()
            start = original.index("## GenreTag\n")
            end = original.index("## KeywordTag\n", start)
            section = original[start:end].replace("**Acceptance criteria:**", "**Examples:**")
            path.write_text(original[:start] + section + original[end:])
            with self.assertRaisesRegex(ValueError, "missing Acceptance criteria"):
                module.check(root)

    def test_screen_contract_can_be_a_section(self):
        with tempfile.TemporaryDirectory() as temp:
            root = Path(temp)
            shutil.copytree(ROOT / "design/screens", root / "design/screens")
            (root / "api").mkdir()
            shutil.copy2(ROOT / "api/coverage.json", root / "api/coverage.json")
            inventory = root / "design/screens/INVENTORY.md"
            inventory.write_text(inventory.read_text().replace("`auth/local.md`", "`auth/local.md#local-sign-in`"))
            self.assertEqual(module.check(root), module.check(ROOT))
            inventory.write_text(inventory.read_text().replace("#local-sign-in", "#missing"))
            with self.assertRaisesRegex(ValueError, "missing contract anchor"):
                module.check(root)

    def test_duplicate_component_identity_fails(self):
        with tempfile.TemporaryDirectory() as temp:
            root = Path(temp)
            shutil.copytree(ROOT / "design/screens", root / "design/screens")
            (root / "api").mkdir()
            shutil.copy2(ROOT / "api/coverage.json", root / "api/coverage.json")
            path = root / "design/screens/components/INVENTORY.md"
            row = next(line for line in path.read_text().splitlines() if line.startswith("| [GenreTag]"))
            path.write_text(path.read_text() + "\n" + row + "\n")
            with self.assertRaisesRegex(ValueError, "duplicate component identities"):
                module.check(root)

    def test_broken_links_and_unowned_auth_cases_fail(self):
        with tempfile.TemporaryDirectory() as temp:
            root = Path(temp)
            shutil.copytree(ROOT / "design/screens", root / "design/screens")
            (root / "api").mkdir()
            shutil.copy2(ROOT / "api/coverage.json", root / "api/coverage.json")
            path = root / "design/screens/components/INVENTORY.md"
            original = path.read_text()
            for link in ["missing.md", "INVENTORY.md#missing-anchor"]:
                path.write_text(original + f"\n[Invalid]({link})\n")
                with self.assertRaisesRegex(ValueError, "broken"):
                    module.check(root)
            path.write_text(original)
            matrix = root / "design/screens/auth/MATRIX.md"
            matrix.write_text(matrix.read_text() + "\n| A99 | unowned case |\n")
            with self.assertRaisesRegex(ValueError, "not owned"):
                module.check(root)


if __name__ == "__main__":
    unittest.main()
