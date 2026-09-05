// SPDX-FileCopyrightText: 2026 Gauja contributors
// SPDX-License-Identifier: AGPL-3.0-or-later
package app.gauja.core.designsystem

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import app.gauja.core.designsystem.generated.GaujaDarkColorScheme
import app.gauja.core.designsystem.generated.GaujaLightColorScheme
import app.gauja.core.designsystem.generated.GaujaShapes
import app.gauja.core.designsystem.generated.GaujaTypography

@Composable
fun GaujaTheme(darkTheme: Boolean = true, content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (darkTheme) GaujaDarkColorScheme else GaujaLightColorScheme,
        typography = GaujaTypography,
        shapes = GaujaShapes,
        content = content,
    )
}
