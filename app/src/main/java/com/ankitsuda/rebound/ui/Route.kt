package com.ankitsuda.rebound.ui

sealed class Route(val route: String) {

    // Tabs
    object HomeTab : Route("home_tab")
    object HistoryTab : Route("history_tab")
    object WorkoutTab : Route("workout_tab")
    object ExercisesTab : Route("exercises_tab")
    object MoreTab : Route("more_tab")

    object Home : Route("home")
    object History : Route("history")
    object Workout : Route("workout")
    object Exercises : Route("exercises")
    object More : Route("more")

    object Calendar : Route("calendar")

    object ExerciseDetails : Route("exercises/{exerciseId}") {
        fun createRoute(exerciseId: Long) = "exercises/$exerciseId"
    }
}