// SPDX-FileCopyrightText: 2026 Gauja contributors
// SPDX-License-Identifier: AGPL-3.0-or-later
// GENERATED — do not edit. Run tools/tokens/generate.sh.

import SwiftUI

public extension Color {
    static func gaujaApprovedBackground(_ scheme: ColorScheme = .dark) -> Color {
        switch scheme {
        case .light: Color(.sRGB, red: 0.878431373, green: 0.905882353, blue: 1.000000000, opacity: 1.000000000)
        case .dark: Color(.sRGB, red: 0.192156863, green: 0.180392157, blue: 0.505882353, opacity: 1.000000000)
        @unknown default: gaujaApprovedBackground(.dark)
        }
    }

    static func gaujaApprovedForeground(_ scheme: ColorScheme = .dark) -> Color {
        switch scheme {
        case .light: Color(.sRGB, red: 0.192156863, green: 0.180392157, blue: 0.505882353, opacity: 1.000000000)
        case .dark: Color(.sRGB, red: 0.878431373, green: 0.905882353, blue: 1.000000000, opacity: 1.000000000)
        @unknown default: gaujaApprovedForeground(.dark)
        }
    }

    static func gaujaAvailableBackground(_ scheme: ColorScheme = .dark) -> Color {
        switch scheme {
        case .light: Color(.sRGB, red: 0.862745098, green: 0.988235294, blue: 0.905882353, opacity: 1.000000000)
        case .dark: Color(.sRGB, red: 0.078431373, green: 0.325490196, blue: 0.176470588, opacity: 1.000000000)
        @unknown default: gaujaAvailableBackground(.dark)
        }
    }

    static func gaujaAvailableForeground(_ scheme: ColorScheme = .dark) -> Color {
        switch scheme {
        case .light: Color(.sRGB, red: 0.078431373, green: 0.325490196, blue: 0.176470588, opacity: 1.000000000)
        case .dark: Color(.sRGB, red: 0.862745098, green: 0.988235294, blue: 0.905882353, opacity: 1.000000000)
        @unknown default: gaujaAvailableForeground(.dark)
        }
    }

    static func gaujaBackground(_ scheme: ColorScheme = .dark) -> Color {
        switch scheme {
        case .light: Color(.sRGB, red: 0.976470588, green: 0.980392157, blue: 0.984313725, opacity: 1.000000000)
        case .dark: Color(.sRGB, red: 0.066666667, green: 0.094117647, blue: 0.152941176, opacity: 1.000000000)
        @unknown default: gaujaBackground(.dark)
        }
    }

    static func gaujaBlocklistedBackground(_ scheme: ColorScheme = .dark) -> Color {
        switch scheme {
        case .light: Color(.sRGB, red: 0.996078431, green: 0.886274510, blue: 0.886274510, opacity: 1.000000000)
        case .dark: Color(.sRGB, red: 0.498039216, green: 0.113725490, blue: 0.113725490, opacity: 1.000000000)
        @unknown default: gaujaBlocklistedBackground(.dark)
        }
    }

    static func gaujaBlocklistedForeground(_ scheme: ColorScheme = .dark) -> Color {
        switch scheme {
        case .light: Color(.sRGB, red: 0.498039216, green: 0.113725490, blue: 0.113725490, opacity: 1.000000000)
        case .dark: Color(.sRGB, red: 0.996078431, green: 0.886274510, blue: 0.886274510, opacity: 1.000000000)
        @unknown default: gaujaBlocklistedForeground(.dark)
        }
    }

    static func gaujaCompletedBackground(_ scheme: ColorScheme = .dark) -> Color {
        switch scheme {
        case .light: Color(.sRGB, red: 0.862745098, green: 0.988235294, blue: 0.905882353, opacity: 1.000000000)
        case .dark: Color(.sRGB, red: 0.078431373, green: 0.325490196, blue: 0.176470588, opacity: 1.000000000)
        @unknown default: gaujaCompletedBackground(.dark)
        }
    }

