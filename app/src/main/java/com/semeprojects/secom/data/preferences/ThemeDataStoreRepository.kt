package com.semeprojects.secom.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

val Context.themeDataStore: DataStore<Preferences> by preferencesDataStore(name = "theme_preferences")

enum class Theme {
    LIGHT,
    DARK,
    SYSTEM
}

class ThemeDataStoreRepository(context: Context) {

    private object PreferencesKey {
        val themeKey = stringPreferencesKey(name = "selected_theme")
    }

    private val dataStore = context.themeDataStore

    suspend fun saveTheme(theme: Theme) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.themeKey] = theme.name
        }
    }

    fun readTheme(): Flow<Theme> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                val themeName = preferences[PreferencesKey.themeKey] ?: Theme.SYSTEM.name
                Theme.valueOf(themeName)
            }
    }
}