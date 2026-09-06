// SPDX-FileCopyrightText: 2026 Gauja contributors
// SPDX-License-Identifier: AGPL-3.0-or-later
import Foundation
import Model

public struct ServerVersion: Sendable, Comparable {
    public let major: Int
    public let minor: Int
    public let patch: Int
    public let suffix: String

    public init?(_ text: String?) {
        guard let text,
            let match = text.wholeMatch(
                of: /^v?(0|[1-9][0-9]*)\.(0|[1-9][0-9]*)\.(0|[1-9][0-9]*)([-+][0-9A-Za-z.+-]+)?$/),
            let major = Int(match.1), let minor = Int(match.2), let patch = Int(match.3)
        else { return nil }
        self.major = major
        self.minor = minor
        self.patch = patch
        self.suffix = match.4.map(String.init) ?? ""
    }

    public static func < (lhs: Self, rhs: Self) -> Bool {
        (lhs.major, lhs.minor, lhs.patch) < (rhs.major, rhs.minor, rhs.patch)
    }

    public var compatibility: Compatibility {
        let numbers = (major, minor, patch)
        if numbers < (3, 4, 1) { return .tooOld }
        if numbers > (3, 4, 1) || !suffix.isEmpty { return .untested }
        return .tested
    }
}
