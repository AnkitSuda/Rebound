package com.ankitsuda.rebound.ui.screens.workout

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import com.ankitsuda.rebound.ui.components.RSpacer
import com.ankitsuda.rebound.ui.components.RoutineItemCard
import com.ankitsuda.rebound.ui.components.TemplateItemCard
import com.ankitsuda.rebound.ui.components.TopBar
import com.ankitsuda.rebound.ui.components.collapsing_toolbar.CollapsingToolbarScaffold
import com.ankitsuda.rebound.ui.components.collapsing_toolbar.rememberCollapsingToolbarScaffoldState
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.MainAxisAlignment
import com.google.accompanist.flowlayout.SizeMode
import timber.log.Timber

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WorkoutScreen(navController: NavHostController) {
    val collapsingState = rememberCollapsingToolbarScaffoldState()

    CollapsingToolbarScaffold(
        state = collapsingState,
        toolbar = {
            TopBar(title = "Workout")
        },
        floatingActionButton = {

            ExtendedFloatingActionButton(
                modifier = Modifier,
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
                onClick = {
                })


        },
        floatingActionButtonPosition = FabPosition.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {


        // User routines
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            contentPadding = PaddingValues(bottom = 64.dp)
        ) {
            item {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Plans", style = MaterialTheme.typography.body1)
                        TextButton(onClick = {

                        }) {
                            Icon(
                                imageVector = Icons.Outlined.Add,
                                contentDescription = "New Plan"
                            )
                            RSpacer(space = 4.dp)
                            Text(text = "NEW", style = MaterialTheme.typography.button)
                        }
                    }

                    LazyRow() {
                        items(5) {
                            RoutineItemCard(
                                name = "Push",
                                date = "2 Aug 2021",
                                totalExercises = 7,
                                modifier = Modifier
                                    .width(((LocalConfiguration.current.screenWidthDp / 2) - 24).dp)
                                    .padding(
                                        start = if (it == 0) 16.dp else 0.dp,
                                        end = 16.dp
                                    )
                            ) {

                            }
                        }
                    }
                }
            }
            item {

                Column() {


                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Templates", style = MaterialTheme.typography.body1)
                        TextButton(onClick = {

                        }) {
                            Icon(
                                imageVector = Icons.Outlined.Add,
                                contentDescription = "New Template"
                            )
                            Text(text = "NEW", style = MaterialTheme.typography.button)
                        }
                    }


                }
            }

            items(5) {
                TemplateItemCard(
                    name = "Push",
                    totalExercises = 7,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                ) {

                }
            }

        }

    }
}