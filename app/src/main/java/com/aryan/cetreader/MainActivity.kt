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
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {

            val systemUiController = rememberSystemUiController()
            val useDarkIcons = currentTheme == AppTheme.LIGHT
            
            SideEffect {
                systemUiController.isStatusBarVisible = false
                systemUiController.isNavigationBarVisible = false
                systemUiController.setSystemBarsColor(
                    color = Color.Transparent,
                    darkIcons = useDarkIcons
                )
            }


            var currentTheme by remember { mutableStateOf(AppTheme.LIGHT) }

            LaunchedEffect(Unit) {
                ThemePreferences.getTheme(this@MainActivity)
                    .collect { currentTheme = it }
            }

            CETReaderTheme(theme = currentTheme) {
                HomeScreen(
                    currentTheme = currentTheme,
                    onThemeChange = { theme ->
                        currentTheme = theme
                        lifecycleScope.launch {
                            ThemePreferences.saveTheme(this@MainActivity, theme)
                        }
                    }
                )
            }
        }
    }
}