    static func gaujaCompletedForeground(_ scheme: ColorScheme = .dark) -> Color {
        switch scheme {
        case .light: Color(.sRGB, red: 0.078431373, green: 0.325490196, blue: 0.176470588, opacity: 1.000000000)
        case .dark: Color(.sRGB, red: 0.862745098, green: 0.988235294, blue: 0.905882353, opacity: 1.000000000)
        @unknown default: gaujaCompletedForeground(.dark)
        }
    }

    static func gaujaDeclinedBackground(_ scheme: ColorScheme = .dark) -> Color {
        switch scheme {
        case .light: Color(.sRGB, red: 0.996078431, green: 0.886274510, blue: 0.886274510, opacity: 1.000000000)
        case .dark: Color(.sRGB, red: 0.498039216, green: 0.113725490, blue: 0.113725490, opacity: 1.000000000)
        @unknown default: gaujaDeclinedBackground(.dark)
        }
    }

    static func gaujaDeclinedForeground(_ scheme: ColorScheme = .dark) -> Color {
        switch scheme {
        case .light: Color(.sRGB, red: 0.498039216, green: 0.113725490, blue: 0.113725490, opacity: 1.000000000)
        case .dark: Color(.sRGB, red: 0.996078431, green: 0.886274510, blue: 0.886274510, opacity: 1.000000000)
        @unknown default: gaujaDeclinedForeground(.dark)
        }
    }

    static func gaujaDeletedBackground(_ scheme: ColorScheme = .dark) -> Color {
        switch scheme {
        case .light: Color(.sRGB, red: 0.996078431, green: 0.886274510, blue: 0.886274510, opacity: 1.000000000)
        case .dark: Color(.sRGB, red: 0.498039216, green: 0.113725490, blue: 0.113725490, opacity: 1.000000000)
        @unknown default: gaujaDeletedBackground(.dark)
        }
    }

    static func gaujaDeletedForeground(_ scheme: ColorScheme = .dark) -> Color {
        switch scheme {
        case .light: Color(.sRGB, red: 0.498039216, green: 0.113725490, blue: 0.113725490, opacity: 1.000000000)
        case .dark: Color(.sRGB, red: 0.996078431, green: 0.886274510, blue: 0.886274510, opacity: 1.000000000)
        @unknown default: gaujaDeletedForeground(.dark)
        }
    }

    static func gaujaError(_ scheme: ColorScheme = .dark) -> Color {
        switch scheme {
        case .light: Color(.sRGB, red: 0.725490196, green: 0.109803922, blue: 0.109803922, opacity: 1.000000000)
        case .dark: Color(.sRGB, red: 0.996078431, green: 0.886274510, blue: 0.886274510, opacity: 1.000000000)
        @unknown default: gaujaError(.dark)
        }
    }

    static func gaujaErrorContainer(_ scheme: ColorScheme = .dark) -> Color {
        switch scheme {
        case .light: Color(.sRGB, red: 0.996078431, green: 0.886274510, blue: 0.886274510, opacity: 1.000000000)
        case .dark: Color(.sRGB, red: 0.498039216, green: 0.113725490, blue: 0.113725490, opacity: 1.000000000)
        @unknown default: gaujaErrorContainer(.dark)
        }
    }

    static func gaujaFailedBackground(_ scheme: ColorScheme = .dark) -> Color {
        switch scheme {
        case .light: Color(.sRGB, red: 0.996078431, green: 0.886274510, blue: 0.886274510, opacity: 1.000000000)
        case .dark: Color(.sRGB, red: 0.498039216, green: 0.113725490, blue: 0.113725490, opacity: 1.000000000)
        @unknown default: gaujaFailedBackground(.dark)
        }
    }

    static func gaujaFailedForeground(_ scheme: ColorScheme = .dark) -> Color {
        switch scheme {
        case .light: Color(.sRGB, red: 0.498039216, green: 0.113725490, blue: 0.113725490, opacity: 1.000000000)
        case .dark: Color(.sRGB, red: 0.996078431, green: 0.886274510, blue: 0.886274510, opacity: 1.000000000)
        @unknown default: gaujaFailedForeground(.dark)
        }
    }

