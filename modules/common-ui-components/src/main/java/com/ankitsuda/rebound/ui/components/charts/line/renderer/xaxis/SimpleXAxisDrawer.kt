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

package com.ankitsuda.rebound.ui.components.charts.line.renderer.xaxis

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

class SimpleXAxisDrawer(
  private val labelTextSize: TextUnit = 12.sp,
  private val labelTextColor: Color = Color.Black,
  /** 1 means we draw everything. 2 means we draw every other, and so on. */
  private val labelRatio: Int = 1,
  private val axisLineThickness: Dp = 1.dp,
  private val axisLineColor: Color = Color.Black
) : XAxisDrawer {
  private val axisLinePaint = Paint().apply {
    isAntiAlias = true
    color = axisLineColor
    style = PaintingStyle.Stroke
  }

  private val textPaint = android.graphics.Paint().apply {
    isAntiAlias = true
    color = labelTextColor.toLegacyInt()
  }

  override fun requiredHeight(drawScope: DrawScope): Float {
    return with(drawScope) {
      (3f / 2f) * (labelTextSize.toPx() + axisLineThickness.toPx())
    }
  }

  override fun drawAxisLine(
    drawScope: DrawScope,
    canvas: Canvas,
    drawableArea: Rect
  ) {
    with(drawScope) {
      val lineThickness = axisLineThickness.toPx()
      val y = drawableArea.top + (lineThickness / 2f)

      canvas.drawLine(
        p1 = Offset(
          x = drawableArea.left,
          y = y
        ),
        p2 = Offset(
          x = drawableArea.right,
          y = y
        ),
        paint = axisLinePaint.apply {
          strokeWidth = lineThickness
        }
      )
    }
  }

  override fun drawAxisLabels(
    drawScope: DrawScope,
    canvas: Canvas,
    drawableArea: Rect,
    labels: List<String>
  ) {
    with(drawScope) {
      val labelPaint = textPaint.apply {
        textSize = labelTextSize.toPx()
        textAlign = android.graphics.Paint.Align.CENTER
      }

      val labelIncrements = drawableArea.width / (labels.size - 1)
      labels.forEachIndexed { index, label ->
        if (index.rem(labelRatio) == 0) {
          val x = drawableArea.left + (labelIncrements * (index))
          val y = drawableArea.bottom

          canvas.nativeCanvas.drawText(label, x, y, labelPaint)
        }
      }
    }
  }
}