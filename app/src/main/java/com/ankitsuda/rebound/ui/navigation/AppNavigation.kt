package com.ankitsuda.rebound.ui.navigation

import androidx.compose.runtime.*
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.navigation
import com.ankitsuda.common.compose.collectEvent
import com.ankitsuda.navigation.*
import com.ankitsuda.rebound.ui.measure.MeasureScreen
import com.ankitsuda.rebound.ui.exercise_details.ExerciseDetailScreen
import com.ankitsuda.rebound.ui.exercises.ExercisesScreen
import com.ankitsuda.rebound.ui.calendar.CalendarScreen
import com.ankitsuda.rebound.ui.create_exercise.CreateExerciseScreen
import com.ankitsuda.rebound.ui.history.HistoryScreen
import com.ankitsuda.rebound.ui.home.HomeScreen
import com.ankitsuda.rebound.ui.more.MoreScreen
import com.ankitsuda.rebound.ui.settings.personalization.ColorPickerDemoScreen
import com.ankitsuda.rebound.ui.settings.personalization.PersonalizationScreen
import com.ankitsuda.rebound.ui.settings.personalization.botom_bar.BottomBarPersonalizationScreen
import com.ankitsuda.rebound.ui.settings.personalization.card.CardPersonalizationScreen
import com.ankitsuda.rebound.ui.settings.personalization.charts.ChartsPersonalizationScreen
import com.ankitsuda.rebound.ui.settings.personalization.main_colors.MainColorsPersonalizationScreen
import com.ankitsuda.rebound.ui.settings.personalization.shapes.ShapesPersonalizationScreen
import com.ankitsuda.rebound.ui.settings.personalization.top_bar.TopBarPersonalizationScreen
import com.ankitsuda.rebound.ui.measure.part.add_sheet.AddPartMeasurementBottomSheet
import com.ankitsuda.rebound.ui.measure.part.overview.PartMeasurementsScreen
import com.ankitsuda.rebound.ui.settings.SettingsScreen
import com.ankitsuda.rebound.ui.workout.WorkoutScreen
import com.ankitsuda.rebound.ui.workout_details.SessionScreen
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import kotlinx.coroutines.InternalCoroutinesApi

@OptIn(InternalCoroutinesApi::class, ExperimentalMaterialNavigationApi::class)
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

    NavHost(
        navController = navController,
        startDestination = RootScreen.HomeTab.route
    ) {
        addHomeRoot(navController)
        addHistoryRoot(navController)
        addWorkoutRoot(navController)
        addExercisesRoot(navController)
        addMoreRoot(navController)
    }
}

private fun NavGraphBuilder.addHomeRoot(navController: NavController) {
    navigation(
        route = RootScreen.HomeTab.route,
        startDestination = LeafScreen.Home().createRoute()
    ) {
        addHome(navController)
    }
}

private fun NavGraphBuilder.addHistoryRoot(navController: NavController) {
    navigation(
        route = RootScreen.HistoryTab.route,
        startDestination = LeafScreen.History().createRoute()
    ) {
        addHistory(navController)
        addCalendar(navController)
        addSession(navController)
    }
}

private fun NavGraphBuilder.addWorkoutRoot(navController: NavController) {
    navigation(
        route = RootScreen.WorkoutTab.route,
        startDestination = LeafScreen.Workout().createRoute()
    ) {
        addWorkout(navController)
    }
}

private fun NavGraphBuilder.addExercisesRoot(navController: NavController) {
    navigation(
        route = RootScreen.ExercisesTab.route,
        startDestination = LeafScreen.Exercises().createRoute()
    ) {
        addExercises(navController)
        addExerciseDetail(navController)
        addCreateExerciseBottomSheet(navController)
    }
}

private fun NavGraphBuilder.addMoreRoot(navController: NavController) {
    navigation(
        route = RootScreen.MoreTab.route,
        startDestination = LeafScreen.More().createRoute()
    ) {
        addMoreScreen(navController)
        addMeasureScreen(navController)
        addPartMeasurementsScreen(navController)
        addSettingsScreen(navController)
        addPersonalizationScreen(navController)
        addMainColorsPersonalizationScreen(navController)
        addShapesPersonalizationScreen(navController)
        addCardPersonalizationScreen(navController)
        addTopBarPersonalizationScreen(navController)
        addBottomBarPersonalizationScreen(navController)
        addChartsPersonalizationScreen(navController)
        addColorPickerDemoScreen(navController)

        addAddPartMeasurementBottomSheet(navController)
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
        SessionScreen(navController)
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

private fun NavGraphBuilder.addExerciseDetail(navController: NavController) {
    composableScreen(LeafScreen.ExerciseDetails()) {
        ExerciseDetailScreen(navController)
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

private fun NavGraphBuilder.addColorPickerDemoScreen(navController: NavController) {
    composableScreen(LeafScreen.ColorPickerDemo()) {
        ColorPickerDemoScreen()
    }
}

private fun NavGraphBuilder.addCreateExerciseBottomSheet(navController: NavController) {
    bottomSheetScreen(LeafScreen.CreateExercise()) {
        CreateExerciseScreen()
    }
}

private fun NavGraphBuilder.addAddPartMeasurementBottomSheet(navController: NavController) {
    bottomSheetScreen(LeafScreen.AddPartMeasurement()) {
        AddPartMeasurementBottomSheet(navController)
    }
}


/**
 * Adds an [NavController.OnDestinationChangedListener] to this [NavController] and updates the
 * returned [State] which is updated as the destination changes.
 */
@Stable
@Composable
internal fun NavController.currentScreenAsState(): State<RootScreen> {
    val selectedItem = remember { mutableStateOf<RootScreen>(RootScreen.HomeTab) }
    val rootScreens = ROOT_SCREENS
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
