package com.aryan.cetreader.ui.theme

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("theme_prefs")

object ThemePreferences {

    private val THEME_KEY = stringPreferencesKey("app_theme")

    fun getTheme(context: Context): Flow<AppTheme> =
        context.dataStore.data.map { prefs ->
            when (prefs[THEME_KEY]) {
                AppTheme.DARK.name -> AppTheme.DARK
                AppTheme.AMOLED.name -> AppTheme.AMOLED
                else -> AppTheme.LIGHT
            }
        }

    suspend fun saveTheme(context: Context, theme: AppTheme) {
        context.dataStore.edit { prefs ->
            prefs[THEME_KEY] = theme.name
        }
    }
}
