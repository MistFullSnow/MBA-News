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
        
            LaunchedEffect(Unit) {
                ThemePreferences.getTheme(this@MainActivity)
                    .collect { currentTheme = it }
            }
        
            CETReaderTheme(theme = currentTheme) {
        
                val navController = rememberNavController()
        
                NavHost(
                    navController = navController,
                    startDestination = "sections"
                ) {
        
                    composable("sections") {
                        SectionScreen { section ->
                            navController.navigate("news/${section.name}")
                        }
                    }
        
                    composable("news/{section}") { backStack ->
                        val section = NewsSection.valueOf(
                            backStack.arguments?.getString("section")!!
                        )
        
                        HomeScreen(
                            section = section,
                            onBack = { navController.popBackStack() },
                            currentTheme = currentTheme,
                            onThemeChange = { theme ->
                                currentTheme = theme
                                lifecycleScope.launch {
                                    ThemePreferences.saveTheme(
                                        this@MainActivity,
                                        theme
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }



    }
}
