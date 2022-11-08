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

package com.ankitsuda.rebound.ui.resttimer.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import kotlin.math.max
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Pause
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ankitsuda.rebound.resttimer.TimerState
import com.ankitsuda.rebound.ui.components.RSpacer
import com.ankitsuda.rebound.ui.resttimer.R
import com.ankitsuda.rebound.ui.theme.LocalThemeState
import kotlin.math.min

/**
 * Circle timer
 * Thanks to https://github.com/p-hlp/InTimeAndroid/blob/master/app/src/main/java/com/example/intimesimple/ui/composables/TimerCircleComponent.kt
 */
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TimerCircleComponent(
    modifier: Modifier = Modifier,
    screenWidthDp: Int,
    screenHeightDp: Int,
    time: String,
    timerState: TimerState,
    elapsedTime: Long,
    totalTime: Long,
    onClickCancel: () -> Unit,
    onClickPause: () -> Unit,
    onClickResume: () -> Unit,
    onClickStart: (Long) -> Unit,
) {
    val maxRadius by remember { mutableStateOf(min(screenHeightDp, screenWidthDp)) }

    val density = LocalDensity.current

    BoxWithConstraints(
        modifier = Modifier
            .size(maxRadius.dp)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {

        Box(
            modifier = modifier
                .size(maxRadius.dp)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            AnimatedContent(targetState = timerState != TimerState.EXPIRED) { bln ->
                if (bln) {

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            key(time) {
                                Text(
                                    modifier = Modifier,
                                    text = time,
                                    style = typography.h2,
                                )
                            }
                        }

                        Row(
                            modifier = Modifier
                                .padding(bottom = 28.dp)
                                .align(Alignment.BottomCenter)
                        ) {
                            if (timerState == TimerState.RUNNING) {
                                IconButton(
                                    onClick = onClickPause
                                ) {
                                    Icon(
                                        modifier = Modifier.size(32.dp),
                                        imageVector = Icons.Outlined.Pause,
                                        contentDescription = stringResource(R.string.pause)
                                    )
                                }
                            } else if (timerState == TimerState.PAUSED) {
                                IconButton(
                                    onClick = onClickResume
                                ) {
                                    Icon(
                                        modifier = Modifier.size(32.dp),
                                        imageVector = Icons.Outlined.PlayArrow,
                                        contentDescription = stringResource(R.string.resume)
                                    )
                                }
                            }
                            RSpacer(space = 16.dp)
                            IconButton(
                                onClick = onClickCancel
                            ) {
                                Icon(
                                    modifier = Modifier.size(32.dp),
                                    imageVector = Icons.Outlined.Close,
                                    contentDescription = stringResource(R.string.cancel)
                                )
                            }
                        }
                    }

                } else {
                    TimesListComponent(
                        contentPadding = PaddingValues(vertical = with(density) { (this@BoxWithConstraints.constraints.minHeight / 3).toDp() }),
                        onClickStart = onClickStart
                    )
                }
            }
        }


        TimerCircle(
            modifier = modifier.align(Alignment.Center),
            elapsedTime = elapsedTime,
            totalTime = totalTime,
            cWidth = with(density) { constraints.minWidth.toDp() },
            cHeight = with(density) { constraints.minHeight.toDp() }
        )
    }

}

@Composable
fun TimerCircle(
    modifier: Modifier = Modifier,
    elapsedTime: Long,
    cHeight: Dp,
    cWidth: Dp,
    totalTime: Long
) {
    val completedColor = LocalThemeState.current.primaryColor
    val remainderColor = completedColor.copy(alpha = 0.25f)
    val whitePercent by animateFloatAsState(
        targetValue =
//        min(
//            1f,
        1f - (elapsedTime.toFloat() / totalTime.toFloat())
//        ),
    )

    Canvas(modifier = modifier.size(cWidth, cHeight), onDraw = {
        val height = size.height
        val width = size.width
        val dotDiameter = 12.dp
        val strokeSize = 20.dp
        val radiusOffset = calculateRadiusOffset(strokeSize.value, dotDiameter.value, 0f)

        val xCenter = width / 2f
        val yCenter = height / 2f
        val radius = min(xCenter, yCenter)
        val arcWidthHeight = ((radius - radiusOffset) * 2f)
        val arcSize = Size(arcWidthHeight, arcWidthHeight)

        val greenPercent = 1 - whitePercent

        drawArc(
            completedColor,
            270f,
            -greenPercent * 360f,
            false,
            topLeft = Offset(radiusOffset, radiusOffset),
            size = arcSize,
            style = Stroke(width = strokeSize.value)
        )

        drawArc(
            remainderColor,
            270f,
            whitePercent * 360,
            false,
            topLeft = Offset(radiusOffset, radiusOffset),
            size = arcSize,
            style = Stroke(width = strokeSize.value)
        )

    })
}

fun calculateRadiusOffset(strokeSize: Float, dotStrokeSize: Float, markerStrokeSize: Float)
        : Float {
    return max(strokeSize, max(dotStrokeSize, markerStrokeSize))
}