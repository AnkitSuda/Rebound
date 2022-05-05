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

package com.ankitsuda.rebound.ui.components.workouteditor.warmupcalculator

import com.ankitsuda.base.utils.generateId
import com.ankitsuda.rebound.domain.LogSetType
import com.ankitsuda.rebound.domain.entities.ExerciseLog
import com.ankitsuda.rebound.domain.entities.ExerciseLogEntry
import com.ankitsuda.rebound.ui.keyboard.picker.getAllWarmUpWeights

data class WarmUpSet(
    var id: String,
    var weight: Double? = null,
    var reps: Int? = null,
    var weightPercentage: Int? = null,
    val formula: String? = null
) {
    fun findFormula(): String {
        return formula ?: "Bar x 1"
    }

//    companion object {
//        fun fromLogEntries(workSetWeight: Double?, entries: List<ExerciseLogEntry>): List<WarmUpSet> {
//            val warmUpSets = arrayListOf<WarmUpSet>()
//            val filteredList = entries.filter { it.setType == LogSetType.WARM_UP }
//            for (entry in filteredList) {
//            }
//        }
//    }
}

fun WarmUpSet.toExerciseLogEntry() = ExerciseLogEntry(
    entryId = generateId(),
    weight = weight,
    reps = reps,
    setType = LogSetType.WARM_UP,
)

fun List<WarmUpSet>.toExerciseLogEntries(): List<ExerciseLogEntry> =
    this.map { it.toExerciseLogEntry()
    }