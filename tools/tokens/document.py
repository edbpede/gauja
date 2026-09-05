# SPDX-FileCopyrightText: 2026 Gauja contributors
# SPDX-License-Identifier: AGPL-3.0-or-later
"""Resolve and validate the DTCG types used by Gauja, using only the stdlib."""
from copy import deepcopy
import json
import math
import re


def load(path):
    def pairs(items):
        result = {}
        for key, value in items:
            if key in result:
                raise ValueError(f"Duplicate token key: {key}")
            result[key] = value
        return result
    document = json.loads(path.read_text(), object_pairs_hook=pairs)
    tokens = {}

    def collect(node, prefix):
        if not isinstance(node, dict):
            raise ValueError(f"Invalid token group: {prefix}")
        if "$value" in node:
            if not node.get("$description") or not node.get("$extensions", {}).get("app.gauja.provenance"):
                raise ValueError(f"Token lacks provenance: {prefix}")
            tokens[prefix] = node
        else:
            for key, child in node.items():
                if not key.startswith("$"):
                    collect(child, f"{prefix}.{key}" if prefix else key)
    collect(document, "")

    def resolve(name, chain=()):
        if name in chain or name not in tokens:
            raise ValueError(f"Cyclic or missing token reference: {name}")
        token = tokens[name]
        value = token["$value"]
        if isinstance(value, str) and re.fullmatch(r"\{[^{}]+\}", value):
            target = value[1:-1]
            value = resolve(target, (*chain, name))
            if tokens[target]["$type"] != token["$type"]:
                raise ValueError(f"Reference type mismatch: {name}")
        validate(token["$type"], value)
        return deepcopy(value)
    return {name: resolve(name) for name in sorted(tokens)}


def number(value):
    return isinstance(value, (int, float)) and not isinstance(value, bool) and math.isfinite(value)


def validate(kind, value):
    valid = False
    if kind == "color" and isinstance(value, dict):
        components = value.get("components", [])
        valid = value.get("colorSpace") == "srgb" and len(components) == 3 and all(number(v) and 0 <= v <= 1 for v in components) and number(value.get("alpha", 1)) and 0 <= value.get("alpha", 1) <= 1
    elif kind in {"dimension", "duration"} and isinstance(value, dict):
        valid = number(value.get("value")) and value["value"] >= 0 and value.get("unit") == ("px" if kind == "dimension" else "ms")
    elif kind == "typography" and isinstance(value, dict):
        validate("dimension", value.get("fontSize"))
        validate("dimension", value.get("letterSpacing"))
        valid = value.get("fontFamily") == "system-ui" and value.get("fontWeight") in {400, 500, 600, 700} and number(value.get("lineHeight")) and value["lineHeight"] > 0
    if not valid:
        raise ValueError(f"Invalid or unsupported {kind} token")
