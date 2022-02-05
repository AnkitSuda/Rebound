package com.ankitsuda.rebound.ui.exercises

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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ankitsuda.navigation.LeafScreen
import com.ankitsuda.navigation.LocalNavigator
import com.ankitsuda.navigation.Navigator
import com.ankitsuda.rebound.domain.entities.ExerciseWithMuscle
import com.ankitsuda.rebound.domain.entities.Muscle
import com.ankitsuda.rebound.ui.components.*
import com.ankitsuda.rebound.ui.components.collapsing_toolbar.CollapsingToolbarScaffold
import com.ankitsuda.rebound.ui.theme.ReboundTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@OptIn(
    ExperimentalPagerApi::class,
    ExperimentalFoundationApi::class,
    ExperimentalAnimationApi::class
)
@Composable
fun ExercisesScreen(
    navController: NavController,
    isBottomSheet: Boolean,
    navigator: Navigator = LocalNavigator.current,
    viewModel: ExercisesScreenViewModel = hiltViewModel()
) {

//    val allExercises by viewModel.allExercises.collectAsState(initial = emptyList())
    val allMuscles by viewModel.allMuscles.collectAsState(initial = emptyList())
    val isSearchMode by viewModel.isSearchMode.observeAsState(false)
    val searchTerm by viewModel.searchTerm.observeAsState("")

    val filteredExercises = viewModel.filteredExercises

    val layout: @Composable () -> Unit = {
        ExercisesScreenContent(
            navController = navController,
            navigator = navigator,
            isBottomSheet = isBottomSheet,
            isSearchMode = isSearchMode,
            searchTerm = searchTerm,
            allExercises = filteredExercises,
            allMuscles = allMuscles,
            onToggleSearchMode = {
                viewModel.toggleSearchMode()
            },
            onChangeSearchTerm = {
                viewModel.setSearchTerm(it)
            }
        )
    }

    if (isBottomSheet) {
        BottomSheetSurface {
            layout()
        }
    } else {
        layout()
    }
}

@Composable
private fun ExercisesScreenContent(
    navController: NavController,
    navigator: Navigator,
    isBottomSheet: Boolean,
    isSearchMode: Boolean,
    searchTerm: String,
    allExercises: List<ExerciseWithMuscle>,
    allMuscles: List<Muscle>,
    onToggleSearchMode: () -> Unit,
    onChangeSearchTerm: (String) -> Unit
) {

    val tabData = arrayListOf<Any>("All").apply { addAll(allMuscles) }

    val pagerState = rememberPagerState(
        initialPage = 0,
    )
    val tabIndex = pagerState.currentPage
    val coroutineScope = rememberCoroutineScope()

    Column {
        CollapsingToolbarScaffold(
            toolbar = {
                Surface(
                    Modifier
                        .background(ReboundTheme.colors.background), elevation = 2.dp
                ) {

                    Column() {
                        if (!isSearchMode) {
                            TopBar(
                                statusBarEnabled = !isBottomSheet,
                                elevationEnabled = false,
                                title = "Exercises",
                                strictLeftIconAlignToStart = false,
                                alignRightIconToLeftWhenTitleAlignIsNotCenter = true,
                                leftIconBtn = {
                                    TopBarIconButton(
                                        icon = Icons.Outlined.Search,
                                        title = "Search",
                                        onClick = {
                                            onToggleSearchMode()
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
                                            navigator.navigate(LeafScreen.CreateExercise().route)
                                        })
                                })

                        } else {
                            TopSearchBar(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                statusBarEnabled = !isBottomSheet,
                                placeholder = "Search here...",
                                value = searchTerm,
                                onBackClick = {
                                    onToggleSearchMode()
                                },
                                onValueChange = {
                                    onChangeSearchTerm(it)
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
                count = tabData.size,
            ) { index ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                ) {


                    // Select all exercises for All tab or get only muscle specific exercises
                    val exercisesForThisTab =
                        if (index == 0) allExercises else allExercises.filter {
                            it.exercise.primaryMuscleTag == allMuscles[index - 1].tag
                        }

                    items(exercisesForThisTab.size) {
                        val item = exercisesForThisTab[it]
                        ExerciseItem(
                            name = item.exercise.name.toString(),
                            muscle = item.primaryMuscle?.name.toString(),
                            totalLogs = item.junctions.size,
                            onClick = {
                                if (isBottomSheet) {
                                    navController.previousBackStackEntry?.savedStateHandle?.set(
                                        "result_exercises_screen_exercise_id",
                                        item.exercise.exerciseId
                                    )
                                    navController.popBackStack()
                                } else {
                                    navigator.navigate(
                                        LeafScreen.ExerciseDetails.createRoute(
                                            exerciseId = item.exercise.exerciseId
                                        )
                                    )
                                }
                            })
                    }
                }
            }


        }
    }
}