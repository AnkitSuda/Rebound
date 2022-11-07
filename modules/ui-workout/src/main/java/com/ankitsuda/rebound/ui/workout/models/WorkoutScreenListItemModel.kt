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

package com.ankitsuda.rebound.ui.workout.models

import com.ankitsuda.rebound.domain.entities.TemplateWithWorkout
import com.ankitsuda.rebound.domain.entities.WorkoutTemplatesFolder
import kotlinx.coroutines.flow.Flow

sealed class WorkoutScreenListItemModel

data class WorkoutScreenListItemOngoingWorkoutModel(
    val durationStrFlow: Flow<String?>
) : WorkoutScreenListItemModel()

object WorkoutScreenListItemHeaderModel : WorkoutScreenListItemModel()

data class WorkoutScreenListItemFolderHeaderModel(
    val folder: WorkoutTemplatesFolder
) : WorkoutScreenListItemModel()

data class WorkoutScreenListItemAddTemplateModel(
    val folderId: String
) : WorkoutScreenListItemModel()

data class WorkoutScreenListItemTemplateModel(
    val templateWithWorkout: TemplateWithWorkout
) : WorkoutScreenListItemModel()