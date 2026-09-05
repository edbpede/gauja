// SPDX-FileCopyrightText: 2026 Gauja contributors
// SPDX-License-Identifier: AGPL-3.0-or-later
// GENERATED — do not edit. Run tools/tokens/generate.sh.
public enum GaujaMotion {
    public static func fast(reduceMotion: Bool = false) -> Double { reduceMotion ? 0 : 0.15 }
    public static func none(reduceMotion: Bool = false) -> Double { reduceMotion ? 0 : 0 }
    public static func slow(reduceMotion: Bool = false) -> Double { reduceMotion ? 0 : 0.3 }
    public static func standard(reduceMotion: Bool = false) -> Double { reduceMotion ? 0 : 0.2 }
}
