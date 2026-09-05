# SPDX-FileCopyrightText: 2026 Gauja contributors
# SPDX-License-Identifier: AGPL-3.0-or-later
"""Generate safe descriptions; wire encoding remains available for authentication."""
import re

BANNER = "// SPDX-FileCopyrightText: 2026 Gauja contributors\n// SPDX-License-Identifier: AGPL-3.0-or-later\n// GENERATED — do not edit. Run tools/codegen/generate.sh.\n"


def kotlin_sources(directory):
    for path in directory.rglob("*.kt"):
        source = path.read_text()
        if "data class " in source:
            pattern = r"\n\)(?:\s*\{|\s*$)"
            replacement = '\n) {\n    override fun toString(): String = "[REDACTED]"\n'
            with_body = bool(re.search(r"\n\)\s*\{", source))
            source, count = re.subn(pattern, lambda _: replacement, source, count=1)
            if count != 1:
                raise ValueError(f"Unrecognized generated model: {path.name}")
            if not with_body:
                source += "}\n"
        path.write_text(BANNER + source)


def swift_descriptions(directory):
    sensitive = set()
    for path in sorted(directory.glob("*.swift")):
        scopes = []
        for line in path.read_text().splitlines():
            declaration = re.match(r"( *)(?:@frozen )?(?:public )?(struct|enum|extension) ([A-Za-z_][A-Za-z0-9_.]*)", line)
            if declaration:
                indent, kind, name = declaration.groups()
                while scopes and scopes[-1][0] >= len(indent):
                    scopes.pop()
                scopes.append((len(indent), kind, name))
            field = re.match(r"( *)public var ([A-Za-z_][A-Za-z0-9_]*):", line)
            if field and re.search(r"password|token|secret|apikey|authorization|cookie", field[2], re.I):
                while scopes and scopes[-1][0] >= len(field[1]):
                    scopes.pop()
                if scopes and scopes[-1][1] == "struct":
                    sensitive.add(".".join(s[2] for s in scopes))
    lines = [BANNER]
    for name in sorted(sensitive):
        lines += [f"extension {name}: CustomStringConvertible, CustomDebugStringConvertible, CustomReflectable {{",
                  '    public var description: String { "[REDACTED]" }',
                  '    public var debugDescription: String { "[REDACTED]" }',
                  '    public var customMirror: Mirror { Mirror(reflecting: "[REDACTED]") }', "}", ""]
    if not sensitive:
        raise ValueError("No secret-bearing Swift structs found; review the generator output")
    (directory / "RedactedDescriptions.swift").write_text("\n".join(lines))
