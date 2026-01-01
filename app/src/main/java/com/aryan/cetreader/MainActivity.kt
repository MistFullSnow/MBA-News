package com.aryan.cetreader

import com.aryan.cetreader.ui.NewsSection
import com.aryan.cetreader.ui.SectionScreen
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
        
            var selectedSection: NewsSection? by remember {
                mutableStateOf(null)
            }
        
            LaunchedEffect(Unit) {
                ThemePreferences.getTheme(this@MainActivity)
                    .collect { currentTheme = it }
            }
        
            CETReaderTheme(theme = currentTheme) {
        
                if (selectedSection == null) {
                    SectionScreen(
                        onSectionSelected = { section ->
                            selectedSection = section
                        }
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
