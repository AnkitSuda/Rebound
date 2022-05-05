/*
 * Copyright (c) 2022 Ankit Suda.
 *
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 */

package com.ankitsuda.rebound.ui.main

import android.view.inputmethod.InputConnection
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.plusAssign
import com.ankitsuda.base.ui.ThemeState
import com.ankitsuda.base.util.LabelVisible
import com.ankitsuda.base.util.NONE_WORKOUT_ID
import com.ankitsuda.base.util.toast
import com.ankitsuda.common.compose.*
import com.ankitsuda.navigation.LeafScreen
import com.ankitsuda.navigation.LocalNavigator
import com.ankitsuda.navigation.NavigatorHost
import com.ankitsuda.navigation.TabRootScreen
import com.ankitsuda.rebound.resttimer.TimerState
import com.ankitsuda.rebound.ui.components.panel_tops.PanelTopCollapsed
import com.ankitsuda.rebound.ui.components.panel_tops.PanelTopDragHandle
import com.ankitsuda.rebound.ui.components.panel_tops.PanelTopExpanded
import com.ankitsuda.rebound.ui.ThemeViewModel
import com.ankitsuda.rebound.ui.components.workouteditor.ReboundKeyboardHost
import com.ankitsuda.rebound.ui.keyboard.LocalReboundSetKeyboard
import com.ankitsuda.rebound.ui.keyboard.ReboundSetKeyboard
import com.ankitsuda.rebound.ui.keyboard.ReboundSetKeyboardComponent
import com.ankitsuda.rebound.ui.keyboard.enums.ReboundKeyboardType
import com.ankitsuda.rebound.ui.navigation.AppNavigation
import com.ankitsuda.rebound.ui.theme.LocalThemeState
import com.ankitsuda.rebound.ui.theme.ReboundTheme
import com.ankitsuda.rebound.ui.theme.ReboundThemeWrapper
import com.ankitsuda.rebound.ui.workout_panel.WorkoutPanel
import com.ankitsuda.rebound.ui.workout_panel.WorkoutPanelViewModel
import com.google.accompanist.insets.navigationBarsHeight
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.navigation.material.BottomSheetNavigator
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Root screen of the app
 */
