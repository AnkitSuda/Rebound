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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ankitsuda.rebound.ui.theme.LocalThemeState

@Composable
fun RoutineItemCard(
    modifier: Modifier = Modifier,
    name: String,
    date: String,
    totalExercises: Int,
    onClick: () -> Unit
) {
    AppCard(modifier = modifier, onClick = onClick) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = name, style = MaterialTheme.typography.body1,
                color = LocalThemeState.current.onBackgroundColor
            )
            Text(
                text = date, style = MaterialTheme.typography.body2,
                color = LocalThemeState.current.onBackgroundColor.copy(alpha = 0.7f)
            )
            Text(
                text = "$totalExercises Exercises",
                style = MaterialTheme.typography.body2,
                modifier = Modifier.padding(top = 16.dp),
                color = LocalThemeState.current.onBackgroundColor.copy(0.5f)
            )
        }
    }
}