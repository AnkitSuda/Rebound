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

package com.ankitsuda.rebound.ui.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.*
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.ankitsuda.common.compose.collectEvent
import com.ankitsuda.navigation.*
import com.ankitsuda.rebound.ui.calendar.CalendarScreen
import com.ankitsuda.rebound.ui.components.workouteditor.supersetselector.SupersetSelectorBottomSheet
import com.ankitsuda.rebound.ui.create_exercise.CreateExerciseScreen
import com.ankitsuda.rebound.ui.customizeplates.CustomizePlatesScreen
import com.ankitsuda.rebound.ui.customizeplates.edit.PlateEditBottomSheet
import com.ankitsuda.rebound.ui.exercise_details.ExerciseDetailScreen
import com.ankitsuda.rebound.ui.exercises.ExercisesScreen
import com.ankitsuda.rebound.ui.history.HistoryScreen
import com.ankitsuda.rebound.ui.home.HomeScreen
import com.ankitsuda.rebound.ui.keyboard.ReboundSetKeyboardDemoScreen
import com.ankitsuda.rebound.ui.measure.MeasureScreen
import com.ankitsuda.rebound.ui.measure.part.add_sheet.AddPartMeasurementBottomSheet
import com.ankitsuda.rebound.ui.measure.part.overview.PartMeasurementsScreen
import com.ankitsuda.rebound.ui.more.MoreScreen
import com.ankitsuda.rebound.ui.resttimer.RestTimerScreen
import com.ankitsuda.rebound.ui.settings.SettingsScreen
import com.ankitsuda.rebound.ui.settings.personalization.ColorPickerDemoScreen
import com.ankitsuda.rebound.ui.settings.personalization.PersonalizationScreen
import com.ankitsuda.rebound.ui.settings.personalization.botom_bar.BottomBarPersonalizationScreen
import com.ankitsuda.rebound.ui.settings.personalization.card.CardPersonalizationScreen
import com.ankitsuda.rebound.ui.settings.personalization.charts.ChartsPersonalizationScreen
import com.ankitsuda.rebound.ui.settings.personalization.keyboard.KeyboardPersonalizationScreen
import com.ankitsuda.rebound.ui.settings.personalization.main_colors.MainColorsPersonalizationScreen
import com.ankitsuda.rebound.ui.settings.personalization.presets.ThemePresetsPersonalizationScreen
import com.ankitsuda.rebound.ui.settings.personalization.shapes.ShapesPersonalizationScreen
import com.ankitsuda.rebound.ui.settings.personalization.top_bar.TopBarPersonalizationScreen
import com.ankitsuda.rebound.ui.workout.WorkoutScreen
import com.ankitsuda.rebound.ui.workout.addfolder.TemplatesFolderEditBottomSheet
import com.ankitsuda.rebound.ui.workout_details.SessionScreen
import com.ankitsuda.rebound.ui.workoutedit.WorkoutEditScreen
import com.ankitsuda.rebound.ui.workouttemplate.preview.WorkoutTemplatePreviewScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.navigation
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import kotlinx.coroutines.InternalCoroutinesApi

