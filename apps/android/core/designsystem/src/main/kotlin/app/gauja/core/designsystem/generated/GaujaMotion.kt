// SPDX-FileCopyrightText: 2026 Gauja contributors
// SPDX-License-Identifier: AGPL-3.0-or-later
// GENERATED — do not edit. Run tools/tokens/generate.sh.
package app.gauja.core.designsystem.generated

object GaujaMotion {
    fun fast(reduceMotion: Boolean = false): Int = if (reduceMotion) 0 else 150
    fun none(reduceMotion: Boolean = false): Int = if (reduceMotion) 0 else 0
    fun slow(reduceMotion: Boolean = false): Int = if (reduceMotion) 0 else 300
    fun standard(reduceMotion: Boolean = false): Int = if (reduceMotion) 0 else 200
}
