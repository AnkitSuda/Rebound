package com.ankitsuda.rebound.ui.screens.exercise

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ankitsuda.rebound.ui.components.AppCard
import com.ankitsuda.rebound.ui.components.RSpacer
import com.ankitsuda.rebound.ui.components.charts.line.LineChartData
import com.ankitsuda.rebound.ui.components.charts.themed.ReboundChart
import com.ankitsuda.rebound.ui.theme.ReboundTheme
import kotlin.random.Random

@Composable
fun ExerciseDetailChartsTab() {


    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(4) {
            AppCard(modifier = Modifier.padding(bottom = 16.dp)) {
                ReboundChart(
                    points = getRandomPoints(),
                    modifier = Modifier
                        .height(250.dp)
                        .fillMaxWidth()
                        .padding(16.dp),
                )
            }
        }
    }
}

private fun getRandomPoints(): List<LineChartData.Point> {
    val points = arrayListOf<LineChartData.Point>()
    repeat(6) {
        points.add(
            LineChartData.Point(
                Random.nextInt(1, 50).toFloat(),
                "Label $it"
            )
        )
    }
    return points
}