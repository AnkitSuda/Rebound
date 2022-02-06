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

package com.ankitsuda.rebound.ui.components.charts.line.renderer.line

import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class SolidLineDrawer(
  val thickness: Dp = 3.dp,
  val color: Color = Color.Cyan
) : LineDrawer {
  private val paint = Paint().apply {
    this.color = this@SolidLineDrawer.color
    this.style = PaintingStyle.Stroke
    this.isAntiAlias = true
  }

  override fun drawLine(
    drawScope: DrawScope,
    canvas: Canvas,
    linePath: Path
  ) {
    val lineThickness = with(drawScope) {
      thickness.toPx()
    }

    canvas.drawPath(
      path = linePath,
      paint = paint.apply {
        strokeWidth = lineThickness
      }
    )
  }
}