package com.example.crimsoneyes.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = CrimsonPrimary,
    onPrimary = CrimsonOnPrimary,
    primaryContainer = CrimsonDark,
    onPrimaryContainer = CrimsonLight,

    secondary = BloodRed,
    onSecondary = CrimsonOnPrimary,
    secondaryContainer = BurgundyRed,
    onSecondaryContainer = CrimsonLight,

    tertiary = WineRed,
    onTertiary = CrimsonOnPrimary,

    background = CrimsonBackground,
    onBackground = CrimsonOnBackground,

    surface = CrimsonSurface,
    onSurface = CrimsonOnSurface,
    surfaceVariant = CrimsonSurfaceVariant,
    onSurfaceVariant = CrimsonOnSurface,

    error = CrimsonError,
    onError = CrimsonOnPrimary,

    outline = CrimsonLight,
    outlineVariant = WineRed,
)

private val LightColorScheme = lightColorScheme(
    primary = CrimsonDark,
    onPrimary = CrimsonOnPrimary,
    primaryContainer = CrimsonLight,
    onPrimaryContainer = CrimsonDark,

    secondary = BurgundyRed,
    onSecondary = CrimsonOnPrimary,
    secondaryContainer = CrimsonLight,
    onSecondaryContainer = CrimsonDark,

    tertiary = WineRed,
    onTertiary = CrimsonOnPrimary,

    background = CrimsonOnPrimary,
    onBackground = CrimsonBackground,

    surface = CrimsonOnSurface,
    onSurface = CrimsonBackground,
    surfaceVariant = CrimsonLight,
    onSurfaceVariant = CrimsonDark,

    error = CrimsonError,
    onError = CrimsonOnPrimary,

    outline = CrimsonDark,
    outlineVariant = CrimsonPrimary,
)

@Composable
fun CrimsonEyesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        content = content
    )
}