@OptIn(
    ExperimentalMaterialApi::class, ExperimentalMaterialNavigationApi::class,
    androidx.compose.animation.ExperimentalAnimationApi::class
)
@Composable
fun MainScreen(
) {
    val navController = rememberAnimatedNavController()
    val bottomSheetNavigator = rememberBottomSheetNavigator()
    navController.navigatorProvider += bottomSheetNavigator
    MainLayout(
        navController = navController,
        bottomSheetNavigator = bottomSheetNavigator,
    )
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterialNavigationApi::class)
@Composable
private fun MainLayout(
    navController: NavHostController,
    bottomSheetNavigator: BottomSheetNavigator,
    workoutPanelViewModel: WorkoutPanelViewModel = hiltViewModel(),
    themeViewModel: ThemeViewModel = hiltViewModel(),
    viewModel: MainScreenViewModel = hiltViewModel(),
) {
    val themeState by rememberFlowWithLifecycle(themeViewModel.themeState).collectAsState(ThemeState())

    val swipeableState = rememberSwipeableState(0)
    val coroutine = rememberCoroutineScope()
    val context = LocalContext.current


    val currentWorkoutId by viewModel.currentWorkoutId.collectAsState(initial = NONE_WORKOUT_ID)

    val panelHidden = currentWorkoutId == NONE_WORKOUT_ID

    var dialogContent: @Composable () -> Unit by remember {
        mutableStateOf({})
    }

    var dialogVisible by remember {
        mutableStateOf(false)
    }

    // Dialog
    val dialog = MainDialog()
    dialog.showDialog = {
        dialogContent = dialog.dialogContent
        dialogVisible = true
        Timber.d("show dialog")
    }
    dialog.hideDialog = {
        dialogVisible = false
    }

    // Panel
    val mainPanel = MainPanel(
        swipeableState = swipeableState
    )

    ReboundThemeWrapper(themeState = themeState) {
        NavigatorHost {
            ReboundKeyboardHost {
                CompositionLocalProvider(
                    LocalDialog provides dialog,
                    LocalPanel provides mainPanel,
                ) {
                    Box() {
                        /**
                         * Temporary using ModalBottomSheetLayout
                         * will create a custom implementation later in MainScreenScaffold with proper status bar padding
                         * and auto corner radius
                         */
                        com.google.accompanist.navigation.material.ModalBottomSheetLayout(
                            sheetElevation = 0.dp,
                            sheetBackgroundColor = Color.Transparent,
                            bottomSheetNavigator = bottomSheetNavigator
                        ) {
                            val navigator = LocalNavigator.current
                            MainScreenScaffold(
                                modifier = Modifier,
                                panelHidden = panelHidden,
                                swipeableState = swipeableState,
                                bottomBar = {
                                    BottomBar(
                                        elevationEnabled = panelHidden,
                                        navController = navController,
                                    )
                                },
                                panel = {
                                    WorkoutPanel(navController)
                                },
                                panelTopCommon = {
                                    PanelTopDragHandle()
                                },
                                panelTopCollapsed = {
                                    val currentTimeStr by workoutPanelViewModel.currentDurationStr.collectAsState()
                                    PanelTopCollapsed(
                                        currentTimeStr = currentTimeStr,
                                    )
                                },
                                panelTopExpanded = {
                                    val restTimerElapsedTime by viewModel.restTimerElapsedTime.observeAsState(
                                        null
                                    )
                                    val restTimerTotalTime by viewModel.restTimerTotalTime.observeAsState(
                                        null
                                    )
                                    val restTimerTimerState by viewModel.restTimerState.observeAsState(
                                        TimerState.EXPIRED
                                    )
                                    val restTimerTimeString by viewModel.restTimerTimeString.observeAsState(
                                        null
                                    )

                                    PanelTopExpanded(
                                        restTimerElapsedTime = restTimerElapsedTime,
                                        restTimerTotalTime = restTimerTotalTime,
                                        restTimerTimeString = restTimerTimeString,
                                        isTimerRunning = restTimerTimerState != TimerState.EXPIRED,
                                        onCollapseBtnClicked = {
                                            coroutine.launch {
                                                swipeableState.animateTo(0)
                                            }
                                        },
                                        onTimerBtnClicked = {
                                            navigator.navigate(LeafScreen.RestTimer.createRoute())
                                        },
                                        onFinishBtnClicked = {
                                            workoutPanelViewModel.finishWorkout(onSetsIncomplete = {
                                                context.toast(message = "Please complete all sets to finish")
                                            })
                                        })
                                }) {
                                Box(
                                    Modifier
                                        .fillMaxSize()
                                        .background(
                                            color = LocalThemeState.current.backgroundColor
                                        )
                                ) {
                                    AppNavigation(navController)
                                }
                            }
                        }

                        if (dialogVisible) {
                            AlertDialog(onDismissRequest = {
                                dialogVisible = false
                            },
                                buttons = {
                                    dialogContent()
                                })
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun BottomBar(
    elevationEnabled: Boolean = false,
    navController: NavHostController,
) {
    val bottomNavigationItems = listOf(
        BottomNavigationScreens.Home,
        BottomNavigationScreens.History,
        BottomNavigationScreens.Workout,
        BottomNavigationScreens.Exercises,
        BottomNavigationScreens.More
    )

    val theme = LocalThemeState.current

    val labelVisible = theme.bottomBarLabelVisible
    val labelWeight = theme.bottomBarLabelWeight
    val iconSize = theme.bottomBarIconSize




    BottomNavigation(
        contentColor = LocalThemeState.current.primaryColor,
        backgroundColor = LocalThemeState.current.backgroundColor,
        elevation = if (elevationEnabled) 8.dp else 0.dp,
        modifier = Modifier
            .navigationBarsHeight(additional = 56.dp)
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        bottomNavigationItems.forEach { screen ->

            val isSelected = currentDestination?.hierarchy?.any { it.route == screen.route } == true

            BottomNavigationItem(
                icon = {
                    Icon(
                        if (isSelected) screen.selectedIcon else screen.unselectedIcon,
                        screen.title,
                        Modifier.size(iconSize.dp)
                    )
                },
                label = if (labelVisible == LabelVisible.NEVER) {
                    null
                } else {
                    {

                        Text(
                            text = screen.title,
                            maxLines = 1,
                            overflow = TextOverflow.Visible,
                            fontWeight = when (labelWeight) {
                                "bold" -> FontWeight.Bold
                                else -> FontWeight.Normal
                            }
                        )

                    }
                },
                selectedContentColor = ReboundTheme.colors.primary,
                unselectedContentColor = ReboundTheme.colors.onBackground.copy(0.4f),
                alwaysShowLabel = labelVisible == LabelVisible.ALWAYS,
                modifier = Modifier.navigationBarsPadding(),
                selected = isSelected,
                onClick = {
                    // This if check gives us a "singleTop" behavior where we do not create a
                    // second instance of the composable if we are already on that destination
//                               if (currentRoute != screen.route) {
//                                   navController.navigate(screen.route)
//                               }
                    navController.navigate(screen.route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }

                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }

                },
            )
        }
    }
}

sealed class BottomNavigationScreens(
    val route: String,
    val title: String,
    val unselectedIcon: ImageVector,
    val selectedIcon: ImageVector
) {
    object Home :
        BottomNavigationScreens(
            TabRootScreen.HomeTab.route,
            "Home",
            Icons.Outlined.Home,
            Icons.Filled.Home
        )

    object History :
        BottomNavigationScreens(
            TabRootScreen.HistoryTab.route,
            "History",
            Icons.Outlined.WatchLater,
            Icons.Filled.WatchLater
        )

    object Workout :
        BottomNavigationScreens(
            TabRootScreen.WorkoutTab.route,
            "Workout",
            Icons.Outlined.PlayArrow,
            Icons.Filled.PlayArrow
        )

    object Exercises :
        BottomNavigationScreens(
            TabRootScreen.ExercisesTab.route,
            "Exercises",
            Icons.Outlined.FitnessCenter, Icons.Filled.FitnessCenter
        )

    object More :
        BottomNavigationScreens(
            TabRootScreen.MoreTab.route,
            "More",
            Icons.Outlined.Menu,
            Icons.Filled.Menu
        )
}
