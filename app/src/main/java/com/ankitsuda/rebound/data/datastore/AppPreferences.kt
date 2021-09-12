package com.ankitsuda.rebound.data.datastore

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.preferencesDataStoreFile
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppPreferences @Inject constructor(@ApplicationContext private val context: Context) :
    PrefStorage {
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "AppPrefStorage")


    companion object {
        val PRIMARY_COLOR_KEY = intPreferencesKey(name = "primary_color")
        val CARD_COLOR_KEY = intPreferencesKey(name = "card_color")
        val CARD_BORDER_ENABLED_KEY = booleanPreferencesKey(name = "card_border_enabled")
    }

    suspend fun setColor(key: Preferences.Key<Int>, color: Color) {
        context.dataStore.edit { preferences ->
            preferences[key] = color.toArgb()
        }
    }

    override val cardColor: Flow<Color>
        get() = context.dataStore.data
            .map { preferences ->
                if (preferences[CARD_COLOR_KEY] != null) {
                    Color(preferences[CARD_COLOR_KEY]!!)
                } else {
                    Color(248, 248, 248)
                }
            }

    override suspend fun setCardColor(color: Color) {
        setColor(CARD_COLOR_KEY, color)
    }

    override val cardBorderEnabled: Flow<Boolean>
        get() = context.dataStore.getValueAsFlow(CARD_BORDER_ENABLED_KEY, false)

    override suspend fun setCardBorderEnabled(enabled: Boolean) {
        context.dataStore.setValue(CARD_BORDER_ENABLED_KEY, enabled)
    }


    override suspend fun clearPreferenceStorage() {
        context.dataStore.edit {
            it.clear()
        }
    }


    /***
     * handy function to save key-value pairs in Preference. Sets or updates the value in Preference
     * @param key used to identify the preference
     * @param value the value to be saved in the preference
     */
    private suspend fun <T> DataStore<Preferences>.setValue(
        key: Preferences.Key<T>,
        value: T
    ) {
        this.edit { preferences ->
            // save the value in prefs
            preferences[key] = value
        }
    }

    /***
     * handy function to return Preference value based on the Preference key
     * @param key  used to identify the preference
     * @param defaultValue value in case the Preference does not exists
     * @throws Exception if there is some error in getting the value
     * @return [Flow] of [T]
     */
    private fun <T> DataStore<Preferences>.getValueAsFlow(
        key: Preferences.Key<T>,
        defaultValue: T
    ): Flow<T> {
        return this.data.catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                // we try again to store the value in the map operator
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            // return the default value if it doesn't exist in the storage
            preferences[key] ?: defaultValue
        }
    }
}