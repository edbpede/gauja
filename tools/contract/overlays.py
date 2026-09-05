# SPDX-FileCopyrightText: 2026 Gauja contributors
# SPDX-License-Identifier: AGPL-3.0-or-later
"""Apply evidence-backed OpenAPI Overlay 1.0 updates without changing routes."""
from copy import deepcopy
import re

from jsonpath import JSONPathEnvironment

from document import read_document
from operations import operations


def merge(value, update):
    if isinstance(value, dict) and isinstance(update, dict):
        for key, item in update.items():
            value[key] = merge(value[key], item) if key in value else deepcopy(item)
        return value
    if isinstance(value, list) and isinstance(update, list):
        return value + deepcopy(update)
    return deepcopy(update)


def apply_overlays(spec, directory):
    result = deepcopy(spec)
    routes = {(path, method) for path, method, _ in operations(spec)}
    environment = JSONPathEnvironment(strict=True)
    for path in sorted(directory.glob("*.yml")):
        header = "\n".join(line for line in path.read_text().splitlines() if line.startswith("#"))
        evidence = r"https://github\.com/seerr-team/seerr/(?:issues/\d+|pull/\d+|blob/[0-9a-f]{40}/[^\s]+)"
        if not re.search(evidence, header):
            raise ValueError(f"{path.name}: missing upstream evidence citation")
        overlay = read_document(path)
        if overlay.get("overlay") != "1.0.0" or not overlay.get("actions"):
            raise ValueError(f"{path.name}: expected OpenAPI Overlay 1.0.0 actions")
        for action in overlay["actions"]:
            if ("update" in action) == ("remove" in action) or action.get("remove", True) is not True:
                raise ValueError(f"{path.name}: action must update or remove: true")
            matches = list(environment.finditer(action["target"], result))
            if not matches:
                raise ValueError(f"{path.name}: unmatched target {action['target']}")
            # Removing array members must not shift subsequent selected indices.
            matches.sort(key=lambda m: (len(m.parts), str(m.parts[:-1]),
                                       m.parts[-1] if isinstance(m.parts[-1], int) else -1), reverse=True)
            for match in matches:
                if not match.parts:
                    if "remove" in action:
                        raise ValueError("An overlay cannot remove the document")
                    result = merge(result, action["update"])
                    continue
                parent = result
                for key in match.parts[:-1]:
                    parent = parent[key]
                key = match.parts[-1]
                if "remove" in action:
                    del parent[key]
                else:
                    parent[key] = merge(parent[key], action["update"])
        if {(p, m) for p, m, _ in operations(result)} != routes:
            raise ValueError(f"{path.name}: overlays must not invent or remove operations")
    return result
