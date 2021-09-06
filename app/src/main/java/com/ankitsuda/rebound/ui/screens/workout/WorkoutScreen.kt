package com.ankitsuda.rebound.ui.screens.workout

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ankitsuda.rebound.ui.components.RoutineItemCard
import com.ankitsuda.rebound.ui.components.TopBar
import com.ankitsuda.rebound.ui.components.collapsing_toolbar.CollapsingToolbarScaffold
import com.ankitsuda.rebound.ui.components.collapsing_toolbar.rememberCollapsingToolbarScaffoldState
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.MainAxisAlignment
import com.google.accompanist.flowlayout.SizeMode
import timber.log.Timber

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WorkoutScreen(navController: NavHostController, panelTopHeightDp: Dp) {
    val collapsingState = rememberCollapsingToolbarScaffoldState()

    CollapsingToolbarScaffold(
        state = collapsingState,
        toolbar = {
            TopBar(title = "Workout")
        },
        floatingActionButton = {

            ExtendedFloatingActionButton(
                modifier = Modifier
                    .padding(bottom = panelTopHeightDp),
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 2.dp,
                    pressedElevation = 4.dp
                ),
                text = { Text(text = "Empty Workout") },
                icon = {
                    Icon(
                        imageVector = Icons.Outlined.Add,
                        contentDescription = null
                    )
                },
                onClick = { Timber.d("New") })


        },
        floatingActionButtonPosition = FabPosition.Center,
        modifier = Modifier.fillMaxSize()
    ) {


        // User routines
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentPadding = PaddingValues(bottom = panelTopHeightDp)
        ) {
            item {

                Column() {


                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "My Routines", style = MaterialTheme.typography.body1)
                        TextButton(onClick = {

                        }) {
                            Icon(
                                imageVector = Icons.Outlined.Add,
                                contentDescription = "New Routine"
                            )
                            Text(text = "New Routine", style = MaterialTheme.typography.button)
                        }
                    }
                    FlowRow(
                        mainAxisAlignment = MainAxisAlignment.Start,
                        mainAxisSize = SizeMode.Expand,
                        crossAxisSpacing = 16.dp,
                        mainAxisSpacing = 16.dp
                    ) {
                        repeat(4) {
                            RoutineItemCard(
                                name = "Push",
                                date = "2 Aug 2021",
                                totalExercises = 7,
                                modifier = Modifier.width(((LocalConfiguration.current.screenWidthDp / 2) - 24).dp)
                            ) {

                            }
                        }
                    }


                }
            }


        }

    }
}