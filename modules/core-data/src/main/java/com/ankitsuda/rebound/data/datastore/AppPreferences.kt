/*
 * Copyright (c) 2022 Ankit Suda.
 *
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 */

package com.ankitsuda.rebound.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.*
import com.ankitsuda.base.ui.ThemeState
import com.ankitsuda.base.util.NONE_WORKOUT_ID
import com.ankitsuda.domain.models.Optional
import com.ankitsuda.rebound.domain.DistanceUnit
import com.ankitsuda.rebound.domain.DistanceUnitSerializer
import com.ankitsuda.rebound.domain.WeightUnit
import com.ankitsuda.rebound.domain.WeightUnitSerializer
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.serialization.KSerializer
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
        val CURRENT_WORKOUT_ID_KEY = stringPreferencesKey(name = "current_workout_id")
        val WEIGHT_UNIT_KEY = stringPreferencesKey(name = "weight_unit")
        val DISTANCE_UNIT_KEY = stringPreferencesKey(name = "distance_unit")
        val FIRST_DAY_OF_WEEK_KEY = intPreferencesKey(name = "first_day_of_week")
    }

    override val themeState: Flow<ThemeState>
        get() = getValue(THEME_STATE_KEY, ThemeState.serializer(), ThemeState())

    override suspend fun setThemeState(value: ThemeState) {
        setValue(THEME_STATE_KEY, value, ThemeState.serializer())

    }

    override val currentWorkoutId: Flow<String>
        get() = getValue(CURRENT_WORKOUT_ID_KEY, NONE_WORKOUT_ID)

    override suspend fun setCurrentWorkoutId(value: String) {
        setValue(CURRENT_WORKOUT_ID_KEY, value)
    }

    override val weightUnit: Flow<WeightUnit>
        get() = getValue(WEIGHT_UNIT_KEY, WeightUnitSerializer, WeightUnit.KG)

    override suspend fun setWeightUnit(value: WeightUnit) {
        setValue(WEIGHT_UNIT_KEY, value, WeightUnitSerializer)
    }

    override val distanceUnit: Flow<DistanceUnit>
        get() = getValue(DISTANCE_UNIT_KEY, DistanceUnitSerializer, DistanceUnit.KM)

    override suspend fun setDistanceUnit(value: DistanceUnit) {
        setValue(DISTANCE_UNIT_KEY, value, DistanceUnitSerializer)
    }

    override val firstDayOfWeek: Flow<Int>
        get() = getValue(FIRST_DAY_OF_WEEK_KEY, 1)

    override suspend fun setFirstDayOfWeek(value: Int) {
        setValue(FIRST_DAY_OF_WEEK_KEY, value)
    }

    override suspend fun clearPreferenceStorage() {
        datastoreUtils.clearPreferenceStorage()
    }


    private suspend fun <T> setValue(
        key: Preferences.Key<T>,
        value: T
    ) {
        datastoreUtils.setValue(key, value)
    }

    private suspend fun <T> setValue(
        key: Preferences.Key<String>,
        value: T,
        serializer: KSerializer<T>
    ) =
        datastoreUtils.setValue(key, value, serializer)

    private fun <T> getValue(
        key: Preferences.Key<T>,
        defaultValue: T
    ): Flow<T> =
        datastoreUtils.getValue(key, defaultValue)


    fun <T> getValue(
        name: Preferences.Key<String>,
        serializer: KSerializer<T>,
        defaultValue: T
    ): Flow<T> =
        datastoreUtils.getValue(name, serializer, defaultValue)

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