// SPDX-FileCopyrightText: 2026 Gauja contributors
// SPDX-License-Identifier: AGPL-3.0-or-later
// GENERATED — do not edit. Run tools/tokens/generate.sh.

import SwiftUI
import UIKit

public struct GaujaTypographyStyle {
    public let font: Font
    public let lineHeight: CGFloat
    public let letterSpacing: CGFloat

    private init(size: CGFloat, weight: UIFont.Weight, textStyle: UIFont.TextStyle, lineHeight: CGFloat, letterSpacing: CGFloat) {
        let metrics = UIFontMetrics(forTextStyle: textStyle)
        font = Font(metrics.scaledFont(for: UIFont.systemFont(ofSize: size, weight: weight)))
        self.lineHeight = metrics.scaledValue(for: size * lineHeight)
        self.letterSpacing = metrics.scaledValue(for: letterSpacing)
    }

    public static var bodyLarge: GaujaTypographyStyle {
        GaujaTypographyStyle(size: 16, weight: .regular, textStyle: .body,
            lineHeight: 1.5, letterSpacing: 0)
    }

    public static var bodyMedium: GaujaTypographyStyle {
        GaujaTypographyStyle(size: 14, weight: .regular, textStyle: .body,
            lineHeight: 1.5, letterSpacing: 0)
    }

    public static var bodySmall: GaujaTypographyStyle {
        GaujaTypographyStyle(size: 12, weight: .regular, textStyle: .body,
            lineHeight: 1.5, letterSpacing: 0)
    }

    public static var displayLarge: GaujaTypographyStyle {
        GaujaTypographyStyle(size: 60, weight: .regular, textStyle: .largeTitle,
            lineHeight: 1.1, letterSpacing: 0)
    }

    public static var displayMedium: GaujaTypographyStyle {
        GaujaTypographyStyle(size: 48, weight: .regular, textStyle: .largeTitle,
            lineHeight: 1.1, letterSpacing: 0)
    }

    public static var displaySmall: GaujaTypographyStyle {
        GaujaTypographyStyle(size: 36, weight: .regular, textStyle: .largeTitle,
            lineHeight: 1.2, letterSpacing: 0)
    }

    public static var headlineLarge: GaujaTypographyStyle {
        GaujaTypographyStyle(size: 32, weight: .regular, textStyle: .title1,
            lineHeight: 1.25, letterSpacing: 0)
    }

    public static var headlineMedium: GaujaTypographyStyle {
        GaujaTypographyStyle(size: 28, weight: .regular, textStyle: .title1,
            lineHeight: 1.3, letterSpacing: 0)
    }

    public static var headlineSmall: GaujaTypographyStyle {
        GaujaTypographyStyle(size: 24, weight: .regular, textStyle: .title1,
            lineHeight: 1.3, letterSpacing: 0)
    }

    public static var labelLarge: GaujaTypographyStyle {
        GaujaTypographyStyle(size: 14, weight: .medium, textStyle: .caption1,
            lineHeight: 1.4, letterSpacing: 0)
    }

    public static var labelMedium: GaujaTypographyStyle {
        GaujaTypographyStyle(size: 12, weight: .semibold, textStyle: .caption1,
            lineHeight: 1.4, letterSpacing: 0)
    }

    public static var labelSmall: GaujaTypographyStyle {
        GaujaTypographyStyle(size: 11, weight: .semibold, textStyle: .caption1,
            lineHeight: 1.4, letterSpacing: 0)
    }

    public static var titleLarge: GaujaTypographyStyle {
        GaujaTypographyStyle(size: 22, weight: .medium, textStyle: .headline,
            lineHeight: 1.3, letterSpacing: 0)
    }

    public static var titleMedium: GaujaTypographyStyle {
        GaujaTypographyStyle(size: 16, weight: .medium, textStyle: .headline,
            lineHeight: 1.5, letterSpacing: 0)
    }

    public static var titleSmall: GaujaTypographyStyle {
        GaujaTypographyStyle(size: 14, weight: .medium, textStyle: .headline,
            lineHeight: 1.4, letterSpacing: 0)
    }
}
