package com.ankitsuda.rebound.ui.screens.workout

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.OpenInFull
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.asLiveData
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.ankitsuda.ui.components.*
import com.ankitsuda.ui.components.collapsing_toolbar.CollapsingToolbarScaffold
import com.ankitsuda.ui.components.collapsing_toolbar.rememberCollapsingToolbarScaffoldState
import com.ankitsuda.ui.theme.ReboundTheme
import com.ankitsuda.rebound.utils.NONE_WORKOUT_ID
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.MainAxisAlignment
import com.google.accompanist.flowlayout.SizeMode
import timber.log.Timber

@OptIn(ExperimentalFoundationApi::class, ExperimentalAnimationApi::class)
@Composable
fun WorkoutScreen(
    navController: NavController,
    viewModel: WorkoutScreenViewModel = hiltViewModel()
) {
    val collapsingState = rememberCollapsingToolbarScaffoldState()
    val currentWorkoutId by viewModel.currentWorkoutId.collectAsState(initial = -1)

    CollapsingToolbarScaffold(
        state = collapsingState,
        toolbar = {
            TopBar(title = "Workout")
        },
        floatingActionButton = {

            AnimatedVisibility(visible = currentWorkoutId == (-1).toLong()) {
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
                        viewModel.startEmptyWorkout()
                    })

            }
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

            if (currentWorkoutId != NONE_WORKOUT_ID) {
                item {
                    AppCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 8.dp),
                        backgroundColor = ReboundTheme.colors.primary,
                        onClick = {
                            // Expand panel
                        }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column() {
                                Text(
                                    text = "Ongoing Workout",
                                    style = ReboundTheme.typography.h6,
                                    color = ReboundTheme.colors.onPrimary
                                )
                                RSpacer(space = 4.dp)
                                Text(
                                    text = "32 minutes 12 seconds",
                                    style = ReboundTheme.typography.body2,
                                    color = ReboundTheme.colors.onPrimary.copy(alpha = 0.7f)

                                )
                            }
                            IconButton(onClick = { }) {
                                Icon(
                                    imageVector = Icons.Outlined.OpenInFull,
                                    contentDescription = "Open",
                                    tint = ReboundTheme.colors.onPrimary
                                )
                            }
                        }
                    }
                }
            }

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