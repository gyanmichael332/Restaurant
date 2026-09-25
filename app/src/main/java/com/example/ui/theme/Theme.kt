package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = SavoriaBurgundyDark,
    onPrimary = Color(0xFF560020),
    primaryContainer = Color(0xFF70132B),
    onPrimaryContainer = SavoriaBurgundyLight,
    secondary = SavoriaGoldDark,
    onSecondary = Color(0xFF472A00),
    secondaryContainer = Color(0xFF653E00),
    onSecondaryContainer = SavoriaGoldLight,
    tertiary = SavoriaOliveDark,
    onTertiary = Color(0xFF04381C),
    background = SavoriaBackgroundDark,
    onBackground = SavoriaTextPrimaryDark,
    surface = SavoriaSurfaceDark,
    onSurface = SavoriaTextPrimaryDark,
    surfaceVariant = SavoriaSurfaceVariantDark,
    onSurfaceVariant = SavoriaTextSecondaryDark,
    outline = SavoriaOutlineDark
)

private val LightColorScheme = lightColorScheme(
    primary = SavoriaBurgundy,
    onPrimary = Color.White,
    primaryContainer = SavoriaBurgundyLight,
    onPrimaryContainer = Color(0xFF3F0014),
    secondary = SavoriaGold,
    onSecondary = Color.White,
    secondaryContainer = SavoriaGoldLight,
    onSecondaryContainer = Color(0xFF2C1700),
    tertiary = SavoriaOlive,
    onTertiary = Color.White,
    background = SavoriaBackgroundLight,
    onBackground = SavoriaTextPrimaryLight,
    surface = SavoriaSurfaceLight,
    onSurface = SavoriaTextPrimaryLight,
    surfaceVariant = SavoriaSurfaceVariantLight,
    onSurfaceVariant = SavoriaTextSecondaryLight,
    outline = SavoriaOutlineLight
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep bespoke bistro identity consistent
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
