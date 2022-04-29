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

package com.ankitsuda.rebound.ui.exercise_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ankitsuda.navigation.EXERCISE_ID_KEY
import com.ankitsuda.rebound.data.repositories.ExercisesRepository
import com.ankitsuda.rebound.domain.entities.LogEntriesWithWorkout
import com.ankitsuda.rebound.domain.entities.calculateTotalVolume
import com.ankitsuda.rebound.ui.components.charts.line.LineChartData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class ExerciseDetailScreenViewModel @Inject constructor(
    private val exercisesRepository: ExercisesRepository,
    handle: SavedStateHandle,
) : ViewModel() {
    private val exerciseId = requireNotNull(handle.get<String>(EXERCISE_ID_KEY))

    val exercise = exercisesRepository.getExercise(exerciseId)

    val history = exercisesRepository.getVisibleLogEntries(exerciseId).map {
        refreshCharts(it)
        it
    }

    private var _charts = MutableStateFlow<Map<String, List<LineChartData.Point>>>(emptyMap())
    val charts = _charts.asStateFlow()

    private fun refreshCharts(entriesWithWorkout: List<LogEntriesWithWorkout>) {
        viewModelScope.launch {
            val maxReps = arrayListOf<LineChartData.Point>()
            val maxWeights = arrayListOf<LineChartData.Point>()
            val totalVolumes = arrayListOf<LineChartData.Point>()

            val entriesGroup = entriesWithWorkout.groupBy { it.workout.createdAt?.toLocalDate() }

            for (group in entriesGroup) {
                val label = group.key?.format(DateTimeFormatter.ofPattern("MMM d")) ?: ""
                val entries = group.value.flatMap { it.logEntries }

                maxReps.add(
                    LineChartData.Point(
                        value = (entries.maxOfOrNull { it.reps ?: 0 }
                            ?: 0).toFloat(),
                        label = label
                    )
                )

                maxWeights.add(
                    LineChartData.Point(
                        value = entries.maxOfOrNull { it.weight ?: 0f } ?: 0f,
                        label = label
                    )
                )

                totalVolumes.add(
                    LineChartData.Point(
                        value = entries.calculateTotalVolume(),
                        label = label
                    )
                )
            }

            val newMap = mapOf(
                Pair("Heaviest weight", maxWeights),
                Pair("Total volume", totalVolumes),
                Pair("Max reps", maxReps)
            )

            Timber.d("charts $newMap")

            _charts.value = newMap
        }
    }
}