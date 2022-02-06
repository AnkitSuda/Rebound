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

package com.github.tehras.charts.piechart

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class AxisLine(
  val thickness: Dp = 1.5.dp,
  val color: Color = Color.Gray
) {
  private val paint = Paint().apply {
    this.color = this@AxisLine.color
    this.style = PaintingStyle.Stroke
  }

  fun paint(density: Density) = paint.apply {
    this.strokeWidth = thickness.value * density.density
  }
}