    static func gaujaHeroEnd(_ scheme: ColorScheme = .dark) -> Color {
        switch scheme {
        case .light: Color(.sRGB, red: 0.752941176, green: 0.517647059, blue: 0.988235294, opacity: 1.000000000)
        case .dark: Color(.sRGB, red: 0.752941176, green: 0.517647059, blue: 0.988235294, opacity: 1.000000000)
        @unknown default: gaujaHeroEnd(.dark)
        }
    }

    static func gaujaHeroStart(_ scheme: ColorScheme = .dark) -> Color {
        switch scheme {
        case .light: Color(.sRGB, red: 0.505882353, green: 0.549019608, blue: 0.972549020, opacity: 1.000000000)
        case .dark: Color(.sRGB, red: 0.505882353, green: 0.549019608, blue: 0.972549020, opacity: 1.000000000)
        @unknown default: gaujaHeroStart(.dark)
        }
    }

    static func gaujaInverseOnSurface(_ scheme: ColorScheme = .dark) -> Color {
        switch scheme {
        case .light: Color(.sRGB, red: 0.952941176, green: 0.956862745, blue: 0.964705882, opacity: 1.000000000)
        case .dark: Color(.sRGB, red: 0.066666667, green: 0.094117647, blue: 0.152941176, opacity: 1.000000000)
        @unknown default: gaujaInverseOnSurface(.dark)
        }
    }

    static func gaujaInversePrimary(_ scheme: ColorScheme = .dark) -> Color {
        switch scheme {
        case .light: Color(.sRGB, red: 0.780392157, green: 0.823529412, blue: 0.996078431, opacity: 1.000000000)
        case .dark: Color(.sRGB, red: 0.309803922, green: 0.274509804, blue: 0.898039216, opacity: 1.000000000)
        @unknown default: gaujaInversePrimary(.dark)
        }
    }

    static func gaujaInverseSurface(_ scheme: ColorScheme = .dark) -> Color {
        switch scheme {
        case .light: Color(.sRGB, red: 0.066666667, green: 0.094117647, blue: 0.152941176, opacity: 1.000000000)
        case .dark: Color(.sRGB, red: 0.952941176, green: 0.956862745, blue: 0.964705882, opacity: 1.000000000)
        @unknown default: gaujaInverseSurface(.dark)
        }
    }

    static func gaujaOnBackground(_ scheme: ColorScheme = .dark) -> Color {
        switch scheme {
        case .light: Color(.sRGB, red: 0.066666667, green: 0.094117647, blue: 0.152941176, opacity: 1.000000000)
        case .dark: Color(.sRGB, red: 0.952941176, green: 0.956862745, blue: 0.964705882, opacity: 1.000000000)
        @unknown default: gaujaOnBackground(.dark)
        }
    }

    static func gaujaOnError(_ scheme: ColorScheme = .dark) -> Color {
        switch scheme {
        case .light: Color(.sRGB, red: 1.000000000, green: 1.000000000, blue: 1.000000000, opacity: 1.000000000)
        case .dark: Color(.sRGB, red: 0.498039216, green: 0.113725490, blue: 0.113725490, opacity: 1.000000000)
        @unknown default: gaujaOnError(.dark)
        }
    }

    static func gaujaOnErrorContainer(_ scheme: ColorScheme = .dark) -> Color {
        switch scheme {
        case .light: Color(.sRGB, red: 0.498039216, green: 0.113725490, blue: 0.113725490, opacity: 1.000000000)
        case .dark: Color(.sRGB, red: 0.996078431, green: 0.886274510, blue: 0.886274510, opacity: 1.000000000)
        @unknown default: gaujaOnErrorContainer(.dark)
        }
    }

    static func gaujaOnPrimary(_ scheme: ColorScheme = .dark) -> Color {
        switch scheme {
        case .light: Color(.sRGB, red: 1.000000000, green: 1.000000000, blue: 1.000000000, opacity: 1.000000000)
        case .dark: Color(.sRGB, red: 1.000000000, green: 1.000000000, blue: 1.000000000, opacity: 1.000000000)
        @unknown default: gaujaOnPrimary(.dark)
        }
    }

