package com.ankitsuda.rebound.ui.screens.part_measurements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ankitsuda.rebound.ui.components.TopBar
import com.ankitsuda.rebound.ui.components.TopBarBackIconButton
import com.ankitsuda.rebound.ui.components.charts.line.LineChart
import com.ankitsuda.rebound.ui.components.charts.line.LineChartData
import com.ankitsuda.rebound.ui.components.collapsing_toolbar.CollapsingToolbarScaffold
import com.ankitsuda.rebound.ui.components.collapsing_toolbar.rememberCollapsingToolbarScaffoldState

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
                            points = listOf(
                                LineChartData.Point(
                                    1f,
                                    "Label 1"
                                ),
                                LineChartData.Point(
                                    12f,
                                    "Label 2"
                                ),
                                LineChartData.Point(
                                    5f,
                                    "Label 3"
                                ),
                        )
                    ),
                    // Optional properties.
                    modifier = Modifier.height(150.dp).fillMaxSize(),
                    )
                }
            }
        }
    }

}