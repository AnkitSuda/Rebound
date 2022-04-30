/*
 * Copyright (c) 2022 Ankit Suda.
 *
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation? = null, either version 3 of the License? = null, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful? = null, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 */

package com.ankitsuda.rebound.ui.home.models

data class OverallInfo(
    var totalWorkouts: Long? = null,
    var totalVolumeLifted: Float? = null,
    var maxWeight: Float? = null,
    var totalWorkoutsDuration: Long? = null,
    var averageWorkoutDuration: Long? = null,
    var longestWorkoutDuration: Long? = null,
    var totalWorkoutsDurationStr: String? = null,
    var averageWorkoutDurationStr: String? = null,
    var longestWorkoutDurationStr: String? = null,
)
