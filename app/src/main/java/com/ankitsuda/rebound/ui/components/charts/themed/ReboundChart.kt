package com.ankitsuda.rebound.ui.components.charts.themed

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.ankitsuda.ui.theme.ReboundTheme

@Composable
fun ReboundChart(
    modifier: Modifier = Modifier,
    points: List<LineChartData.Point>,
    viewModel: ReboundChartViewModel = hiltViewModel()
) {
    val shaderEnabled by viewModel.shaderEnabled.collectAsState(initial = true)
    val lineThickness by viewModel.lineThickness.collectAsState(initial = 2)
    val pointDiameter by viewModel.pointDiameter.collectAsState(initial = 6)
    val pointLineThickness by viewModel.pointLineThickness.collectAsState(initial = 2)

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