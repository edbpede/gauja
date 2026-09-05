# SPDX-FileCopyrightText: 2026 Gauja contributors
# SPDX-License-Identifier: AGPL-3.0-or-later
import json
from pathlib import Path
import subprocess
import sys
import tempfile
import unittest

ROOT = Path(__file__).resolve().parents[3]
sys.path.insert(0, str(ROOT / "tools/tokens"))
from document import load


class TokenTests(unittest.TestCase):
    def test_alias_cycles_missing_values_and_bad_colors_fail(self):
        for value in ["{missing}", "{palette.gray900}", {"colorSpace": "srgb", "components": [2, 0, 0]}]:
            data = json.loads((ROOT / "design/tokens.json").read_text())
            data["palette"]["gray900"]["$value"] = value
            with tempfile.TemporaryDirectory() as temp:
                path = Path(temp) / "tokens.json"
                path.write_text(json.dumps(data))
                with self.assertRaises(ValueError):
                    load(path)

    def test_color_theme_parity_and_contrast(self):
        values = load(ROOT / "design/tokens.json")
        dark = {k.removeprefix("color.dark.") for k in values if k.startswith("color.dark.")}
        light = {k.removeprefix("color.light.") for k in values if k.startswith("color.light.")}
        self.assertEqual(dark, light)
        def luminance(value):
            c = [v / 12.92 if v <= .04045 else ((v + .055) / 1.055) ** 2.4 for v in value["components"]]
            return sum(a * b for a, b in zip(c, [.2126, .7152, .0722]))
        for theme in ("dark", "light"):
            pairs = [("primary", "onPrimary"), ("surface", "onSurface"), ("background", "onBackground")]
            pairs += [(key, key.replace("Background", "Foreground")) for key in dark if key.endswith("Background") and key != "onBackground"]
            for bg, fg in pairs:
                l = sorted([luminance(values[f"color.{theme}.{name}"]) for name in (bg, fg)])
                self.assertGreaterEqual((l[1] + .05) / (l[0] + .05), 4.5, (theme, bg, fg))

    def test_both_generators_are_deterministic(self):
        with tempfile.TemporaryDirectory() as temp:
            for platform in ("compose", "swiftui"):
                snapshots = []
                for run in ("first", "second"):
                    out = Path(temp) / platform / run
                    subprocess.run([sys.executable, str(ROOT / f"tools/tokens/generate-{platform}.py"), "--tokens", str(ROOT / "design/tokens.json"), "--output", str(out)], check=True)
                    snapshots.append({p.name: p.read_bytes() for p in out.iterdir()})
                self.assertEqual(*snapshots)
                text = "\n".join(v.decode() for v in snapshots[0].values())
                self.assertIn("reduceMotion", text)
                self.assertIn("onBackground" if platform == "compose" else "gaujaOnBackground", text)


if __name__ == "__main__":
    unittest.main()
