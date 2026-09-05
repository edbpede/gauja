# SPDX-FileCopyrightText: 2026 Gauja contributors
# SPDX-License-Identifier: AGPL-3.0-or-later
"""Validate contract references, provenance and compatibility before generation."""
import datetime
import json
import re

from jsonschema import Draft202012Validator

from document import read_document
from operations import assign_operation_ids, operations
from overlays import apply_overlays


def validate_coverage(spec, coverage):
    ids = {op["operationId"] for _, _, op in operations(spec)}
    if not isinstance(coverage, dict) or set(coverage) != ids:
        raise ValueError("coverage.json must cover exactly the effective operation IDs")
    for name, entry in coverage.items():
        if (not isinstance(entry, dict) or
                entry.get("status") not in ("planned", "implemented", "excluded") or
                not entry.get("phase") or not entry.get("note")):
            raise ValueError(f"Invalid coverage metadata: {name}")


def validate_refs(value, document):
    if isinstance(value, list):
        for item in value:
            validate_refs(item, document)
    elif isinstance(value, dict):
        if "$ref" in value:
            ref = value["$ref"]
            if not isinstance(ref, str) or (ref != "#" and not ref.startswith("#/")):
                raise ValueError("Only local OpenAPI references are allowed")
            node = document
            try:
                for part in ref[2:].split("/") if ref != "#" else []:
                    part = part.replace("~1", "/").replace("~0", "~")
                    if isinstance(node, list):
                        if not re.fullmatch(r"0|[1-9][0-9]*", part):
                            raise ValueError("Invalid array index")
                        node = node[int(part)]
                    else:
                        node = node[part]
            except (KeyError, IndexError, TypeError, ValueError) as error:
                raise ValueError(f"Unresolved reference: {ref}") from error
        for item in value.values():
            validate_refs(item, document)


def load_contract(api):
    lines = (api / "UPSTREAM_COMMIT").read_text().splitlines()
    if len(lines) != 2 or not re.fullmatch(r"[0-9a-f]{40}", lines[0]):
        raise ValueError("UPSTREAM_COMMIT must contain a full SHA and commented fetch date")
    if not re.fullmatch(r"# Fetched: \d{4}-\d{2}-\d{2}", lines[1]):
        raise ValueError("Missing fetch date")
    datetime.date.fromisoformat(lines[1].removeprefix("# Fetched: "))
    raw = read_document(api / "seerr-api.yml")
    if raw.get("openapi") != "3.0.2" or not raw.get("paths"):
        raise ValueError("Expected a nonempty OpenAPI 3.0.2 document")
    spec = apply_overlays(raw, api / "overlays")
    assign_operation_ids(spec)
    validate_refs(spec, spec)
    declared = {tag["name"] for tag in spec["tags"]}
    for path, method, operation in operations(spec):
        if not path.startswith("/") or not operation.get("responses"):
            raise ValueError(f"Invalid operation: {method} {path}")
        if not operation.get("tags") or not set(operation["tags"]) <= declared:
            raise ValueError(f"Undeclared operation tags: {method} {path}")
    compat = json.loads((api / "compat.json").read_text())
    schema = json.loads((api / "compat.schema.json").read_text())
    Draft202012Validator.check_schema(schema)
    Draft202012Validator(schema).validate(compat)
    callable_paths = {path for path, _, _ in operations(spec)}
    for name, gate in compat.items():
        if gate["endpoint"] not in callable_paths:
            raise ValueError(f"{name}: endpoint absent from pinned contract")
        if gate["max"] is not None and tuple(map(int, gate["min"].split("."))) > tuple(map(int, gate["max"].split("."))):
            raise ValueError(f"{name}: inverted supported version range")
    return spec
