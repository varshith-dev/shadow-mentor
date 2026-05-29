package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = ShadowPrimary,
    onPrimary = ShadowOnPrimary,
    primaryContainer = ShadowPrimaryContainer,
    secondary = ShadowSecondary,
    secondaryContainer = ShadowSecondaryContainer,
    background = ShadowOnSurface, // dark backdrop
    surface = ShadowOnSurfaceVariant,
    onBackground = ShadowBackground,
    onSurface = ShadowBackground
)

private val LightColorScheme = lightColorScheme(
    primary = ShadowPrimary,
    onPrimary = ShadowOnPrimary,
    primaryContainer = ShadowPrimaryContainer,
    secondary = ShadowSecondary,
    secondaryContainer = ShadowSecondaryContainer,
    onSecondaryContainer = ShadowOnSecondaryContainer,
    background = ShadowBackground,
    surface = ShadowSurface,
    onBackground = ShadowOnSurface,
    onSurface = ShadowOnSurface,
    onSurfaceVariant = ShadowOnSurfaceVariant,
    outline = ShadowOutline,
    outlineVariant = ShadowOutlineVariant
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Keep dynamic color disabled to enforce our highly branded aesthetic
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