    static func gaujaOnPrimaryContainer(_ scheme: ColorScheme = .dark) -> Color {
        switch scheme {
        case .light: Color(.sRGB, red: 0.192156863, green: 0.180392157, blue: 0.505882353, opacity: 1.000000000)
        case .dark: Color(.sRGB, red: 0.878431373, green: 0.905882353, blue: 1.000000000, opacity: 1.000000000)
        @unknown default: gaujaOnPrimaryContainer(.dark)
        }
    }

    static func gaujaOnSecondary(_ scheme: ColorScheme = .dark) -> Color {
        switch scheme {
        case .light: Color(.sRGB, red: 1.000000000, green: 1.000000000, blue: 1.000000000, opacity: 1.000000000)
        case .dark: Color(.sRGB, red: 0.011764706, green: 0.027450980, blue: 0.070588235, opacity: 1.000000000)
        @unknown default: gaujaOnSecondary(.dark)
        }
    }

    static func gaujaOnSecondaryContainer(_ scheme: ColorScheme = .dark) -> Color {
        switch scheme {
        case .light: Color(.sRGB, red: 0.066666667, green: 0.094117647, blue: 0.152941176, opacity: 1.000000000)
        case .dark: Color(.sRGB, red: 0.952941176, green: 0.956862745, blue: 0.964705882, opacity: 1.000000000)
        @unknown default: gaujaOnSecondaryContainer(.dark)
        }
    }

    static func gaujaOnSurface(_ scheme: ColorScheme = .dark) -> Color {
        switch scheme {
        case .light: Color(.sRGB, red: 0.066666667, green: 0.094117647, blue: 0.152941176, opacity: 1.000000000)
        case .dark: Color(.sRGB, red: 0.952941176, green: 0.956862745, blue: 0.964705882, opacity: 1.000000000)
        @unknown default: gaujaOnSurface(.dark)
        }
    }

    static func gaujaOnSurfaceVariant(_ scheme: ColorScheme = .dark) -> Color {
        switch scheme {
        case .light: Color(.sRGB, red: 0.215686275, green: 0.254901961, blue: 0.317647059, opacity: 1.000000000)
        case .dark: Color(.sRGB, red: 0.819607843, green: 0.835294118, blue: 0.858823529, opacity: 1.000000000)
        @unknown default: gaujaOnSurfaceVariant(.dark)
        }
    }

    static func gaujaOnTertiary(_ scheme: ColorScheme = .dark) -> Color {
        switch scheme {
        case .light: Color(.sRGB, red: 1.000000000, green: 1.000000000, blue: 1.000000000, opacity: 1.000000000)
        case .dark: Color(.sRGB, red: 0.011764706, green: 0.027450980, blue: 0.070588235, opacity: 1.000000000)
        @unknown default: gaujaOnTertiary(.dark)
        }
    }

    static func gaujaOnTertiaryContainer(_ scheme: ColorScheme = .dark) -> Color {
        switch scheme {
        case .light: Color(.sRGB, red: 0.345098039, green: 0.109803922, blue: 0.529411765, opacity: 1.000000000)
        case .dark: Color(.sRGB, red: 0.952941176, green: 0.909803922, blue: 1.000000000, opacity: 1.000000000)
        @unknown default: gaujaOnTertiaryContainer(.dark)
        }
    }

    static func gaujaOutline(_ scheme: ColorScheme = .dark) -> Color {
        switch scheme {
        case .light: Color(.sRGB, red: 0.294117647, green: 0.333333333, blue: 0.388235294, opacity: 1.000000000)
        case .dark: Color(.sRGB, red: 0.611764706, green: 0.639215686, blue: 0.686274510, opacity: 1.000000000)
        @unknown default: gaujaOutline(.dark)
        }
    }

