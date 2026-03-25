package com.proxy.tool.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryBlue,
    secondary = PrimaryBlueDark,
    tertiary = SecondaryGray,
    background = DarkBackground,
    surface = DarkSurface,
    surfaceVariant = DarkSurfaceVariant,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = TextPrimary,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    error = ErrorRed,
    outline = BorderColor
)

val LocalSpacing = staticCompositionLocalOf { Spacing() }

data class Spacing(
    val tiny: Int = 4,
    val small: Int = 8,
    val medium: Int = 16,
    val large: Int = 24,
    val extraLarge: Int = 32
)

@Composable
fun AndroBurpTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme
    
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(),
        content = {
            CompositionLocalProvider(LocalSpacing provides Spacing()) {
                content()
            }
        }
    )
}