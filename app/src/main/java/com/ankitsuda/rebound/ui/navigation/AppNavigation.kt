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
import com.ankitsuda.rebound.ui.screens.exercise.ExerciseDetailScreen
import com.ankitsuda.rebound.ui.screens.exercises.ExercisesScreen
import com.ankitsuda.rebound.ui.screens.calendar.CalendarScreen
import com.ankitsuda.rebound.ui.screens.create_exercise.CreateExerciseScreen
import com.ankitsuda.rebound.ui.screens.history.HistoryScreen
import com.ankitsuda.rebound.ui.screens.home.HomeScreen
import com.ankitsuda.rebound.ui.screens.main_screen.BottomNavigationScreens
import com.ankitsuda.rebound.ui.screens.measure.MeasureScreen
import com.ankitsuda.rebound.ui.screens.more.MoreScreen
import com.ankitsuda.rebound.ui.screens.part_measurements.AddPartMeasurementBottomSheet
import com.ankitsuda.rebound.ui.screens.part_measurements.PartMeasurementsScreen
import com.ankitsuda.rebound.ui.settings.personalization.ColorPickerDemoScreen
import com.ankitsuda.rebound.ui.settings.personalization.PersonalizationScreen
import com.ankitsuda.rebound.ui.settings.personalization.botom_bar.BottomBarPersonalizationScreen
import com.ankitsuda.rebound.ui.settings.personalization.card.CardPersonalizationScreen
import com.ankitsuda.rebound.ui.settings.personalization.charts.ChartsPersonalizationScreen
import com.ankitsuda.rebound.ui.settings.personalization.main_colors.MainColorsPersonalizationScreen
import com.ankitsuda.rebound.ui.settings.personalization.shapes.ShapesPersonalizationScreen
import com.ankitsuda.rebound.ui.settings.personalization.top_bar.TopBarPersonalizationScreen
import com.ankitsuda.rebound.ui.screens.session.SessionScreen
import com.ankitsuda.rebound.ui.screens.settings.SettingsScreen
import com.ankitsuda.rebound.ui.screens.workout.WorkoutScreen
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
        route = RootScreen.HomeTab.route,
        startDestination = LeafScreen.Home().createRoute()
    ) {
        addHistory(navController)
        addCalendar(navController)
        addSession(navController)
    }
}

private fun NavGraphBuilder.addWorkoutRoot(navController: NavController) {
    navigation(
        route = RootScreen.HomeTab.route,
        startDestination = LeafScreen.Home().createRoute()
    ) {
        addWorkout(navController)
    }
}

private fun NavGraphBuilder.addExercisesRoot(navController: NavController) {
    navigation(
        route = RootScreen.HomeTab.route,
        startDestination = LeafScreen.Home().createRoute()
    ) {
        addExercises(navController)
        addExerciseDetail(navController)
    }
}

private fun NavGraphBuilder.addMoreRoot(navController: NavController) {
    navigation(
        route = RootScreen.HomeTab.route,
        startDestination = LeafScreen.Home().createRoute()
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
    composableScreen(LeafScreen.ExerciseDetails()) {
        MoreScreen(navController)
    }
}

private fun NavGraphBuilder.addMeasureScreen(navController: NavController) {
    composableScreen(LeafScreen.ExerciseDetails()) {
        MeasureScreen(navController)
    }
}

private fun NavGraphBuilder.addPartMeasurementsScreen(navController: NavController) {
    composableScreen(LeafScreen.ExerciseDetails()) {
        PartMeasurementsScreen(navController)
    }
}

private fun NavGraphBuilder.addSettingsScreen(navController: NavController) {
    composableScreen(LeafScreen.ExerciseDetails()) {
        SettingsScreen(navController)
    }
}

private fun NavGraphBuilder.addPersonalizationScreen(navController: NavController) {
    composableScreen(LeafScreen.ExerciseDetails()) {
        PersonalizationScreen(navController)
    }
}

private fun NavGraphBuilder.addMainColorsPersonalizationScreen(navController: NavController) {
    composableScreen(LeafScreen.ExerciseDetails()) {
        MainColorsPersonalizationScreen(navController)
    }
}

private fun NavGraphBuilder.addShapesPersonalizationScreen(navController: NavController) {
    composableScreen(LeafScreen.ExerciseDetails()) {
        ShapesPersonalizationScreen(navController)
    }
}

private fun NavGraphBuilder.addCardPersonalizationScreen(navController: NavController) {
    composableScreen(LeafScreen.ExerciseDetails()) {
        CardPersonalizationScreen(navController)
    }
}

private fun NavGraphBuilder.addTopBarPersonalizationScreen(navController: NavController) {
    composableScreen(LeafScreen.ExerciseDetails()) {
        TopBarPersonalizationScreen(navController)
    }
}

private fun NavGraphBuilder.addBottomBarPersonalizationScreen(navController: NavController) {
    composableScreen(LeafScreen.ExerciseDetails()) {
        BottomBarPersonalizationScreen(navController)
    }
}

private fun NavGraphBuilder.addChartsPersonalizationScreen(navController: NavController) {
    composableScreen(LeafScreen.ExerciseDetails()) {
        ChartsPersonalizationScreen(navController)
    }
}

private fun NavGraphBuilder.addColorPickerDemoScreen(navController: NavController) {
    composableScreen(LeafScreen.ExerciseDetails()) {
        ColorPickerDemoScreen()
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
            rootScreens.firstOrNull { rs -> destination.hierarchy.any { it.route == rs.route } }?.let {
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
