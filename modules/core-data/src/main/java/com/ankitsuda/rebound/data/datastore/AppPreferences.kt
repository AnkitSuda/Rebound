package com.ankitsuda.rebound.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.*
import com.ankitsuda.base.ui.ThemeState
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton
import com.ankitsuda.data.DatastoreUtils
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import timber.log.Timber

@Singleton
class AppPreferences @Inject constructor(@ApplicationContext private val context: Context) :
    PrefStorage {
    private val datastoreUtils: DatastoreUtils = DatastoreUtils(context)

    companion object {
        val THEME_STATE_KEY = stringPreferencesKey(name = "theme_state")


        // Current workout id
        val CURRENT_WORKOUT_ID_KEY = longPreferencesKey(name = "current_workout_id")
    }

    override val themeState: Flow<ThemeState>
        get() = flow {
            getValue(THEME_STATE_KEY, Json.encodeToString(ThemeState())).mapLatest {
                val deserializedString = Json.decodeFromString<ThemeState>(it)

                Timber.i("deserializedString $deserializedString")


                emit(deserializedString)
            }
        }


    override suspend fun setThemeState(value: ThemeState) {
        val serializedString = Json.encodeToString(value)

        Timber.i("serializedString $serializedString")

        setValue(THEME_STATE_KEY, serializedString)
    }


    override val currentWorkoutId: Flow<Long>
        get() = getValue(CURRENT_WORKOUT_ID_KEY, -1)

    override suspend fun setCurrentWorkoutId(value: Long) {
        setValue(CURRENT_WORKOUT_ID_KEY, value)
    }
    // SMALL SHAPE ENDS

    override suspend fun clearPreferenceStorage() {
        datastoreUtils.clearPreferenceStorage()
    }


    private suspend fun <T> setValue(
        key: Preferences.Key<T>,
        value: T
    ) {
        datastoreUtils.setValue(key, value)
    }

    private fun <T> getValue(
        key: Preferences.Key<T>,
        defaultValue: T
    ): Flow<T> =
        datastoreUtils.getValue(key, defaultValue)

//    private fun getColor(
//        key: Preferences.Key<String>,
//        defaultColor: Color
//    ) = datastoreUtils.getColor(key, defaultColor)
//
//
//    private suspend fun setColor(
//        key: Preferences.Key<String>,
//        color: Color
//    ) {
//        datastoreUtils.setColor(key, color)
//    }
}