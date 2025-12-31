package com.aryan.cetreader.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

enum class AppTheme {
    LIGHT, DARK, AMOLED
}

@Composable
fun CETReaderTheme(
    theme: AppTheme,
    content: @Composable () -> Unit
) {
    val colorScheme = when (theme) {
        AppTheme.LIGHT -> lightColorScheme(
            background = LightBg,
            surface = LightCard,
            onSurface = LightText,
            onBackground = LightText,
            primary = Color(0xFF2563EB)
        )

        AppTheme.DARK -> darkColorScheme(
            background = DarkBg,
            surface = DarkCard,
            onSurface = DarkText,
            onBackground = DarkText,
            primary = Color(0xFF38BDF8)
        )

        AppTheme.AMOLED -> darkColorScheme(
            background = AmoledBg,
            surface = AmoledCard,
            onSurface = AmoledText,
            onBackground = AmoledText,
            primary = Color(0xFF22D3EE)
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
