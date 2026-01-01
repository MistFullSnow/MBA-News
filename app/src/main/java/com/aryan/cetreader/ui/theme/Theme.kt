package com.aryan.cetreader.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

enum class AppTheme {
    LIGHT, DARK, AMOLED
}

private val LightColors = lightColorScheme(
    primary = Color.Black,
    onPrimary = Color.White,
    background = Color(0xFFF9FAFB),
    surface = Color.White,
    onSurface = Color.Black
)

private val DarkColors = darkColorScheme(
    primary = Color.White,
    onPrimary = Color.Black,
    background = Color(0xFF0D1117),
    surface = Color(0xFF161B22),
    onSurface = Color.White
)

private val AmoledColors = darkColorScheme(
    primary = Color.White,
    background = Color.Black,
    surface = Color(0xFF121212), // subtle lift for cards
    onSurface = Color.White
)

@Composable
fun CETReaderTheme(
    theme: AppTheme,
    content: @Composable () -> Unit
) {
    val colorScheme = when (theme) {
        AppTheme.LIGHT -> LightColors
        AppTheme.DARK -> DarkColors
        AppTheme.AMOLED -> AmoledColors
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
