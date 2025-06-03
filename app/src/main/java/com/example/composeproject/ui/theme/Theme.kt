package com.example.composeproject.ui.theme

import android.app.Activity
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

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF1976D2),       // Action bar, buttons, FABs
    secondary = Color(0xFF64B5F6),     // Chips, tabs, secondary buttons
    tertiary = Color(0xFF81C784),      // Progress bars, highlights, stats

    background = Color(0xFFF5F9FF),    // Whole screen background
    surface = Color(0xFFFFFFFF),       // Cards, surfaces

    onPrimary = Color.White,           // Text/icon on primary
    onSecondary = Color.White,         // Text/icon on secondary
    onTertiary = Color.Black,          // Text/icon on tertiary

    onBackground = Color(0xFF121212),  // General text
    onSurface = Color(0xFF121212)      // Card text, content
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF90CAF9),       // Action bar, buttons, FABs
    secondary = Color(0xFF42A5F5),     // Chips, tabs, secondary buttons
    tertiary = Color(0xFF66BB6A),      // Progress bars, highlights, stats

    background = Color(0xFF121212),    // Whole screen background
    surface = Color(0xFF1E1E1E),       // Cards, surfaces

    onPrimary = Color.Black,           // Text/icon on primary
    onSecondary = Color.Black,         // Text/icon on secondary
    onTertiary = Color.White,          // Text/icon on tertiary

    onBackground = Color(0xFFE0E0E0),  // General text
    onSurface = Color(0xFFE0E0E0)      // Card text, content
)


@Composable
fun ComposeProjectTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
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

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}