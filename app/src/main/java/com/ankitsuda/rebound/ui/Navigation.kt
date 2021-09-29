package com.ankitsuda.rebound.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
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
import com.ankitsuda.rebound.ui.screens.personalization.ColorPickerDemoScreen
import com.ankitsuda.rebound.ui.screens.personalization.PersonalizationScreen
import com.ankitsuda.rebound.ui.screens.personalization.botom_bar.BottomBarPersonalizationScreen
import com.ankitsuda.rebound.ui.screens.personalization.card.CardPersonalizationScreen
import com.ankitsuda.rebound.ui.screens.personalization.charts.ChartsPersonalizationScreen
import com.ankitsuda.rebound.ui.screens.personalization.main_colors.MainColorsPersonalizationScreen
import com.ankitsuda.rebound.ui.screens.personalization.shapes.ShapesPersonalizationScreen
import com.ankitsuda.rebound.ui.screens.personalization.top_bar.TopBarPersonalizationScreen
import com.ankitsuda.rebound.ui.screens.session.SessionScreen
import com.ankitsuda.rebound.ui.screens.settings.SettingsScreen
import com.ankitsuda.rebound.ui.screens.workout.WorkoutScreen
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.bottomSheet
import kotlinx.coroutines.DelicateCoroutinesApi


@OptIn(DelicateCoroutinesApi::class, ExperimentalMaterialNavigationApi::class)
@Composable
fun MainScreenNavigationConfigurations(navController: NavHostController) {

    NavHost(navController, startDestination = BottomNavigationScreens.Home.route) {
        // Bottom Nav

        navigation(
            route = Route.HomeTab.route,
            startDestination = Route.Home.route
        ) {
            composable(Route.Home.route) {
                HomeScreen(navController)
            }
        }

        navigation(
            route = Route.HistoryTab.route,
            startDestination = Route.History.route
        ) {
            composable(Route.History.route) {
                HistoryScreen(navController)
            }
            composable(Route.Calendar.route) {
                CalendarScreen(navController)
            }
            composable(Route.Session.route) {
                SessionScreen(navController)
            }
        }

        navigation(
            startDestination = Route.Workout.route,
            route = Route.WorkoutTab.route
        ) {
            composable(Route.Workout.route) {
                WorkoutScreen(navController)
            }
        }

        navigation(
            startDestination = Route.Exercises.route,
            route = Route.ExercisesTab.route
        ) {
            composable(Route.Exercises.route) {
                ExercisesScreen(navController, isBottomSheet = false)
            }

            composable(Route.ExerciseDetails.route) {
                ExerciseDetailScreen(navController)
            }

//            composable(Route.CreateExercise.route) {
//                CreateExerciseScreen(navController)
//            }
        }

        navigation(
            route = Route.MoreTab.route,
            startDestination = Route.More.route
        ) {
            composable(Route.More.route) {
                MoreScreen(navController)
            }
            composable(Route.Measure.route) {
                MeasureScreen(navController)
            }
            composable(Route.PartMeasurements.route) {
                PartMeasurementsScreen(navController)
            }
            composable(Route.Settings.route) {
                SettingsScreen(navController)
            }
            composable(Route.Personalization.route) {
                PersonalizationScreen(navController)
            }
            composable(Route.MainColorsPersonalization.route) {
                MainColorsPersonalizationScreen(navController)
            }
            composable(Route.ShapesPersonalization.route) {
                ShapesPersonalizationScreen(navController)
            }
            composable(Route.CardsPersonalization.route) {
                CardPersonalizationScreen(navController)
            }
            composable(Route.TopBarPersonalization.route) {
                TopBarPersonalizationScreen(navController)
            }
            composable(Route.BottomBarPersonalization.route) {
                BottomBarPersonalizationScreen(navController)
            }
            composable(Route.ChartsPersonalization.route) {
                ChartsPersonalizationScreen(navController)
            }
            composable(Route.ColorPickerDemo.route) {
                ColorPickerDemoScreen()
            }


        }

        bottomSheet(Route.CreateExercise.route) {
            CreateExerciseScreen(navController)
        }
        bottomSheet(Route.ExercisesBottomSheet.route) {
            ExercisesScreen(navController, isBottomSheet = true)
        }
        bottomSheet(Route.AddPartMeasurement.route) {
            AddPartMeasurementBottomSheet(navController)
        }
    }
}