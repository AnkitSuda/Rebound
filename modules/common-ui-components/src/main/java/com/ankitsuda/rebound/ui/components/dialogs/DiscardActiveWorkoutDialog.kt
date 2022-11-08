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

package com.ankitsuda.rebound.ui.components.dialogs

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ankitsuda.common.compose.R

@Composable
fun DiscardActiveWorkoutDialog(
    modifier: Modifier = Modifier,
    onClickDiscard: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    AlertDialog(
        modifier = modifier,
        title = { Text(stringResource(id = R.string.workout_in_progress)) },
        text = { Text(stringResource(id = R.string.workout_in_progress_description)) },
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = onClickDiscard) {
                Text(stringResource(id = R.string.discard))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = stringResource(id = R.string.cancel))
            }
        })
}