    static func gaujaOutlineVariant(_ scheme: ColorScheme = .dark) -> Color {
        switch scheme {
        case .light: Color(.sRGB, red: 0.819607843, green: 0.835294118, blue: 0.858823529, opacity: 1.000000000)
        case .dark: Color(.sRGB, red: 0.294117647, green: 0.333333333, blue: 0.388235294, opacity: 1.000000000)
        @unknown default: gaujaOutlineVariant(.dark)
        }
    }

    static func gaujaPartiallyAvailableBackground(_ scheme: ColorScheme = .dark) -> Color {
        switch scheme {
        case .light: Color(.sRGB, red: 0.862745098, green: 0.988235294, blue: 0.905882353, opacity: 1.000000000)
        case .dark: Color(.sRGB, red: 0.078431373, green: 0.325490196, blue: 0.176470588, opacity: 1.000000000)
        @unknown default: gaujaPartiallyAvailableBackground(.dark)
        }
    }

    static func gaujaPartiallyAvailableForeground(_ scheme: ColorScheme = .dark) -> Color {
        switch scheme {
        case .light: Color(.sRGB, red: 0.078431373, green: 0.325490196, blue: 0.176470588, opacity: 1.000000000)
        case .dark: Color(.sRGB, red: 0.862745098, green: 0.988235294, blue: 0.905882353, opacity: 1.000000000)
        @unknown default: gaujaPartiallyAvailableForeground(.dark)
        }
    }

    static func gaujaPendingBackground(_ scheme: ColorScheme = .dark) -> Color {
        switch scheme {
        case .light: Color(.sRGB, red: 0.996078431, green: 0.976470588, blue: 0.764705882, opacity: 1.000000000)
        case .dark: Color(.sRGB, red: 0.443137255, green: 0.247058824, blue: 0.070588235, opacity: 1.000000000)
        @unknown default: gaujaPendingBackground(.dark)
        }
    }

    static func gaujaPendingForeground(_ scheme: ColorScheme = .dark) -> Color {
        switch scheme {
        case .light: Color(.sRGB, red: 0.443137255, green: 0.247058824, blue: 0.070588235, opacity: 1.000000000)
        case .dark: Color(.sRGB, red: 0.996078431, green: 0.976470588, blue: 0.764705882, opacity: 1.000000000)
        @unknown default: gaujaPendingForeground(.dark)
        }
    }

    static func gaujaPrimary(_ scheme: ColorScheme = .dark) -> Color {
        switch scheme {
        case .light: Color(.sRGB, red: 0.309803922, green: 0.274509804, blue: 0.898039216, opacity: 1.000000000)
        case .dark: Color(.sRGB, red: 0.309803922, green: 0.274509804, blue: 0.898039216, opacity: 1.000000000)
        @unknown default: gaujaPrimary(.dark)
        }
    }

    static func gaujaPrimaryContainer(_ scheme: ColorScheme = .dark) -> Color {
        switch scheme {
        case .light: Color(.sRGB, red: 0.878431373, green: 0.905882353, blue: 1.000000000, opacity: 1.000000000)
        case .dark: Color(.sRGB, red: 0.192156863, green: 0.180392157, blue: 0.505882353, opacity: 1.000000000)
        @unknown default: gaujaPrimaryContainer(.dark)
        }
    }

    static func gaujaProcessingBackground(_ scheme: ColorScheme = .dark) -> Color {
        switch scheme {
        case .light: Color(.sRGB, red: 0.878431373, green: 0.905882353, blue: 1.000000000, opacity: 1.000000000)
        case .dark: Color(.sRGB, red: 0.192156863, green: 0.180392157, blue: 0.505882353, opacity: 1.000000000)
        @unknown default: gaujaProcessingBackground(.dark)
        }
    }

    static func gaujaProcessingForeground(_ scheme: ColorScheme = .dark) -> Color {
        switch scheme {
        case .light: Color(.sRGB, red: 0.192156863, green: 0.180392157, blue: 0.505882353, opacity: 1.000000000)
        case .dark: Color(.sRGB, red: 0.878431373, green: 0.905882353, blue: 1.000000000, opacity: 1.000000000)
        @unknown default: gaujaProcessingForeground(.dark)
        }
    }

