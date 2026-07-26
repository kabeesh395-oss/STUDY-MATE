package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView

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

@Composable
fun StudyMateTheme(
    darkTheme: Boolean = true, // Force premium dark theme by default
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = BgDark.toArgb()
            window.navigationBarColor = BgDark.toArgb()
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
