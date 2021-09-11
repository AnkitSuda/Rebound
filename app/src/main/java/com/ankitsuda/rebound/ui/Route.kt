package com.ankitsuda.rebound.ui

import com.ankitsuda.rebound.utils.CalendarItem
import com.ankitsuda.rebound.utils.CalendarUtils
import java.text.SimpleDateFormat
import java.util.*

sealed class Route(val route: String) {

    // Tabs
    object HomeTab : Route("home_tab")
    object HistoryTab : Route("history_tab")
    object WorkoutTab : Route("workout_tab")
    object ExercisesTab : Route("exercises_tab")
    object MoreTab : Route("more_tab")

    object Home : Route("home")
    object History : Route("history/{date}") {
        fun createRoute(date: Date) =
            "history/${date.time}"
    }

    object Workout : Route("workout")
    object Exercises : Route("exercises")
    object More : Route("more")

    object Calendar : Route("calendar/{selectedDate}") {
        fun createRoute(selectedDate: Date) = "calendar/${selectedDate.time}"
        fun createRoute(selectedDate: Long) = "calendar/${selectedDate}"
    }

    object ExerciseDetails : Route("exercises/{exerciseId}") {
        fun createRoute(exerciseId: Long) = "exercises/$exerciseId"
    }
}