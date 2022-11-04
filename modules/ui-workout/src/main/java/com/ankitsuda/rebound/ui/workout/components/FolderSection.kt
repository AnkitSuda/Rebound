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
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.ankitsuda.rebound.domain.entities.TemplateWithWorkout
import com.ankitsuda.rebound.domain.entities.WorkoutTemplatesFolder
import com.ankitsuda.rebound.ui.components.RButtonStyle2
import com.ankitsuda.rebound.ui.components.RDashedButton
import com.ankitsuda.rebound.ui.components.RTextButton
import com.ankitsuda.rebound.ui.components.TemplateItemCard
import com.ankitsuda.rebound.ui.components.dragdrop.DragDropListState
import com.ankitsuda.rebound.ui.components.modifiers.dashedBorder
import com.ankitsuda.rebound.ui.theme.LocalThemeState
import com.ankitsuda.rebound.ui.theme.ReboundTheme
import com.ankitsuda.rebound.ui.workout.UNORGANIZED_FOLDERS_ID

@OptIn(ExperimentalFoundationApi::class)
@Composable()
internal fun LazyItemScope.FolderSection(
    index: Int,
    folder: WorkoutTemplatesFolder?,
    isExpanded: Boolean,
    dragDropListState: DragDropListState,
    onChangeExpanded: (Boolean) -> Unit,
    onRenameFolder: () -> Unit,
    onDeleteFolder: () -> Unit,
    onAddTemplate: () -> Unit,
) {
    var menuExpanded by remember {
        mutableStateOf(false)
    }

    folder?.let {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clickable { onChangeExpanded(!isExpanded) }
                .padding(start = 16.dp, end = 16.dp)
                .composed {
                    val offsetOrNull =
                        dragDropListState.elementDisplacement.takeIf {
                            index == dragDropListState.currentIndexOfDraggedItem
                        }

                    Modifier
                        .graphicsLayer {
                            translationY = offsetOrNull ?: 0f
                        }
                }
                .animateItemPlacement(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (isExpanded) Icons.Outlined.ExpandLess else Icons.Outlined.ExpandMore,
                contentDescription = if (isExpanded) "Collapse" else "Expand"
            )
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
                    isForUnorganized = it.id == UNORGANIZED_FOLDERS_ID,
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

@OptIn(ExperimentalFoundationApi::class)
internal fun LazyListScope.folderSection(
    folder: WorkoutTemplatesFolder?,
    templates: List<TemplateWithWorkout>,
    isExpanded: Boolean,
    onClickPlay: (templateId: String) -> Unit,
    onClickTemplate: (templateId: String) -> Unit,
    onAddTemplate: () -> Unit,
) {
    if (isExpanded) {
        if (templates.isEmpty() && folder?.id != null) {
            item(key = "add_template_${folder.id}") {
                RDashedButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .animateItemPlacement(),
                    text = "Add Template",
                    icon = Icons.Outlined.Add,
                    onClick = onAddTemplate
                )
            }
        } else {
            items(templates, key = { it.template.id }) {
                with(it) {
                    TemplateItemCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
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
}