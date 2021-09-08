package com.ankitsuda.rebound.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.ankitsuda.rebound.ui.screens.exercise.ExerciseDetailScreen
import com.ankitsuda.rebound.ui.screens.exercises.ExercisesScreen
import com.ankitsuda.rebound.ui.screens.history.CalendarScreen
import com.ankitsuda.rebound.ui.screens.history.HistoryScreen
import com.ankitsuda.rebound.ui.screens.home.HomeScreen
import com.ankitsuda.rebound.ui.screens.main_screen.BottomNavigationScreens
import com.ankitsuda.rebound.ui.screens.more.MoreScreen
import com.ankitsuda.rebound.ui.screens.workout.WorkoutScreen


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
                ExercisesScreen(navController)
            }

            composable(Route.ExerciseDetails.route) {
                ExerciseDetailScreen(navController)
            }
        }

        navigation(
            route = Route.MoreTab.route,
            startDestination = Route.More.route
        ) {
            composable(Route.More.route) {
                MoreScreen(navController)
            }
        }
    }
}