@OptIn(
    InternalCoroutinesApi::class,
    ExperimentalMaterialNavigationApi::class,
    androidx.compose.animation.ExperimentalAnimationApi::class
)
@Composable
internal fun AppNavigation(
    navController: NavHostController,
    navigator: Navigator = LocalNavigator.current,
) {
    collectEvent(navigator.queue) { event ->
        when (event) {
            is NavigationEvent.Destination -> {
                // switch tabs first because of a bug in navigation that doesn't allow
                // changing tabs when destination is opened from a different tab
                event.root?.let {
                    navController.navigate(it) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
                navController.navigate(event.route)
            }
            is NavigationEvent.Back -> navController.navigateUp()
            else -> Unit
        }
    }

    val animDuration = 250

    AnimatedNavHost(
        navController = navController,
        startDestination = TabRootScreen.HomeTab.route,
        enterTransition = {
            fadeIn(animationSpec = tween(animDuration)) + scaleIn(
                animationSpec = tween(animDuration),
                initialScale = 0.75f
            )
        },
        exitTransition = {
            fadeOut(animationSpec = tween(animDuration))
        },
        popEnterTransition = {
            fadeIn(animationSpec = tween(animDuration / 1)) + scaleIn(
                animationSpec = tween(animDuration / 1),
                initialScale = 0.9f
            )
        },
        popExitTransition = {
            fadeOut(animationSpec = tween(animDuration)) + scaleOut(
                animationSpec = tween(animDuration),
                targetScale = 0.75f
            )
        }
    ) {
        addHomeRoot(navController)
        addHistoryRoot(navController)
        addWorkoutRoot(navController)
        addExercisesRoot(navController)
        addMoreRoot(navController)

        addExercisesBottomSheet(navController)
        addRestTimer(navController)
        addSupersetSelector(navController)
    }
}

@OptIn(ExperimentalAnimationApi::class)
private fun NavGraphBuilder.addHomeRoot(navController: NavController) {
    navigation(
        route = TabRootScreen.HomeTab.route,
        startDestination = LeafScreen.Home().createRoute()
    ) {
        addHome(navController)
    }
}

@OptIn(ExperimentalAnimationApi::class)
private fun NavGraphBuilder.addHistoryRoot(navController: NavController) {
    navigation(
        route = TabRootScreen.HistoryTab.route,
        startDestination = LeafScreen.History().createRoute()
    ) {
        addHistory(navController)
        addCalendar(navController)
        addSession(navController)
        addWorkoutEdit(navController, TabRootScreen.HistoryTab)
    }
}

@OptIn(ExperimentalAnimationApi::class)
private fun NavGraphBuilder.addWorkoutRoot(navController: NavController) {
    navigation(
        route = TabRootScreen.WorkoutTab.route,
        startDestination = LeafScreen.Workout().createRoute()
    ) {
        addWorkout(navController)
        addWorkoutTemplatePreview(navController)
        addExerciseDetail(navController, TabRootScreen.WorkoutTab)
        addWorkoutEdit(navController, TabRootScreen.WorkoutTab)
        addTemplatesFolderEditBottomSheet(navController);
    }
}

@OptIn(ExperimentalAnimationApi::class)
private fun NavGraphBuilder.addExercisesRoot(navController: NavController) {
    navigation(
        route = TabRootScreen.ExercisesTab.route,
        startDestination = LeafScreen.Exercises().createRoute()
    ) {
        addExercises(navController)
        addExerciseDetail(navController, TabRootScreen.ExercisesTab)
        addCreateExerciseBottomSheet(navController)
    }
}

@OptIn(ExperimentalAnimationApi::class)
private fun NavGraphBuilder.addMoreRoot(navController: NavController) {
    navigation(
        route = TabRootScreen.MoreTab.route,
        startDestination = LeafScreen.More().createRoute()
    ) {
        addMoreScreen(navController)
        addMeasureScreen(navController)
        addPartMeasurementsScreen(navController)
        addSettingsScreen(navController)
        addCustomizePlatesScreen(navController)
        addPersonalizationScreen(navController)
        addMainColorsPersonalizationScreen(navController)
        addShapesPersonalizationScreen(navController)
        addCardPersonalizationScreen(navController)
        addTopBarPersonalizationScreen(navController)
        addBottomBarPersonalizationScreen(navController)
        addChartsPersonalizationScreen(navController)
        addKeyboardPersonalizationScreen(navController)
        addThemePresetsPersonalizationScreen(navController)
        addColorPickerDemoScreen(navController)
        addReboundSetKeyboardDemo(navController)

        addAddPartMeasurementBottomSheet(navController)
        addPlateEditBottomSheet(navController)
    }
}


private fun NavGraphBuilder.addHome(navController: NavController) {
    composableScreen(LeafScreen.Home()) {
        HomeScreen()
    }
}

private fun NavGraphBuilder.addHistory(navController: NavController) {
    composableScreen(LeafScreen.History()) {
        HistoryScreen(navController)
    }
}

private fun NavGraphBuilder.addCalendar(navController: NavController) {
    composableScreen(LeafScreen.Calendar()) {
        CalendarScreen(navController)
    }
}

private fun NavGraphBuilder.addSession(navController: NavController) {
    composableScreen(LeafScreen.Session()) {
        SessionScreen()
    }
}

private fun NavGraphBuilder.addWorkoutEdit(navController: NavController, root: TabRootScreen) {
    composableScreen(LeafScreen.WorkoutEdit(root = root)) {
        WorkoutEditScreen(navController)
    }
}


private fun NavGraphBuilder.addWorkout(navController: NavController) {
    composableScreen(LeafScreen.Workout()) {
        WorkoutScreen(navController)
    }
}

private fun NavGraphBuilder.addExercises(navController: NavController) {
    composableScreen(LeafScreen.Exercises()) {
        ExercisesScreen(navController, false)
    }
}

private fun NavGraphBuilder.addExerciseDetail(navController: NavController, root: TabRootScreen) {
    composableScreen(LeafScreen.ExerciseDetails(root = root)) {
        ExerciseDetailScreen(navController)
    }
}

private fun NavGraphBuilder.addWorkoutTemplatePreview(navController: NavController) {
    composableScreen(LeafScreen.WorkoutTemplatePreview()) {
        WorkoutTemplatePreviewScreen()
    }
}

private fun NavGraphBuilder.addMoreScreen(navController: NavController) {
    composableScreen(LeafScreen.More()) {
        MoreScreen(navController)
    }
}

private fun NavGraphBuilder.addMeasureScreen(navController: NavController) {
    composableScreen(LeafScreen.Measure()) {
        MeasureScreen(navController)
    }
}

private fun NavGraphBuilder.addPartMeasurementsScreen(navController: NavController) {
    composableScreen(LeafScreen.PartMeasurements()) {
        PartMeasurementsScreen(navController)
    }
}

private fun NavGraphBuilder.addSettingsScreen(navController: NavController) {
    composableScreen(LeafScreen.Settings()) {
        SettingsScreen(navController)
    }
}

private fun NavGraphBuilder.addCustomizePlatesScreen(navController: NavController) {
    composableScreen(LeafScreen.Plates()) {
        CustomizePlatesScreen()
    }
}

private fun NavGraphBuilder.addPersonalizationScreen(navController: NavController) {
    composableScreen(LeafScreen.Personalization()) {
        PersonalizationScreen(navController)
    }
}

private fun NavGraphBuilder.addMainColorsPersonalizationScreen(navController: NavController) {
    composableScreen(LeafScreen.MainColorsPersonalization()) {
        MainColorsPersonalizationScreen(navController)
    }
}

private fun NavGraphBuilder.addShapesPersonalizationScreen(navController: NavController) {
    composableScreen(LeafScreen.ShapesPersonalization()) {
        ShapesPersonalizationScreen(navController)
    }
}

private fun NavGraphBuilder.addCardPersonalizationScreen(navController: NavController) {
    composableScreen(LeafScreen.CardsPersonalization()) {
        CardPersonalizationScreen(navController)
    }
}

private fun NavGraphBuilder.addTopBarPersonalizationScreen(navController: NavController) {
    composableScreen(LeafScreen.TopBarPersonalization()) {
        TopBarPersonalizationScreen(navController)
    }
}

private fun NavGraphBuilder.addBottomBarPersonalizationScreen(navController: NavController) {
    composableScreen(LeafScreen.BottomBarPersonalization()) {
        BottomBarPersonalizationScreen(navController)
    }
}

private fun NavGraphBuilder.addChartsPersonalizationScreen(navController: NavController) {
    composableScreen(LeafScreen.ChartsPersonalization()) {
        ChartsPersonalizationScreen(navController)
    }
}

private fun NavGraphBuilder.addKeyboardPersonalizationScreen(navController: NavController) {
    composableScreen(LeafScreen.KeyboardPersonalization()) {
        KeyboardPersonalizationScreen(navController)
    }
}

private fun NavGraphBuilder.addThemePresetsPersonalizationScreen(navController: NavController) {
    composableScreen(LeafScreen.ThemePresetsPersonalization()) {
        ThemePresetsPersonalizationScreen()
    }
}

private fun NavGraphBuilder.addColorPickerDemoScreen(navController: NavController) {
    composableScreen(LeafScreen.ColorPickerDemo()) {
        ColorPickerDemoScreen()
    }
}

private fun NavGraphBuilder.addReboundSetKeyboardDemo(navController: NavController) {
    composableScreen(LeafScreen.ReboundSetKeyboardDemo()) {
        ReboundSetKeyboardDemoScreen()
    }
}

private fun NavGraphBuilder.addCreateExerciseBottomSheet(navController: NavController) {
    bottomSheetScreen(LeafScreen.CreateExercise()) {
        CreateExerciseScreen()
    }
}

private fun NavGraphBuilder.addExercisesBottomSheet(navController: NavController) {
    bottomSheetScreen(LeafScreen.ExercisesBottomSheet()) {
        ExercisesScreen(navController = navController, isBottomSheet = true)
    }
}

private fun NavGraphBuilder.addAddPartMeasurementBottomSheet(navController: NavController) {
    bottomSheetScreen(LeafScreen.AddPartMeasurement()) {
        AddPartMeasurementBottomSheet(navController)
    }
}

private fun NavGraphBuilder.addPlateEditBottomSheet(navController: NavController) {
    bottomSheetScreen(LeafScreen.PlateEdit()) {
        PlateEditBottomSheet()
    }
}

private fun NavGraphBuilder.addTemplatesFolderEditBottomSheet(navController: NavController) {
    bottomSheetScreen(LeafScreen.TemplatesFolderEdit()) {
        TemplatesFolderEditBottomSheet()
    }
}

private fun NavGraphBuilder.addRestTimer(navController: NavController) {
    bottomSheetScreen(LeafScreen.RestTimer()) {
        RestTimerScreen()
    }
}

private fun NavGraphBuilder.addSupersetSelector(navController: NavController) {
    bottomSheetScreen(LeafScreen.SupersetSelector()) {
        SupersetSelectorBottomSheet(navController)
    }
}

/**
 * Adds an [NavController.OnDestinationChangedListener] to this [NavController] and updates the
 * returned [State] which is updated as the destination changes.
 */
@Stable
@Composable
internal fun NavController.currentScreenAsState(): State<TabRootScreen> {
    val selectedItem = remember { mutableStateOf<TabRootScreen>(TabRootScreen.HomeTab) }
    val rootScreens = TAB_ROOT_SCREENS
    DisposableEffect(this) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            rootScreens.firstOrNull { rs -> destination.hierarchy.any { it.route == rs.route } }
                ?.let {
                    selectedItem.value = it
                }
        }
        addOnDestinationChangedListener(listener)

        onDispose {
            removeOnDestinationChangedListener(listener)
        }
    }

    return selectedItem
}
