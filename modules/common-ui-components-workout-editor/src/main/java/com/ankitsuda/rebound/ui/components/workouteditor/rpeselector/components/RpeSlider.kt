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

package com.ankitsuda.rebound.ui.components.workouteditor.rpeselector.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.ankitsuda.base.util.toLegacyInt
import com.ankitsuda.base.util.toReadableString
import com.ankitsuda.rebound.ui.theme.ReboundTheme
import timber.log.Timber
import kotlin.math.roundToInt

@Composable
internal fun RpeSlider(
    modifier: Modifier,
    value: Float?,
    onValueChange: (Float?) -> Unit,
) {
    val allRPEs = listOf(null, 6f, 7f, 7.5f, 8f, 8.5f, 9f, 9.5f, 10f)

    var mValue by remember {
        mutableStateOf(with(allRPEs.indexOf(value)) {
            if (this == -1) 0f else this.toFloat()
        })
    }

    val drawPadding = with(LocalDensity.current) { 10.dp.toPx() }
    val textSize = with(LocalDensity.current) { 10.dp.toPx() }
    val lineHeightDp = 10.dp
    val lineHeightPx = with(LocalDensity.current) { lineHeightDp.toPx() }
    val canvasHeight = 50.dp
    val textPaint = android.graphics.Paint().apply {
        color = ReboundTheme.colors.onBackground.toLegacyInt()
        textAlign = android.graphics.Paint.Align.CENTER
        this.textSize = textSize
    }
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .height(canvasHeight)
                .fillMaxWidth()
                .padding(
                    top = canvasHeight
                        .div(2)
                        .minus(lineHeightDp.div(2))
                )
        ) {
            val yStart = 0f
            val distance = (size.width.minus(2 * drawPadding)).div(allRPEs.size.minus(1))
            allRPEs.forEachIndexed { index, rpe ->
                drawLine(
                    color = Color.DarkGray,
                    start = Offset(x = drawPadding + index.times(distance), y = yStart),
                    end = Offset(x = drawPadding + index.times(distance), y = lineHeightPx)
                )
//                if (index.rem(2) == 1) {
                this.drawContext.canvas.nativeCanvas.drawText(
                    rpe?.toReadableString() ?: "?",
                    drawPadding + index.times(distance),
                    size.height,
                    textPaint
                )
//                }
            }
        }
        Slider(
            modifier = Modifier.fillMaxWidth(),
            value = mValue,
            valueRange = 0f..(allRPEs.size - 1).toFloat(),
            steps = allRPEs.size - 2,
            colors = SliderDefaults.colors(
                activeTickColor = Color.Transparent,
                inactiveTickColor = Color.Transparent
            ),
            onValueChange = {
                mValue = it
                onValueChange(allRPEs.getOrNull(it.roundToInt()))
            })
    }
}