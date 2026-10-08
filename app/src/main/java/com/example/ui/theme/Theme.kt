package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = ForestGreen,
    onPrimary = PureWhite,
    primaryContainer = MintContainer,
    onPrimaryContainer = ForestGreenDark,
    secondary = ForestGreenLight,
    onSecondary = PureWhite,
    background = MintBackground,
    onBackground = TextDark,
    surface = PureWhite,
    onSurface = TextDark,
    surfaceVariant = MintSurface,
    onSurfaceVariant = TextMuted,
    outline = LightBorder
)

@Composable
fun CVMakerTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}
