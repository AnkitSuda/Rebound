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

package com.ankitsuda.rebound.ui.components.charts.themed

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ankitsuda.rebound.ui.components.charts.line.LineChart
import com.ankitsuda.rebound.ui.components.charts.line.LineChartData
import com.ankitsuda.rebound.ui.components.charts.line.renderer.line.GradientLineShader
import com.ankitsuda.rebound.ui.components.charts.line.renderer.line.NoLineShader
import com.ankitsuda.rebound.ui.components.charts.line.renderer.line.SolidLineDrawer
import com.ankitsuda.rebound.ui.components.charts.line.renderer.point.HollowFilledCircularPointDrawer
import com.ankitsuda.rebound.ui.components.charts.line.renderer.xaxis.SimpleXAxisDrawer
import com.ankitsuda.rebound.ui.components.charts.line.renderer.yaxis.SimpleYAxisDrawer
import com.ankitsuda.rebound.ui.theme.LocalThemeState
import com.ankitsuda.rebound.ui.theme.ReboundTheme

@Composable
fun ReboundChart(
    modifier: Modifier = Modifier,
    points: List<LineChartData.Point>,
) {
    val theme = LocalThemeState.current
    val shaderEnabled = theme.chartsShaderEnabled
    val lineThickness = theme.chartsLineThickness
    val pointDiameter = theme.chartsPointDiameter
    val pointLineThickness = theme.chartsPointLineThickness

    LineChart(
        lineChartData = LineChartData(
            points = points
        ),
        modifier = modifier,
        pointDrawer = HollowFilledCircularPointDrawer(
            diameter = pointDiameter.dp,
            lineThickness = pointLineThickness.dp,
            strokeColor = ReboundTheme.colors.primary,
            fillColor = Color.White,
        ),
        lineDrawer = SolidLineDrawer(
            thickness = lineThickness.dp,
            color = ReboundTheme.colors.primary
        ),
        xAxisDrawer = SimpleXAxisDrawer(
            axisLineThickness = 1.dp,
            labelTextColor = ReboundTheme.colors.onBackground.copy(alpha = 0.7f),
            axisLineColor = ReboundTheme.colors.onBackground.copy(alpha = 0.5f)
        ),
        yAxisDrawer = SimpleYAxisDrawer(
            axisLineThickness = 1.dp,
            labelTextColor = ReboundTheme.colors.onBackground.copy(alpha = 0.7f),
            axisLineColor = ReboundTheme.colors.onBackground.copy(alpha = 0.5f)
        ),
        horizontalOffset = 0f,
        lineShader = if (shaderEnabled) {
            GradientLineShader(
                colors = listOf(
                    ReboundTheme.colors.primary.copy(alpha = 0.4f),
                    ReboundTheme.colors.primary.copy(alpha = 0.3f),
                    ReboundTheme.colors.primary.copy(alpha = 0.2f),
                    ReboundTheme.colors.primary.copy(alpha = 0.05f),
                    ReboundTheme.colors.primary.copy(alpha = 0.0f),
                )
            )
        } else {
            NoLineShader
        }
    )
}