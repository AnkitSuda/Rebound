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

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Expand
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ankitsuda.rebound.domain.entities.TemplateWithWorkout
import com.ankitsuda.rebound.domain.entities.WorkoutTemplatesFolder
import com.ankitsuda.rebound.ui.components.TemplateItemCard
import com.ankitsuda.rebound.ui.theme.LocalThemeState

@OptIn(ExperimentalFoundationApi::class)
internal fun LazyListScope.folderSection(
    folder: WorkoutTemplatesFolder?,
    templates: List<TemplateWithWorkout>,
    isExpanded: Boolean,
    onChangeExpanded: (Boolean) -> Unit,
    onClickPlay: (templateId: String) -> Unit,
    onClickTemplate: (templateId: String) -> Unit,
    onAddTemplate: () -> Unit,
    onRenameFolder: () -> Unit,
    onDeleteFolder: () -> Unit,
) {
    folder?.let {
        item(key = "workout_templates_folder_${folder.id}") {

            var menuExpanded by remember {
                mutableStateOf(false)
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onChangeExpanded(!isExpanded) }
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                    .animateItemPlacement(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Outlined.ExpandLess else Icons.Outlined.ExpandMore,
                        contentDescription = if (isExpanded) "Collapse" else "Expand"
                    )
                }
                Text(
                    modifier = Modifier.weight(1f),
                    text = it.name, style = MaterialTheme.typography.body1,
                    color = LocalThemeState.current.onBackgroundColor
                )
                IconButton(onClick = {
                    menuExpanded = true
                }) {
                    Icon(
                        imageVector = Icons.Outlined.MoreVert,
                        contentDescription = "More"
                    )
                    FolderMenu(
                        expanded = menuExpanded,
                        onDismissRequest = {
                            menuExpanded = false
                        },
                        onAddTemplate = onAddTemplate,
                        onRename = onRenameFolder,
                        onDelete = onDeleteFolder
                    )
                }
            }
        }
    }

    if (isExpanded) {
        items(templates, key = { it.template.id }) {
            with(it) {
                TemplateItemCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                        .animateItemPlacement(),
                    name = (workout.name ?: "").ifBlank { "Unnamed Template" },
                    italicName = (workout.name ?: "").isBlank(),
                    totalExercises = exerciseWorkoutJunctions.size,
                    onClickPlay = {
                        onClickPlay(template.id)
                    },
                    onClick = {
                        onClickTemplate(template.id)
                    }
                )
            }
        }
    }
}