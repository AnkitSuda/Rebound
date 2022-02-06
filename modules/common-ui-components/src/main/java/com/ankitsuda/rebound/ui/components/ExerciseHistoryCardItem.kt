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

package com.ankitsuda.rebound.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ankitsuda.rebound.domain.entities.ExerciseLogEntry
import com.ankitsuda.rebound.domain.entities.Workout
import com.ankitsuda.rebound.ui.theme.ReboundTheme

@Composable
fun ExerciseHistoryCardItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    workout: Workout,
    entries: List<ExerciseLogEntry>
) {
    AppCard(modifier = modifier, onClick = onClick) {
        Column(modifier = Modifier.padding(16.dp)) {
            workout.name?.let {
                Text(
                    text = it, style = ReboundTheme.typography.body1
                )
                RSpacer(4.dp)
            }

           /* workout.createdAt?.let {
                Text(
                    text = it.format(
                        DateTimeFormatter.ofLocalizedDateTime(
                            FormatStyle.MEDIUM,
                            FormatStyle.SHORT
                        )
                    ), style = ReboundTheme.typography.body1,
                    color = ReboundTheme.colors.onBackground.copy(alpha = 0.50f)
                )
                if (entries.isNotEmpty())
                    RSpacer(8.dp)
            }*/

            if ((workout.name != null || workout.createdAt != null) && entries.isNotEmpty()) RSpacer(
                space = 6.dp
            )

            if (entries.isNotEmpty()) {
                for (i in entries.indices) {
                    val entry = entries[i]
                    SessionExerciseSetItem(
                        order = i,
                        set = Pair(entry.weight ?: 0f, entry.reps ?: 0)
                    )
                    if (i != entries.size - 1) {
                        RSpacer(8.dp)
                    }
                }
            }
        }
    }
}