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

import android.widget.ProgressBar
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ankitsuda.base.util.NONE_WORKOUT_ID
import com.ankitsuda.rebound.domain.entities.Workout
import com.ankitsuda.rebound.ui.theme.LocalThemeState
import com.ankitsuda.rebound.ui.theme.ReboundTheme
import timber.log.Timber

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
    viewModel: PanelTopsViewModel = hiltViewModel()
) {
    val workoutId by viewModel.currentWorkoutId.collectAsState(initial = NONE_WORKOUT_ID)
    val workout by viewModel.getWorkout(workoutId).collectAsState(initial = null)
    Column(
        modifier = Modifier
            .padding(6.dp)
            .fillMaxWidth()
            .background(
                color = LocalThemeState.current.backgroundColor
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        workout?.name?.let {
            Text(
                text = it, style = MaterialTheme.typography.h6,
                color = LocalThemeState.current.onBackgroundColor
            )
        }
        Text(
            text = currentTimeStr, style = MaterialTheme.typography.caption,
            color = LocalThemeState.current.onBackgroundColor.copy(alpha = 0.7f)
        )
    }
}

@Composable
fun PanelTopExpanded(
    restTimerElapsedTime: Long?,
    restTimerTotalTime: Long?,
    restTimerTimeString: String?,
    isTimerRunning: Boolean,
    onCollapseBtnClicked: () -> Unit,
    onTimerBtnClicked: () -> Unit,
    onFinishBtnClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(6.dp)
            .fillMaxWidth()
            .background(
                color = LocalThemeState.current.backgroundColor
            ),
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
            RestTimerButton(
                restTimerElapsedTime = restTimerElapsedTime,
                restTimerTotalTime = restTimerTotalTime,
                onTimerBtnClicked = onTimerBtnClicked,
                isTimerRunning = isTimerRunning,
                restTimerTimeString = restTimerTimeString
            )
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

@Composable
private fun RestTimerButton(
    restTimerElapsedTime: Long?,
    restTimerTotalTime: Long?,
    isTimerRunning: Boolean,
    restTimerTimeString: String?,
    onTimerBtnClicked: () -> Unit,
) {
    val primaryColor = LocalThemeState.current.primaryColor
    val contentColor by animateColorAsState(
        targetValue = if (isTimerRunning)
            LocalThemeState.current.onPrimaryColor
        else
            LocalThemeState.current.onBackgroundColor
    )

    var parentModifier = Modifier
        .width(100.dp)
        .clip(ReboundTheme.shapes.small)


    var iconModifier = Modifier
        .padding(8.dp)

    if (isTimerRunning) {
        parentModifier = parentModifier.clickable(onClick = onTimerBtnClicked)
    } else {
        iconModifier = iconModifier.clickable(onClick = onTimerBtnClicked)
    }

    Box(
        modifier = parentModifier
    ) {

        AnimatedVisibility(
            modifier = Modifier
                .matchParentSize()
                .width(100.dp),
            enter = slideInHorizontally(
                initialOffsetX = { -it })/* + fadeIn()*/,
            exit = slideOutHorizontally(
                targetOffsetX = { -it })/* + fadeOut()*/,
            visible = restTimerElapsedTime != null && restTimerTotalTime != null && isTimerRunning
        ) {
            LinearProgressIndicator(
                modifier = Modifier
                    .matchParentSize()
                    .width(100.dp),
                progress = (restTimerElapsedTime!!.toFloat() / restTimerTotalTime!!.toFloat()),
                color = primaryColor,
                backgroundColor = primaryColor.copy(alpha = 0.5f)
            )
        }
        Box(modifier = Modifier) {
            Icon(
                modifier = iconModifier
                    .align(Alignment.CenterStart),
                imageVector = Icons.Outlined.Timer,
                contentDescription = "Rest timer",
                tint = contentColor
            )


            AnimatedVisibility(
                modifier = Modifier
                    .align(Alignment.CenterEnd),
                enter = slideInHorizontally(initialOffsetX = { -it }) + fadeIn(),
                exit = slideOutHorizontally(targetOffsetX = { -it }) + fadeOut(),
                visible = isTimerRunning && restTimerTimeString != null
            ) {
                Box(
                    modifier = Modifier
                        .width(100.dp)
                        .align(Alignment.CenterEnd)
                        .padding(end = 8.dp)
                ) {
                    Text(
                        modifier = Modifier
                            .align(Alignment.CenterEnd),
                        text = restTimerTimeString ?: "",
                        fontSize = 14.sp,
                        color = contentColor
                    )
                }
            }
        }
    }
}