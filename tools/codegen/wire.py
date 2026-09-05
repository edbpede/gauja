# SPDX-FileCopyrightText: 2026 Gauja contributors
# SPDX-License-Identifier: AGPL-3.0-or-later
"""Lower wire enums to primitives without weakening the canonical contract."""
from copy import deepcopy


def wire_document(spec):
    result = deepcopy(spec)

    def visit(node):
        if isinstance(node, list):
            for item in node:
                visit(item)
        elif isinstance(node, dict):
            # Future values reach Data's explicit domain Unknown branch. Outgoing
            # domain validation uses the enum constraints in the effective contract.
            if node.get("type") == "number" and "format" not in node:
                node["format"] = "double"
            if "enum" in node:
                node["x-gauja-known-values"] = node.pop("enum")
            for item in node.values():
                visit(item)
    visit(result)
    return result
