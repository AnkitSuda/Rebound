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

    object ExercisesBottomSheet : Route("exercises_bottom_sheet")


    object Measure : Route("measure")
    object PartMeasurements : Route("part_measurements/{partId}") {

        fun createRoute(partId: Long) = "part_measurements/$partId"
    }

    object Settings : Route("settings")
    object Personalization : Route("personalization")
    object MainColorsPersonalization : Route("personalization/main_colors")
    object ShapesPersonalization : Route("personalization/shapes")
    object CardsPersonalization : Route("personalization/cards")
    object TopBarPersonalization : Route("personalization/top_bar")
    object BottomBarPersonalization : Route("personalization/bottom_bar")
    object ChartsPersonalization : Route("personalization/charts")
    object ColorPickerDemo : Route("personalization/color_picker_demo")

    object CreateExercise : Route("create_exercise")
    object AddPartMeasurement : Route("add_part_measurement?partId={partId}&logId={logId}") {
        fun createRoute(partId: Long? = null, logId: Long? = null) =
            "add_part_measurement?partId=$partId&logId=$logId"
    }

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