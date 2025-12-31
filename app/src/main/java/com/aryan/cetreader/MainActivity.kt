package com.aryan.cetreader

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.aryan.cetreader.ui.HomeScreen
import com.aryan.cetreader.ui.theme.AppTheme
import com.aryan.cetreader.ui.theme.CETReaderTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CETReaderTheme(theme = AppTheme.LIGHT) {
                HomeScreen()
            }
        }
    }
}
