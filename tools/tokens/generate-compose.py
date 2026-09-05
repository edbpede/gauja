#!/usr/bin/env python3
# SPDX-FileCopyrightText: 2026 Gauja contributors
# SPDX-License-Identifier: AGPL-3.0-or-later
"""Generate Compose theme primitives from the shared DTCG document."""
import argparse
from pathlib import Path
from document import load

HEADER = "// SPDX-FileCopyrightText: 2026 Gauja contributors\n// SPDX-License-Identifier: AGPL-3.0-or-later\n// GENERATED — do not edit. Run tools/tokens/generate.sh.\npackage app.gauja.core.designsystem.generated\n\n"


def generate(tokens, output):
    values = load(tokens)
    output.mkdir(parents=True, exist_ok=True)
    for theme in ("dark", "light"):
        colors = {k.split(".")[-1]: v for k, v in values.items() if k.startswith(f"color.{theme}.")}
        literal = lambda v: "Color(0x" + "".join(f"{round(x * 255):02X}" for x in [v.get("alpha", 1), *v["components"]]) + ")"
        semantic = [f"    val {name} = {literal(value)}" for name, value in colors.items()]
        name = "Gauja" + theme.title() + "Colors"
        (output / f"{name}.kt").write_text(HEADER + "import androidx.compose.ui.graphics.Color\n\n" + f"object {name} {{\n" + "\n".join(semantic) + "\n}\n")
        native = [f"    {key} = {name}.{key}," for key in colors if (not key.endswith(("Foreground", "Background")) or key == "onBackground") and not key.startswith("hero")]
        (output / f"Gauja{theme.title()}ColorScheme.kt").write_text(HEADER + f"import androidx.compose.material3.{theme}ColorScheme\n\nval Gauja{theme.title()}ColorScheme = {theme}ColorScheme(\n" + "\n".join(native) + "\n)\n")
    for group in ("spacing", "radii", "elevation"):
        members = [f"    val {k.split('.')[-1]} = {v['value']:g}.dp" for k, v in values.items() if k.startswith(group + ".")]
        name = "Gauja" + group.title()
        (output / f"{name}.kt").write_text(HEADER + "import androidx.compose.ui.unit.dp\n\n" + f"object {name} {{\n" + "\n".join(members) + "\n}\n")
    (output / "GaujaShapes.kt").write_text(HEADER + "import androidx.compose.foundation.shape.RoundedCornerShape\nimport androidx.compose.material3.Shapes\n\nval GaujaShapes = Shapes(\n" + "\n".join(f"    {n} = RoundedCornerShape(GaujaRadii.{n})," for n in ["extraSmall", "small", "medium", "large", "extraLarge"]) + "\n)\n")
    styles = []
    for key, v in values.items():
        if key.startswith("typography."):
            size = v["fontSize"]["value"]
            styles.append(f"    {key.split('.')[-1]} = TextStyle(fontFamily = FontFamily.Default, fontSize = {size:g}.sp, fontWeight = FontWeight({v['fontWeight']}), lineHeight = {size * v['lineHeight']:g}.sp, letterSpacing = {v['letterSpacing']['value']:g}.sp),")
    (output / "GaujaTypography.kt").write_text(HEADER + "import androidx.compose.material3.Typography\nimport androidx.compose.ui.text.TextStyle\nimport androidx.compose.ui.text.font.FontFamily\nimport androidx.compose.ui.text.font.FontWeight\nimport androidx.compose.ui.unit.sp\n\nval GaujaTypography = Typography(\n" + "\n".join(styles) + "\n)\n")
    members = [f"    fun {k.split('.')[-1]}(reduceMotion: Boolean = false): Int = if (reduceMotion) 0 else {v['value']}" for k, v in values.items() if k.startswith("motion.")]
    (output / "GaujaMotion.kt").write_text(HEADER + "object GaujaMotion {\n" + "\n".join(members) + "\n}\n")


if __name__ == "__main__":
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--tokens", type=Path, required=True)
    parser.add_argument("--output", type=Path, required=True)
    args = parser.parse_args()
    generate(args.tokens, args.output)
