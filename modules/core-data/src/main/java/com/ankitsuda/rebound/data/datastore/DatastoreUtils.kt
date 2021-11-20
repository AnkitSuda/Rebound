package com.ankitsuda.rebound.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.ankitsuda.domain.models.DEFAULT_JSON_FORMAT
import com.ankitsuda.domain.models.None
import com.ankitsuda.domain.models.Optional
import com.ankitsuda.domain.models.some
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.IOException
import javax.inject.Inject
private val format = DEFAULT_JSON_FORMAT
class DatastoreUtils @Inject constructor(@ApplicationContext private val context: Context) {
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = "AppPrefStorage",
    )

    suspend fun clearPreferenceStorage() {
        context.dataStore.edit {
            it.clear()
        }
    }

    suspend fun <T> setValue(
        key: Preferences.Key<T>,
        value: T
    ) {
        context.dataStore.setValue(key, value)
    }

    fun <T> getValue(
        key: Preferences.Key<T>,
        defaultValue: T
    ): Flow<T> =
        context.dataStore.getValueAsFlow(key, defaultValue)


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
    fun <T> DataStore<Preferences>.getValueAsFlow(
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

    fun <T> optional(key: Preferences.Key<T>): Flow<Optional<T>> =
        context.dataStore.data.map { preferences -> some(preferences[key]) }

    suspend fun <T> setValue(key: Preferences.Key<String>, value: T, serializer: KSerializer<T>) {
        setValue(key, Json.encodeToString(serializer, value))
    }

    fun <T> optional(key: Preferences.Key<String>, serializer: KSerializer<T>): Flow<Optional<T>> {
        return optional(key).map {
            when (it) {
                is Optional.Some<String> ->
                    try {
                        some(format.decodeFromString(serializer, it.value))
                    } catch (e: SerializationException) {
                        None
                    }
                else -> Optional.None
            }
        }
    }

    fun <T> getValue(
        name: Preferences.Key<String>,
        serializer: KSerializer<T>,
        defaultValue: T
    ): Flow<T> {
        return optional(name, serializer).map {
            when (it) {
                is Optional.None -> defaultValue
                else -> it.value()
            }
        }
    }
//    suspend fun setColor(
//        key: Preferences.Key<String>,
//        color: Color
//    ) {
//        setValue(key, color.toHexString())
//    }
//
//    fun getColor(
//        key: Preferences.Key<String>,
//        defaultColor: Color
//    ) = context.dataStore.data
//        .map { preferences ->
//            if (preferences[key] != null) {
//                val colorStr = preferences[key]!!
//                Color(
//                    android.graphics.Color.parseColor(
//                        if (colorStr.startsWith("#"))
//                            colorStr
//                        else
//                            "#$colorStr"
//                    )
//                )
//            } else {
//                defaultColor
//            }
//        }

}