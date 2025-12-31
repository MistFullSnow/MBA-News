package com.aryan.cetreader.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun ThemeDialog(
    selectedTheme: AppTheme,
    onThemeSelected: (AppTheme) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card {
            Column {
                Text(
                    "Choose Theme",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = androidx.compose.ui.Modifier.padding(16.dp)
                )

                ThemeOption("Light", AppTheme.LIGHT, selectedTheme, onThemeSelected)
                ThemeOption("Dark", AppTheme.DARK, selectedTheme, onThemeSelected)
                ThemeOption("AMOLED", AppTheme.AMOLED, selectedTheme, onThemeSelected)
            }
        }
    }
}

@Composable
private fun ThemeOption(
    label: String,
    theme: AppTheme,
    selectedTheme: AppTheme,
    onThemeSelected: (AppTheme) -> Unit
) {
    ListItem(
        headlineContent = { Text(label) },
        leadingContent = {
            RadioButton(
                selected = theme == selectedTheme,
                onClick = { onThemeSelected(theme) }
            )
        },
        modifier = androidx.compose.ui.Modifier.clickable {
            onThemeSelected(theme)
        }
    )
}
