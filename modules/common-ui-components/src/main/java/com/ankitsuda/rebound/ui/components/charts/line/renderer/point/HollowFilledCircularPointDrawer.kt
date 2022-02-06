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

package com.ankitsuda.rebound.ui.components.charts.line.renderer.point

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class HollowFilledCircularPointDrawer(
    val diameter: Dp = 8.dp,
    val lineThickness: Dp = 2.dp,
    val strokeColor: Color = Color.Blue,
    val fillColor: Color = Color.White,
) : PointDrawer {

    private val paintStroke = Paint().apply {
        color = this@HollowFilledCircularPointDrawer.strokeColor
        style = PaintingStyle.Stroke
        isAntiAlias = true
    }

    private val paintFill = Paint().apply {
        color = this@HollowFilledCircularPointDrawer.fillColor
        style = PaintingStyle.Fill
        isAntiAlias = true
    }

    override fun drawPoint(
        drawScope: DrawScope,
        canvas: Canvas,
        center: Offset
    ) {
        with(drawScope as Density) {
            canvas.drawCircle(
                center = center,
                radius = diameter.toPx() / 2f,
                paint = paintFill
            )
            canvas.drawCircle(
                center = center,
                radius = diameter.toPx() / 2f,
                paint = paintStroke.apply {
                    strokeWidth = lineThickness.toPx()
                }
            )
        }
    }
}