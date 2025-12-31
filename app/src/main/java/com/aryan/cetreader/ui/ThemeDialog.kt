package com.aryan.cetreader.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.aryan.cetreader.ui.theme.AppTheme

@Composable
fun ThemeDialog(
    selectedTheme: AppTheme,
    onThemeSelected: (AppTheme) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.medium
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                Text("Choose Theme", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(12.dp))

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
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onThemeSelected(theme) }
            .padding(12.dp)
    ) {
        RadioButton(
            selected = theme == selectedTheme,
            onClick = { onThemeSelected(theme) }
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(label)
    }
}
