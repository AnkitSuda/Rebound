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

import kotlin.math.max
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import com.ankitsuda.rebound.ui.theme.LocalThemeState
import kotlin.math.min

/**
 * Circle timer
 * Thanks to https://github.com/p-hlp/InTimeAndroid/blob/master/app/src/main/java/com/example/intimesimple/ui/composables/TimerCircleComponent.kt
 */
@Composable
fun TimerCircleComponent(
    modifier: Modifier = Modifier,
    screenWidthDp: Int,
    screenHeightDp: Int,
    time: String,
    state: String,
    reps: String,
    elapsedTime: Long,
    totalTime: Long
) {
    val maxRadius by remember { mutableStateOf(min(screenHeightDp, screenWidthDp)) }

    Box(
        modifier = modifier
            .size(maxRadius.dp)
            .padding(8.dp)
    ) {

        val constraints = if (screenWidthDp.dp < 600.dp) {
            portraitConstraints()
        } else landscapeConstraints()
        ConstraintLayout(modifier = Modifier.fillMaxSize(), constraintSet = constraints) {

            Text(
                modifier = Modifier.layoutId("timerText"),
                text = time,
                style = typography.h2,
            )

            Text(
                modifier = Modifier.layoutId("workoutStateText"),
                text = state,
                style = typography.h5,
            )

            Text(
                modifier = Modifier.layoutId("repText"),
                text = reps,
                style = typography.h5,
            )
        }

        // only show in portrait mode
        if (screenWidthDp.dp < 600.dp) {
            TimerCircle(
                modifier = modifier,
                elapsedTime = elapsedTime,
                totalTime = totalTime
            )
        }
    }
}

@Composable
fun TimerCircle(
    modifier: Modifier = Modifier,
    elapsedTime: Long,
    totalTime: Long
) {
    val completedColor = LocalThemeState.current.primaryColor
    Canvas(modifier = modifier.fillMaxSize(), onDraw = {
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

        val remainderColor = Color.White.copy(alpha = 0.25f)

        val whitePercent =
            min(1f, elapsedTime.toFloat() / totalTime.toFloat())
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

@Composable
fun DebugCenterLines(
    modifier: Modifier
) {
    Canvas(modifier = modifier.fillMaxSize(), onDraw = {
        drawLine(
            color = Color.Black,
            start = Offset(size.width / 2f, 0f),
            end = Offset(size.width / 2f, size.height),
            strokeWidth = 4f
        )

        drawLine(
            color = Color.Black,
            start = Offset(0f, size.height / 2f),
            end = Offset(size.width, size.height / 2f),
            strokeWidth = 4f
        )
    })
}

private fun portraitConstraints(): ConstraintSet {
    return ConstraintSet {
        val timerText = createRefFor("timerText")
        val workoutStateText = createRefFor("workoutStateText")
        val repText = createRefFor("repText")

        constrain(timerText) {
            centerHorizontallyTo(parent)
            centerVerticallyTo(parent)
        }

        constrain(workoutStateText) {
            centerHorizontallyTo(parent)
            bottom.linkTo(timerText.top, 8.dp)
        }

        constrain(repText) {
            centerHorizontallyTo(parent)
            top.linkTo(timerText.bottom, 8.dp)
        }
    }
}

private fun landscapeConstraints(): ConstraintSet {
    return ConstraintSet {
        val timerText = createRefFor("timerText")
        val workoutStateText = createRefFor("workoutStateText")
        val repText = createRefFor("repText")

        constrain(timerText) {
            centerHorizontallyTo(parent)
            bottom.linkTo(repText.top, 8.dp)
        }

        constrain(workoutStateText) {
            centerHorizontallyTo(parent)
            bottom.linkTo(timerText.top, 8.dp)
        }

        constrain(repText) {
            centerHorizontallyTo(parent)
            centerVerticallyTo(parent)
        }
    }
}

fun calculateRadiusOffset(strokeSize: Float, dotStrokeSize: Float, markerStrokeSize: Float)
        : Float {
    return max(strokeSize, max(dotStrokeSize, markerStrokeSize))
}