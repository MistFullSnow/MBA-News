package com.aryan.cetreader

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.lifecycle.lifecycleScope
import com.aryan.cetreader.ui.HomeScreen
import com.aryan.cetreader.ui.theme.AppTheme
import com.aryan.cetreader.ui.theme.CETReaderTheme
import com.aryan.cetreader.ui.theme.ThemePreferences
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
        
            var currentTheme by remember { mutableStateOf(AppTheme.LIGHT) }
            var selectedSection by remember { mutableStateOf<NewsSection?>(null) }
        
            LaunchedEffect(Unit) {
                ThemePreferences.getTheme(this@MainActivity)
                    .collect { currentTheme = it }
            }
        
            CETReaderTheme(theme = currentTheme) {
        
                if (selectedSection == null) {
                    SectionScreen(
                        onSectionSelected = { selectedSection = it }
                    )
                } else {
                    HomeScreen(
                        currentTheme = currentTheme,
                        onThemeChange = { theme ->
                            currentTheme = theme
                            lifecycleScope.launch {
                                ThemePreferences.saveTheme(this@MainActivity, theme)
                            }
                        },
                        section = selectedSection!!,
                        onBack = { selectedSection = null }
                    )
                }
            }
        }

    }
}
