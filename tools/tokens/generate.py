#!/usr/bin/env python3
# SPDX-FileCopyrightText: 2026 Gauja contributors
# SPDX-License-Identifier: AGPL-3.0-or-later
"""Generate or compare both native themes without either platform toolchain."""
import argparse
from pathlib import Path
import subprocess
import sys
import tempfile
sys.path.insert(0, str(Path(__file__).resolve().parents[1] / "codegen"))
from outputs import publish


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--check", action="store_true")
    parser.add_argument("tokens", nargs="?", type=Path)
    args = parser.parse_args()
    root = Path(__file__).resolve().parents[2]
    try:
        with tempfile.TemporaryDirectory(prefix="gauja-tokens-") as directory:
            staged = []
            for name, destination in [
                ("compose", "apps/android/core/designsystem/src/main/kotlin/app/gauja/core/designsystem/generated"),
                ("swiftui", "apps/ios/Packages/DesignSystem/Sources/DesignSystem/Generated"),
            ]:
                output = Path(directory) / name
                subprocess.run([sys.executable, str(root / f"tools/tokens/generate-{name}.py"), "--tokens", str(args.tokens or root / "design/tokens.json"), "--output", str(output)], check=True)
                staged.append((output, root / destination))
            for output, destination in staged:
                publish(output, destination, args.check)
        print("tokens: both themes " + ("match" if args.check else "generated"))
    except (ValueError, OSError, subprocess.CalledProcessError) as error:
        parser.exit(1, f"tokens: {error}\n")


if __name__ == "__main__":
    main()
