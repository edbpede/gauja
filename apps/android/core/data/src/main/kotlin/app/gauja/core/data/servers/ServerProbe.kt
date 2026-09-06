// SPDX-FileCopyrightText: 2026 Gauja contributors
// SPDX-License-Identifier: AGPL-3.0-or-later
package app.gauja.core.data.servers

import app.gauja.core.model.ServerAddress
import app.gauja.core.model.ServerSnapshot

interface ServerProbe {
    suspend fun check(address: ServerAddress): ServerSnapshot
}