    static func gaujaScrim(_ scheme: ColorScheme = .dark) -> Color {
        switch scheme {
        case .light: Color(.sRGB, red: 0.011764706, green: 0.027450980, blue: 0.070588235, opacity: 1.000000000)
        case .dark: Color(.sRGB, red: 0.011764706, green: 0.027450980, blue: 0.070588235, opacity: 1.000000000)
        @unknown default: gaujaScrim(.dark)
        }
    }

    static func gaujaSecondary(_ scheme: ColorScheme = .dark) -> Color {
        switch scheme {
        case .light: Color(.sRGB, red: 0.262745098, green: 0.219607843, blue: 0.792156863, opacity: 1.000000000)
        case .dark: Color(.sRGB, red: 0.505882353, green: 0.549019608, blue: 0.972549020, opacity: 1.000000000)
        @unknown default: gaujaSecondary(.dark)
        }
    }

    static func gaujaSecondaryContainer(_ scheme: ColorScheme = .dark) -> Color {
        switch scheme {
        case .light: Color(.sRGB, red: 0.898039216, green: 0.905882353, blue: 0.921568627, opacity: 1.000000000)
        case .dark: Color(.sRGB, red: 0.215686275, green: 0.254901961, blue: 0.317647059, opacity: 1.000000000)
        @unknown default: gaujaSecondaryContainer(.dark)
        }
    }

    static func gaujaSurface(_ scheme: ColorScheme = .dark) -> Color {
        switch scheme {
        case .light: Color(.sRGB, red: 1.000000000, green: 1.000000000, blue: 1.000000000, opacity: 1.000000000)
        case .dark: Color(.sRGB, red: 0.066666667, green: 0.094117647, blue: 0.152941176, opacity: 1.000000000)
        @unknown default: gaujaSurface(.dark)
        }
    }

    static func gaujaSurfaceBright(_ scheme: ColorScheme = .dark) -> Color {
        switch scheme {
        case .light: Color(.sRGB, red: 1.000000000, green: 1.000000000, blue: 1.000000000, opacity: 1.000000000)
        case .dark: Color(.sRGB, red: 0.215686275, green: 0.254901961, blue: 0.317647059, opacity: 1.000000000)
        @unknown default: gaujaSurfaceBright(.dark)
        }
    }

    static func gaujaSurfaceContainer(_ scheme: ColorScheme = .dark) -> Color {
        switch scheme {
        case .light: Color(.sRGB, red: 0.952941176, green: 0.956862745, blue: 0.964705882, opacity: 1.000000000)
        case .dark: Color(.sRGB, red: 0.121568627, green: 0.160784314, blue: 0.215686275, opacity: 1.000000000)
        @unknown default: gaujaSurfaceContainer(.dark)
        }
    }

    static func gaujaSurfaceContainerHigh(_ scheme: ColorScheme = .dark) -> Color {
        switch scheme {
        case .light: Color(.sRGB, red: 0.898039216, green: 0.905882353, blue: 0.921568627, opacity: 1.000000000)
        case .dark: Color(.sRGB, red: 0.215686275, green: 0.254901961, blue: 0.317647059, opacity: 1.000000000)
        @unknown default: gaujaSurfaceContainerHigh(.dark)
        }
    }

    static func gaujaSurfaceContainerHighest(_ scheme: ColorScheme = .dark) -> Color {
        switch scheme {
        case .light: Color(.sRGB, red: 0.819607843, green: 0.835294118, blue: 0.858823529, opacity: 1.000000000)
        case .dark: Color(.sRGB, red: 0.294117647, green: 0.333333333, blue: 0.388235294, opacity: 1.000000000)
        @unknown default: gaujaSurfaceContainerHighest(.dark)
        }
    }

