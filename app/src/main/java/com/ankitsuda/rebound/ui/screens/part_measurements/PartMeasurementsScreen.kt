package com.ankitsuda.rebound.ui.screens.part_measurements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ankitsuda.rebound.ui.components.*
import com.ankitsuda.rebound.ui.components.charts.line.LineChart
import com.ankitsuda.rebound.ui.components.charts.line.LineChartData
import com.ankitsuda.rebound.ui.components.charts.line.renderer.line.GradientLineShader
import com.ankitsuda.rebound.ui.components.charts.line.renderer.line.SolidLineDrawer
import com.ankitsuda.rebound.ui.components.charts.line.renderer.line.SolidLineShader
import com.ankitsuda.rebound.ui.components.charts.line.renderer.point.FilledCircularPointDrawer
import com.ankitsuda.rebound.ui.components.charts.line.renderer.point.HollowCircularPointDrawer
import com.ankitsuda.rebound.ui.components.charts.line.renderer.point.HollowFilledCircularPointDrawer
import com.ankitsuda.rebound.ui.components.charts.line.renderer.xaxis.SimpleXAxisDrawer
import com.ankitsuda.rebound.ui.components.charts.line.renderer.yaxis.SimpleYAxisDrawer
import com.ankitsuda.rebound.ui.components.charts.themed.ReboundChart
import com.ankitsuda.rebound.ui.components.collapsing_toolbar.CollapsingToolbarScaffold
import com.ankitsuda.rebound.ui.components.collapsing_toolbar.rememberCollapsingToolbarScaffoldState
import com.ankitsuda.rebound.ui.screens.main_screen.LocalBottomSheet
import com.ankitsuda.rebound.ui.theme.ReboundTheme
import kotlin.random.Random

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
        floatingActionButton = {
            FloatingActionButton(onClick = {
                bottomSheet.show {
                    partId?.let {
                        AddPartMeasurementBottomSheet(it)
                    }
                }
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
            contentPadding = PaddingValues(16.dp)
        ) {

            item {
                AppCard {
                    ReboundChart(
                        points = points,
                        modifier = Modifier
                            .height(250.dp)
                            .fillMaxWidth()
                            .padding(16.dp),
                    )
                }
            }

            item {
                Text(
                    text = "History",
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                )
            }

            items(logs.size) {
                val log = logs[it]
                Row(Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        text = log.measurement.toString(),
                        style = MaterialTheme.typography.body2,
                        modifier = Modifier
                    )
                    Text(
                        text = log.createdAt.toString(),
                        style = MaterialTheme.typography.caption,
                        modifier = Modifier
                    )
                }
            }

        }
    }

}