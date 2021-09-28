package com.ankitsuda.rebound.ui.screens.exercises

import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ankitsuda.rebound.data.entities.Muscle
import com.ankitsuda.rebound.ui.Route
import com.ankitsuda.rebound.ui.components.ExerciseItem
import com.ankitsuda.rebound.ui.components.TopBar
import com.ankitsuda.rebound.ui.components.TopBarIconButton
import com.ankitsuda.rebound.ui.components.TopSearchBar
import com.ankitsuda.rebound.ui.components.collapsing_toolbar.CollapsingToolbarScaffold
import com.ankitsuda.rebound.ui.components.collapsing_toolbar.rememberCollapsingToolbarScaffoldState
import com.ankitsuda.rebound.ui.screens.create_exercise.CreateExerciseScreen
import com.ankitsuda.rebound.ui.screens.main_screen.LocalBottomSheet
import com.ankitsuda.rebound.ui.theme.ReboundTheme
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import kotlin.random.Random

@OptIn(
    ExperimentalPagerApi::class,
    ExperimentalFoundationApi::class,
    ExperimentalAnimationApi::class
)
@Composable
fun ExercisesScreen(
    navController: NavHostController,
    isBottomSheet: Boolean = true,
    viewModel: ExercisesScreenViewModel = hiltViewModel()
) {

    val allExercises by viewModel.allExercises.collectAsState(initial = emptyList())
    val allMuscles by viewModel.allMuscles.collectAsState(initial = emptyList())

    val tabData = arrayListOf<Any>("All").apply { addAll(allMuscles) }

    val pagerState = rememberPagerState(
        pageCount = tabData.size,
        initialOffscreenLimit = 2,
        infiniteLoop = false,
        initialPage = 0,
    )
    val tabIndex = pagerState.currentPage
    val coroutineScope = rememberCoroutineScope()


    val isSearchMode by viewModel.isSearchMode.observeAsState(false)
    val searchTerm by viewModel.searchTerm.observeAsState("")
    CollapsingToolbarScaffold(
        toolbar = {
            Surface(
                Modifier
                    .background(ReboundTheme.colors.background)
                    .zIndex(2f), elevation = 2.dp
            ) {

                Column() {
                    if (!isSearchMode) {
                        TopBar(
                            elevationEnabled = false,
                            title = "Exercises",
                            strictLeftIconAlignToStart = false,
                            alignRightIconToLeftWhenTitleAlignIsNotCenter = true,
                            leftIconBtn = {
                                TopBarIconButton(
                                    icon = Icons.Outlined.Search,
                                    title = "Search",
                                    onClick = {
                                        viewModel.toggleSearchMode()
                                    })
                            },
                            rightIconBtn = {
                                TopBarIconButton(
                                    icon = Icons.Outlined.Add,
                                    title = "Create Exercise",
                                    onClick = {
//                                        bottomSheet.show {
//                                            CreateExerciseScreen()
//                                        }
                                        navController.navigate(Route.CreateExercise.route)
                                    })
                            })

                    } else {
                        TopSearchBar(
                            modifier = Modifier
                                .fillMaxWidth(),
                            placeholder = "Search here...",
                            value = searchTerm,
                            onBackClick = {
                                viewModel.toggleSearchMode()
                            },
                            onValueChange = {
                                viewModel.setSearchTerm(it)
                            },
                        )
                    }

                    ScrollableTabRow(
                        selectedTabIndex = tabIndex,
                        edgePadding = 0.dp,
                        backgroundColor = ReboundTheme.colors.background,
                        divider = { Divider(thickness = 0.dp) },
                        indicator = { tabPositions ->
                            TabRowDefaults.Indicator(
                                Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
                            )
                        }
                    ) {
                        tabData.forEachIndexed { index, item ->
                            Tab(
                                selected = tabIndex == index,
                                onClick = {
                                    coroutineScope.launch {
                                        pagerState.animateScrollToPage(index)
                                    }
                                },
                                text = {
                                    Text(text = if (item is Muscle) item.name else item.toString())

                                }
                            )
                        }
                    }
                }
            }
        },
    ) {


        HorizontalPager(
            state = pagerState,
            verticalAlignment = Alignment.Top,
        ) { index ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
            ) {


                // Select all exercises for All tab or get only muscle specific exercises
                val exercisesForThisTab =
                    if (index == 0) allExercises else allExercises.filter {
                        it.primaryMuscleTag == allMuscles[index - 1].tag
                    }

                items(exercisesForThisTab.size) {
                    val exercise = exercisesForThisTab[it]
                    ExerciseItem(
                        name = exercise.name.toString(),
                        muscle = exercise.primaryMuscleTag.toString(),
                        totalLogs = it,
                        onClick = {
                            if (isBottomSheet) {
                                navController.previousBackStackEntry?.savedStateHandle?.set(
                                    "result_exercises_screen_exercise_id",
                                    exercise.id
                                )
                                navController.popBackStack()
                            } else {
                                navController.navigate(Route.ExerciseDetails.createRoute(exerciseId = exercise.id))
                            }
                        })
                }
            }
        }


    }
}