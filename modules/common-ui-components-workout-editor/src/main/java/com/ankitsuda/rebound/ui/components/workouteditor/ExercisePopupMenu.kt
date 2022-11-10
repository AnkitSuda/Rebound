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

package com.ankitsuda.rebound.ui.components.workouteditor

import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource


@Composable
fun ExercisePopupMenu(
    modifier: Modifier = Modifier,
    isExpanded: Boolean,
    isInSuperset: Boolean,
    onDismissRequest: () -> Unit,
    onDeleteExercise: () -> Unit,
    onAddWarmUpSets: () -> Unit,
    onAddNote: () -> Unit,
    onAddToSuperset: () -> Unit,
    onRemoveFromSuperset: () -> Unit,
) {
    DropdownMenu(
        modifier = modifier,
        expanded = isExpanded,
        onDismissRequest = onDismissRequest
    ) {
        DropdownMenuItem(onClick = {
            onDismissRequest()
            onAddNote()
        }) {
            Text(stringResource(id = R.string.add_note))
        }
        DropdownMenuItem(onClick = {
            onDismissRequest()
            onAddWarmUpSets()
        }) {
            Text(stringResource(id = R.string.warm_up_sets))
        }
        DropdownMenuItem(onClick = {
            onDismissRequest()
            if (isInSuperset) {
                onRemoveFromSuperset()
            } else {
                onAddToSuperset()
            }
        }) {
            Text(stringResource(id = if (isInSuperset) R.string.remove_from_superset else R.string.add_to_superset))
        }
        DropdownMenuItem(onClick = {
            onDismissRequest()
            onDeleteExercise()
        }) {
            Text(stringResource(id = R.string.delete_exercise))
        }
    }
}