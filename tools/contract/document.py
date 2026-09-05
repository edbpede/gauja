# SPDX-FileCopyrightText: 2026 Gauja contributors
# SPDX-License-Identifier: AGPL-3.0-or-later
"""Load YAML without duplicate keys, executable tags or implicit date coercion."""
from pathlib import Path

import yaml


class ContractLoader(yaml.SafeLoader):
    yaml_implicit_resolvers = {
        key: [(tag, pattern) for tag, pattern in values
              if tag not in {"tag:yaml.org,2002:timestamp", "tag:yaml.org,2002:bool"}]
        for key, values in yaml.SafeLoader.yaml_implicit_resolvers.items()
    }

    def construct_mapping(self, node, deep=False):
        result = {}
        for key_node, value_node in node.value:
            key = self.construct_object(key_node, deep=deep)
            if key in result:
                raise ValueError(f"Duplicate YAML key: {key}")
            result[key] = self.construct_object(value_node, deep=deep)
        return result


# YAML 1.2 booleans: do not turn the OpenAPI parameter name 'on' into True.
import re
ContractLoader.add_implicit_resolver(
    "tag:yaml.org,2002:bool", re.compile(r"^(?:true|false)$"), list("tf")
)


def read_document(path: Path):
    return yaml.load(path.read_text(), Loader=ContractLoader)
