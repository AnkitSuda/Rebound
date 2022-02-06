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

package com.ankitsuda.rebound.ui.components.charts.line.renderer.yaxis

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ankitsuda.base.util.toLegacyInt
//import com.ankitsuda.rebound.utils.toLegacyInt
import kotlin.math.max
import kotlin.math.roundToInt

typealias LabelFormatter = (value: Float) -> String

class SimpleYAxisDrawer(
  private val labelTextSize: TextUnit = 12.sp,
  private val labelTextColor: Color = Color.Black,
  private val labelRatio: Int = 3,
  private val labelValueFormatter: LabelFormatter = { value -> "%.1f".format(value) },
  private val axisLineThickness: Dp = 1.dp,
  private val axisLineColor: Color = Color.Black
) : YAxisDrawer {
  private val axisLinePaint = Paint().apply {
    isAntiAlias = true
    color = axisLineColor
    style = PaintingStyle.Stroke
  }
  private val textPaint = android.graphics.Paint().apply {
    isAntiAlias = true
    color = labelTextColor.toLegacyInt()
  }
  private val textBounds = android.graphics.Rect()

  override fun drawAxisLine(
    drawScope: DrawScope,
    canvas: Canvas,
    drawableArea: Rect
  ) = with(drawScope) {
    val lineThickness = axisLineThickness.toPx()
    val x = drawableArea.right - (lineThickness / 2f)

    canvas.drawLine(
      p1 = Offset(
        x = x,
        y = drawableArea.top
      ),
      p2 = Offset(
        x = x,
        y = drawableArea.bottom
      ),
      paint = axisLinePaint.apply {
        strokeWidth = lineThickness
      }
    )
  }

  override fun drawAxisLabels(
    drawScope: DrawScope,
    canvas: Canvas,
    drawableArea: Rect,
    minValue: Float,
    maxValue: Float
  ) = with(drawScope) {
    val labelPaint = textPaint.apply {
      textSize = labelTextSize.toPx()
      textAlign = android.graphics.Paint.Align.RIGHT
    }
    val minLabelHeight = (labelTextSize.toPx() * labelRatio.toFloat())
    val totalHeight = drawableArea.height
    val labelCount = max((drawableArea.height / minLabelHeight).roundToInt(), 2)

    for (i in 0..labelCount) {
      val value = minValue + (i * ((maxValue - minValue) / labelCount))

      val label = labelValueFormatter(value)
      val x =
        drawableArea.right - axisLineThickness.toPx() - (labelTextSize.toPx() / 2f)

      labelPaint.getTextBounds(label, 0, label.length, textBounds)

      val y =
        drawableArea.bottom - (i * (totalHeight / labelCount)) + (textBounds.height() / 2f)

      canvas.nativeCanvas.drawText(label, x, y, labelPaint)
    }
  }
}