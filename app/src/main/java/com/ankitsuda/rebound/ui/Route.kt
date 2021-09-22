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

    object Measure : Route("measure")
    object PartMeasurements : Route("part_measurements/{partId}") {
        fun createRoute(partId: String) = "part_measurements/$partId"
    }

    object Settings : Route("settings")
    object Personalization : Route("personalization")
    object MainColorsPersonalization : Route("personalization/main_colors")
    object ShapesPersonalization : Route("personalization/shapes")
    object CardsPersonalization : Route("personalization/cards")
    object TopBarPersonalization : Route("personalization/top_bar")
    object BottomBarPersonalization : Route("personalization/bottom_bar")
    object ColorPickerDemo : Route("personalization/color_picker_demo")

//    object CreateExercise : Route("create_exercise")

    object Calendar : Route("calendar/{selectedDate}") {
        fun createRoute(selectedDate: Date) = "calendar/${selectedDate.time}"
        fun createRoute(selectedDate: Long) = "calendar/${selectedDate}"
    }

    object Session : Route("session/{sessionId}") {
        fun createRoute(sessionId: Long) = "session/$sessionId"
    }

    object ExerciseDetails : Route("exercises/{exerciseId}") {
        fun createRoute(exerciseId: Long) = "exercises/$exerciseId"
    }
}