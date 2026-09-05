# SPDX-FileCopyrightText: 2026 Gauja contributors
# SPDX-License-Identifier: AGPL-3.0-or-later
"""One stable operation identity shared by clients, coverage and recordings."""
import re

METHODS = {"get", "put", "post", "delete", "patch", "head", "options", "trace"}


def operations(spec):
    for path, item in sorted(spec["paths"].items()):
        for method, operation in sorted(item.items()):
            if method in METHODS:
                yield path, method, operation


def assign_operation_ids(spec):
    seen = set()
    for path, method, operation in operations(spec):
        pieces = []
        for segment in path.strip("/").split("/"):
            parameter = segment.startswith("{") and segment.endswith("}")
            words = re.findall(r"[A-Za-z0-9]+", segment)
            pieces.append(("By" if parameter else "") + "".join(w[:1].upper() + w[1:] for w in words))
        name = operation.setdefault("operationId", method + "".join(pieces))
        if not isinstance(name, str) or not re.fullmatch(r"[A-Za-z][A-Za-z0-9_]*", name) or name in seen:
            raise ValueError(f"Invalid or duplicate operation ID: {name}")
        seen.add(name)
