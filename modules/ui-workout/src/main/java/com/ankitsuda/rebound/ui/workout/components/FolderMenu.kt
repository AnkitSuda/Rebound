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

package com.ankitsuda.rebound.ui.workout.components

import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ankitsuda.rebound.ui.workout.R

@Composable
internal fun FolderMenu(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    isForUnorganized: Boolean,
    onDismissRequest: () -> Unit,
    onAddTemplate: () -> Unit,
    onRename: () -> Unit,
    onDelete: () -> Unit,
) {

    DropdownMenu(
        modifier = modifier,
        expanded = expanded,
        onDismissRequest = onDismissRequest
    ) {
        DropdownMenuItem(onClick = {
            onDismissRequest()
            onAddTemplate()
        }) {
            Text(stringResource(id = R.string.add_template))
        }
        if (!isForUnorganized) {
            DropdownMenuItem(onClick = {
                onDismissRequest()
                onRename()
            }) {
                Text(stringResource(id = R.string.rename))
            }
            DropdownMenuItem(onClick = {
                onDismissRequest()
                onDelete()
            }) {
                Text(stringResource(id = R.string.delete))
            }
        }
    }
}