    static func gaujaSurfaceContainerLow(_ scheme: ColorScheme = .dark) -> Color {
        switch scheme {
        case .light: Color(.sRGB, red: 0.976470588, green: 0.980392157, blue: 0.984313725, opacity: 1.000000000)
        case .dark: Color(.sRGB, red: 0.066666667, green: 0.094117647, blue: 0.152941176, opacity: 1.000000000)
        @unknown default: gaujaSurfaceContainerLow(.dark)
        }
    }

    static func gaujaSurfaceContainerLowest(_ scheme: ColorScheme = .dark) -> Color {
        switch scheme {
        case .light: Color(.sRGB, red: 1.000000000, green: 1.000000000, blue: 1.000000000, opacity: 1.000000000)
        case .dark: Color(.sRGB, red: 0.011764706, green: 0.027450980, blue: 0.070588235, opacity: 1.000000000)
        @unknown default: gaujaSurfaceContainerLowest(.dark)
        }
    }

    static func gaujaSurfaceDim(_ scheme: ColorScheme = .dark) -> Color {
        switch scheme {
        case .light: Color(.sRGB, red: 0.898039216, green: 0.905882353, blue: 0.921568627, opacity: 1.000000000)
        case .dark: Color(.sRGB, red: 0.011764706, green: 0.027450980, blue: 0.070588235, opacity: 1.000000000)
        @unknown default: gaujaSurfaceDim(.dark)
        }
    }

    static func gaujaSurfaceTint(_ scheme: ColorScheme = .dark) -> Color {
        switch scheme {
        case .light: Color(.sRGB, red: 0.309803922, green: 0.274509804, blue: 0.898039216, opacity: 1.000000000)
        case .dark: Color(.sRGB, red: 0.505882353, green: 0.549019608, blue: 0.972549020, opacity: 1.000000000)
        @unknown default: gaujaSurfaceTint(.dark)
        }
    }

    static func gaujaSurfaceVariant(_ scheme: ColorScheme = .dark) -> Color {
        switch scheme {
        case .light: Color(.sRGB, red: 0.952941176, green: 0.956862745, blue: 0.964705882, opacity: 1.000000000)
        case .dark: Color(.sRGB, red: 0.215686275, green: 0.254901961, blue: 0.317647059, opacity: 1.000000000)
        @unknown default: gaujaSurfaceVariant(.dark)
        }
    }

    static func gaujaTertiary(_ scheme: ColorScheme = .dark) -> Color {
        switch scheme {
        case .light: Color(.sRGB, red: 0.494117647, green: 0.133333333, blue: 0.807843137, opacity: 1.000000000)
        case .dark: Color(.sRGB, red: 0.752941176, green: 0.517647059, blue: 0.988235294, opacity: 1.000000000)
        @unknown default: gaujaTertiary(.dark)
        }
    }

    static func gaujaTertiaryContainer(_ scheme: ColorScheme = .dark) -> Color {
        switch scheme {
        case .light: Color(.sRGB, red: 0.952941176, green: 0.909803922, blue: 1.000000000, opacity: 1.000000000)
        case .dark: Color(.sRGB, red: 0.345098039, green: 0.109803922, blue: 0.529411765, opacity: 1.000000000)
        @unknown default: gaujaTertiaryContainer(.dark)
        }
    }

    static func gaujaUnknownBackground(_ scheme: ColorScheme = .dark) -> Color {
        switch scheme {
        case .light: Color(.sRGB, red: 0.952941176, green: 0.956862745, blue: 0.964705882, opacity: 1.000000000)
        case .dark: Color(.sRGB, red: 0.215686275, green: 0.254901961, blue: 0.317647059, opacity: 1.000000000)
        @unknown default: gaujaUnknownBackground(.dark)
        }
    }

    static func gaujaUnknownForeground(_ scheme: ColorScheme = .dark) -> Color {
        switch scheme {
        case .light: Color(.sRGB, red: 0.215686275, green: 0.254901961, blue: 0.317647059, opacity: 1.000000000)
        case .dark: Color(.sRGB, red: 0.952941176, green: 0.956862745, blue: 0.964705882, opacity: 1.000000000)
        @unknown default: gaujaUnknownForeground(.dark)
        }
    }
}
