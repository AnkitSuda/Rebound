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

package com.ankitsuda.rebound.ui.exercise_details.about

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ankitsuda.rebound.domain.entities.Exercise
import com.ankitsuda.rebound.ui.components.RSpacer
import com.ankitsuda.rebound.ui.theme.ReboundTheme

@Composable
fun ExerciseDetailAboutTab(exercise: Exercise) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        if (!exercise.notes.isNullOrBlank()) {
            Section(title = "Instructions", text = exercise.notes.toString())
        }

        if (!exercise.primaryMuscleTag.isNullOrBlank()) {
            Section(title = "Primary Muscle", text = exercise.primaryMuscleTag.toString())

        }

        Section(title = "Category", text = exercise.category.cName)
    }
}

@Composable
private fun ColumnScope.Section(title: String, text: String) {
    Text(
        text = title,
        style = ReboundTheme.typography.caption,
        color = ReboundTheme.colors.onBackground.copy(alpha = 0.75f)
    )
    RSpacer(space = 4.dp)
    Text(
        text = text, style = ReboundTheme.typography.body1,
        color = ReboundTheme.colors.onBackground
    )
    RSpacer(space = 16.dp)
}