package com.ankitsuda.rebound.ui.screens.part_measurements

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ankitsuda.rebound.ui.Route
import com.ankitsuda.rebound.ui.components.*
import com.ankitsuda.rebound.ui.components.charts.line.LineChartData
import com.ankitsuda.rebound.ui.components.charts.themed.ReboundChart
import com.ankitsuda.rebound.ui.components.collapsing_toolbar.CollapsingToolbarScaffold
import com.ankitsuda.rebound.ui.components.collapsing_toolbar.rememberCollapsingToolbarScaffoldState
import com.ankitsuda.rebound.ui.screens.main_screen.LocalBottomSheet
import com.ankitsuda.rebound.ui.theme.ReboundTheme
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle

@Composable
fun PartMeasurementsScreen(
    navController: NavHostController,
    viewModel: PartMeasurementsScreenViewModel = hiltViewModel()
) {
    val bottomSheet = LocalBottomSheet.current
    val partId by remember {
        mutableStateOf(
            navController.currentBackStackEntry?.arguments?.getString(
                "partId"
            )?.toLong()
        )
    }


    val collapsingState = rememberCollapsingToolbarScaffoldState()

    val logs by viewModel.getLogsForPart(partId!!).collectAsState(initial = emptyList())

    val points = if (logs.isEmpty()) emptyList() else logs.map {
        LineChartData.Point(it.measurement, "id ${it.id}")
    }

    val showChart = points.size > 1


    CollapsingToolbarScaffold(
        state = collapsingState,
        toolbar = {
            TopBar(title = partId.toString(), strictLeftIconAlignToStart = true, leftIconBtn = {
                TopBarBackIconButton {
                    navController.popBackStack()
                }
            })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
//                bottomSheet.show {
                partId?.let {
//                        AddPartMeasurementBottomSheet(it)
                    navController.navigate(Route.AddPartMeasurement.createRoute(partId = it))
                }
//                }
            }) {
                Icon(imageVector = Icons.Outlined.Add, contentDescription = "Add measurement")
            }
        },
        modifier = Modifier.background(MaterialTheme.colors.background)
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            contentPadding = PaddingValues(top = 16.dp, bottom = 72.dp)
        ) {


            if (showChart) {
                item {

                    AppCard(modifier = Modifier.padding(start = 16.dp, end = 16.dp)) {
                        ReboundChart(
                            points = points,
                            modifier = Modifier
                                .height(250.dp)
                                .fillMaxWidth()
                                .padding(16.dp),
                        )

                    }
                }
            }

            item {
                Text(
                    text = "History",
                    style = ReboundTheme.typography.h6,
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
                )
            }

            items(logs.size) {
                val log = logs[it]
                Row(
                    Modifier
                        .fillMaxWidth()
                        .clickable {
//                            bottomSheet.show {
                            navController.navigate(
                                Route.AddPartMeasurement.createRoute(
                                    partId = partId!!,
                                    logId = log.id
                                )
                            )
//                            AddPartMeasurementBottomSheet(partId = partId!!, logId = log.id)
//                            }
                        }
                        .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = log.measurement.toString(),
                        style = ReboundTheme.typography.body2,
                        modifier = Modifier
                    )
                    Text(
                        text = log.createdAt!!.format(
                            DateTimeFormatter.ofLocalizedDateTime(
                                FormatStyle.MEDIUM,
                                FormatStyle.SHORT
                            )
                        ),
                        style = ReboundTheme.typography.caption,
                        color = ReboundTheme.colors.onBackground.copy(alpha = 0.5f),
                        modifier = Modifier
                    )
                }
            }

        }
    }

}