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

package com.ankitsuda.rebound.ui.components.charts.line

data class LineChartData(
  val points: List<Point>,
  /** This is percentage we pad yValue by.**/
  val padBy: Float = 20f,
  val startAtZero: Boolean = false
) {
  init {
    require(padBy in 0f..100f)
  }

  private val yMinMax: Pair<Float, Float>
    get() {
      val min = points.minByOrNull { it.value }?.value ?: 0f
      val max = points.maxByOrNull { it.value }?.value ?: 0f

      return min to max
    }

  internal val maxYValue: Float =
    yMinMax.second + ((yMinMax.second - yMinMax.first) * padBy / 100f)
  internal val minYValue: Float
    get() {
      return if (startAtZero) {
        0f
      } else {
        yMinMax.first - ((yMinMax.second - yMinMax.first) * padBy / 100f)
      }
    }
  internal val yRange = maxYValue - minYValue

  data class Point(val value: Float, val label: String)
}