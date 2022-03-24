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

package com.ankitsuda.rebound.ui.components.panel_tops

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ankitsuda.base.util.NONE_WORKOUT_ID
import com.ankitsuda.rebound.domain.entities.Workout

@Composable
fun PanelTopDragHandle(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .height(2.dp)
                .width(32.dp)
                .align(Alignment.Center)
                .background(Color(245, 245, 245))
                .clip(RoundedCornerShape(50))
        )
    }
}

@Composable
fun PanelTopCollapsed(
    currentTimeStr: String,
    onTimePlay: () -> Unit,
    onTimePause: () -> Unit,
    onTimeReset: () -> Unit,
    viewModel: PanelTopsViewModel = hiltViewModel()
) {
    val workoutId by viewModel.currentWorkoutId.collectAsState(initial = NONE_WORKOUT_ID)
    val workout by viewModel.getWorkout(workoutId).collectAsState(initial = null)
    Column(
        modifier = Modifier
            .padding(6.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        workout?.name?.let {
            Text(text = it, style = MaterialTheme.typography.h6)
        }
        Text(text = currentTimeStr, style = MaterialTheme.typography.caption)
        Row() {
            IconButton(onClick = onTimePause) {
                Icon(imageVector = Icons.Outlined.Pause, contentDescription = null)
            }
            IconButton(onClick = onTimePlay) {
                Icon(imageVector = Icons.Outlined.PlayArrow, contentDescription = null)
            }
            IconButton(onClick = onTimeReset) {
                Icon(imageVector = Icons.Outlined.ResetTv, contentDescription = null)
            }
        }
    }
}

@Composable
fun PanelTopExpanded(
    onCollapseBtnClicked: () -> Unit,
    onTimerBtnClicked: () -> Unit,
    onFinishBtnClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(6.dp)
            .fillMaxWidth(),
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, end = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onCollapseBtnClicked) {
                Icon(
                    imageVector = Icons.Outlined.KeyboardArrowDown,
                    contentDescription = "Collapse panel"
                )
            }
            IconButton(onClick = onTimerBtnClicked) {
                Icon(
                    imageVector = Icons.Outlined.Timer,
                    contentDescription = "Collapse panel"
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = onFinishBtnClicked,
                elevation = ButtonDefaults.elevation(0.dp, 0.dp, 0.dp)
            ) {
                Text(text = "Finish", style = MaterialTheme.typography.button)
            }
        }
    }
}