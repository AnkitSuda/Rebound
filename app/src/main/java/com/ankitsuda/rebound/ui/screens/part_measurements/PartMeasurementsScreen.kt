package com.ankitsuda.rebound.ui.screens.part_measurements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ankitsuda.rebound.ui.components.TopBar
import com.ankitsuda.rebound.ui.components.TopBarBackIconButton
import com.ankitsuda.rebound.ui.components.charts.line.LineChart
import com.ankitsuda.rebound.ui.components.charts.line.LineChartData
import com.ankitsuda.rebound.ui.components.charts.line.renderer.line.SolidLineDrawer
import com.ankitsuda.rebound.ui.components.charts.line.renderer.point.FilledCircularPointDrawer
import com.ankitsuda.rebound.ui.components.charts.line.renderer.xaxis.SimpleXAxisDrawer
import com.ankitsuda.rebound.ui.components.charts.line.renderer.yaxis.SimpleYAxisDrawer
import com.ankitsuda.rebound.ui.components.collapsing_toolbar.CollapsingToolbarScaffold
import com.ankitsuda.rebound.ui.components.collapsing_toolbar.rememberCollapsingToolbarScaffoldState
import com.ankitsuda.rebound.ui.theme.ReboundTheme
import kotlin.random.Random

@Composable
fun PartMeasurementsScreen(navController: NavHostController) {
    val partId by remember {
        mutableStateOf(
            navController.currentBackStackEntry?.arguments?.getString(
                "partId"
            )
        )
    }


    val collapsingState = rememberCollapsingToolbarScaffoldState()

    val points = arrayListOf<LineChartData.Point>()


    repeat(6) {
        points.add(
            LineChartData.Point(
                Random.nextInt(1, 50).toFloat(),
                "Label $it"
            )
        )
    }


    CollapsingToolbarScaffold(
        state = collapsingState,
        toolbar = {
            TopBar(title = partId.toString(), strictLeftIconAlignToStart = true, leftIconBtn = {
                TopBarBackIconButton {
                    navController.popBackStack()
                }
            })
        },
        modifier = Modifier.background(MaterialTheme.colors.background)
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            contentPadding = PaddingValues(16.dp)
        ) {

            item {
                Box() {
                    LineChart(
                        lineChartData = LineChartData(
                            points = points
                        ),
                        // Optional properties.
                        modifier = Modifier
                            .height(200.dp)
                            .fillMaxSize(),
                        pointDrawer = FilledCircularPointDrawer(
                            diameter = 6.dp,
                            color = ReboundTheme.colors.primary
                        ),
                        lineDrawer = SolidLineDrawer(
                            thickness = 2.dp,
                            color = ReboundTheme.colors.primary.copy(alpha = 0.75f)
                        ),
                        xAxisDrawer = SimpleXAxisDrawer(),
                        yAxisDrawer = SimpleYAxisDrawer(),
                        horizontalOffset = 1f,
                    )
                }
            }
        }
    }

}