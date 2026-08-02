package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = ElectricBlue,
    onPrimary = PrimaryText,
    primaryContainer = ElectricBlueDark,
    onPrimaryContainer = PrimaryText,
    secondary = CyberPurple,
    onSecondary = PrimaryText,
    background = BgDark,
    onBackground = PrimaryText,
    surface = SurfaceDark,
    onSurface = PrimaryText,
    surfaceVariant = CardDark,
    onSurfaceVariant = SecondaryText,
    outline = CardBorderDark,
    error = StatusDanger,
    onError = PrimaryText
)

private val LightColorScheme = lightColorScheme(
    primary = ElectricBlue,
    onPrimary = Color.White,
    primaryContainer = ElectricBlueLight,
    onPrimaryContainer = PrimaryTextLight,
    secondary = CyberPurple,
    onSecondary = Color.White,
    background = BgLight,
    onBackground = PrimaryTextLight,
    surface = SurfaceLight,
    onSurface = PrimaryTextLight,
    surfaceVariant = CardLight,
    onSurfaceVariant = SecondaryTextLight,
    outline = CardBorderLight,
    error = StatusDanger,
    onError = Color.White
)

@Composable
fun StudyMateTheme(
    themeMode: String = "DARK", // "DARK", "LIGHT", "SYSTEM"
    content: @Composable () -> Unit
) {
    val systemInDark = isSystemInDarkTheme()
    val isDark = when (themeMode) {
        "LIGHT" -> false
        "SYSTEM" -> systemInDark
        else -> true // "DARK"
    }

    val colorScheme = if (isDark) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            val bgArgb = if (isDark) BgDark.toArgb() else BgLight.toArgb()
            window.statusBarColor = bgArgb
            window.navigationBarColor = bgArgb

            val insetsController = WindowCompat.getInsetsController(window, view)
            insetsController.isAppearanceLightStatusBars = !isDark
            insetsController.isAppearanceLightNavigationBars = !isDark
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
