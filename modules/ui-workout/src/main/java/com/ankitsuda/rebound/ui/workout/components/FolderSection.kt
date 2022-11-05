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

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.ankitsuda.rebound.domain.entities.TemplateWithWorkout
import com.ankitsuda.rebound.domain.entities.WorkoutTemplatesFolder
import com.ankitsuda.rebound.ui.components.RDashedButton
import com.ankitsuda.rebound.ui.components.TemplateItemCard
import com.ankitsuda.rebound.ui.components.dragdrop.DragDropListState
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
        val isSelectedForDrag = folder.id == dragDropListState.initiallyDraggedElement?.key
        val offsetY by animateFloatAsState(targetValue =
        dragDropListState.elementDisplacement.takeIf {
            isSelectedForDrag
        } ?: 0f)

        val iconScaleY by animateFloatAsState(targetValue = if (isExpanded) 1f else -1f)

        val elevation by animateDpAsState(targetValue = if (isSelectedForDrag) 2.dp else 0.dp)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clickable { onChangeExpanded(!isExpanded) }
                .zIndex(if (isSelectedForDrag) 10f else 0f)
                .animateItemPlacement(),
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .offset { IntOffset(0, offsetY.toInt()) },
                elevation = elevation,
                color = ReboundTheme.colors.background
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(start = 16.dp, end = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.scale(1f, iconScaleY),
                        imageVector = Icons.Outlined.ExpandLess,// else Icons.Outlined.ExpandMore,
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
    }
}