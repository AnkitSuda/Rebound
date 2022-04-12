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

package com.ankitsuda.navigation

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.*
import com.google.accompanist.navigation.animation.composable
import com.ankitsuda.base.utils.toEpochMillis
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.bottomSheet
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.util.*

const val QUERY_KEY = "query"
const val SEARCH_BACKENDS_KEY = "backends"
const val ARTIST_ID_KEY = "artist_id"
const val ALBUM_ID_KEY = "album_id"
const val PLAYLIST_ID_KEY = "playlist_id"
const val DATE_KEY = "date"
const val SELECTED_DATE_KEY = "selected_date"
const val WORKOUT_ID_KEY = "workout_id"
const val WORKOUT_TEMPLATE_ID_KEY = "workout_template_id"
const val PART_ID_KEY = "part_id"
const val LOG_ID_KEY = "log_id"

interface Screen {
    val route: String
}

val ROOT_SCREENS = listOf(
    RootScreen.HomeTab,
    RootScreen.HistoryTab,
    RootScreen.WorkoutTab,
    RootScreen.ExercisesTab,
    RootScreen.MoreTab
)

sealed class RootScreen(
    override val route: String,
    val startScreen: LeafScreen,
    val arguments: List<NamedNavArgument> = emptyList(),
    val deepLinks: List<NavDeepLink> = emptyList(),
) : Screen {
    object HomeTab : RootScreen("home_tab", LeafScreen.HomeTab())
    object HistoryTab : RootScreen("history_tab", LeafScreen.HistoryTab())
    object WorkoutTab : RootScreen("workout_tab", LeafScreen.WorkoutTab())
    object ExercisesTab : RootScreen("exercises_tab", LeafScreen.ExercisesTab())
    object MoreTab : RootScreen("more_tab", LeafScreen.MoreTab())
}

sealed class LeafScreen(
    override val route: String,
    open val root: RootScreen? = null,
    protected open val path: String = "",
    val arguments: List<NamedNavArgument> = emptyList(),
    val deepLinks: List<NavDeepLink> = emptyList(),
) : Screen {


    fun createRoute(root: RootScreen? = null) = when (val rootPath = (root ?: this.root)?.route) {
        is String -> "$rootPath/$route"
        else -> route
    }

    data class HomeTab(override val route: String = "home_tab") :
        LeafScreen(route, RootScreen.HomeTab)

    data class HistoryTab(override val route: String = "history_tab") :
        LeafScreen(route, RootScreen.HistoryTab)

    data class WorkoutTab(override val route: String = "workout_tab") :
        LeafScreen(route, RootScreen.WorkoutTab)

    data class ExercisesTab(override val route: String = "exercises_tab") :
        LeafScreen(route, RootScreen.ExercisesTab)

    data class MoreTab(override val route: String = "more_tab") :
        LeafScreen(route, RootScreen.MoreTab)


    data class Home(override val route: String = "home") : LeafScreen(route)
    data class History(override val route: String = "history?$DATE_KEY={$DATE_KEY}") :
        LeafScreen(route = route,
            arguments = listOf(
                navArgument(DATE_KEY) {
                    nullable = true
                    type = NavType.StringType
                }
            )) {
        fun createRoute(date: Date) =
            "history?$DATE_KEY=${date.time}"
    }

    data class Workout(override val route: String = "workout") : LeafScreen(route)
    data class Exercises(override val route: String = "exercises") : LeafScreen(route)
    data class More(override val route: String = "more") : LeafScreen(route)

    data class ExercisesBottomSheet(override val route: String = "exercises_bottom_sheet") :
        LeafScreen(route)


    data class Measure(override val route: String = "measure") : LeafScreen(route)
    data class PartMeasurements(override val route: String = "part_measurements/{${PART_ID_KEY}}") :
        LeafScreen(route) {
        companion object {
            fun createRoute(partId: String) = "part_measurements/$partId"
        }
    }

    data class Settings(override val route: String = "settings") : LeafScreen(route)
    data class Personalization(override val route: String = "personalization") : LeafScreen(route)
    data class MainColorsPersonalization(override val route: String = "personalization/main_colors") :
        LeafScreen(route)

    data class ShapesPersonalization(override val route: String = "personalization/shapes") :
        LeafScreen(route)

    data class CardsPersonalization(override val route: String = "personalization/cards") :
        LeafScreen(route)

    data class TopBarPersonalization(override val route: String = "personalization/top_bar") :
        LeafScreen(route)

    data class BottomBarPersonalization(override val route: String = "personalization/bottom_bar") :
        LeafScreen(route)

    data class ChartsPersonalization(override val route: String = "personalization/charts") :
        LeafScreen(route)

    data class ColorPickerDemo(override val route: String = "personalization/color_picker_demo") :
        LeafScreen(route)

    data class CreateExercise(override val route: String = "create_exercise") : LeafScreen(route)
    data class AddPartMeasurement(override val route: String = "add_part_measurement?${PART_ID_KEY}={${PART_ID_KEY}}&${LOG_ID_KEY}={${LOG_ID_KEY}}") :
        LeafScreen(
            route = route,
            arguments = listOf(
                navArgument(PART_ID_KEY) {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument(LOG_ID_KEY) {
                    type = NavType.StringType
                    nullable = true
                },
            ),
        ) {
        companion object {
            fun createRoute(partId: String? = null, logId: String? = null): String {
                var str = "add_part_measurement?"
                partId?.let {
                    str = "$str${PART_ID_KEY}=$it&"
                }
                logId?.let {
                    str = "$str${LOG_ID_KEY}=$it"
                }
                return str;
            }
        }
    }

    data class Calendar(override val route: String = "calendar/{$SELECTED_DATE_KEY}") :
        LeafScreen(route) {
        companion object {

            fun createRoute(selectedDate: LocalDate) = "calendar/${selectedDate.toEpochMillis()}"
            fun createRoute(selectedDate: Date) = "calendar/${selectedDate.time}"
            fun createRoute(selectedDate: Long) = "calendar/${selectedDate}"
        }
    }

    data class Session(
        override val route: String = "session/{${WORKOUT_ID_KEY}}",
    ) : LeafScreen(
        route = route,
        arguments = listOf(
            navArgument(WORKOUT_ID_KEY) {
                type = NavType.StringType
            }
        ),
    ) {
        companion object {

            fun createRoute(workoutId: String) = "session/$workoutId"

        }
    }

    data class ExerciseDetails(override val route: String = "exercises/{exerciseId}") :
        LeafScreen(route) {
        companion object {
            fun createRoute(exerciseId: String) = "exercises/$exerciseId"
        }
    }

    data class WorkoutTemplatePreview(override val route: String = "workout_template/{${WORKOUT_TEMPLATE_ID_KEY}}/preview") :
        LeafScreen(route) {
        companion object {
            fun createRoute(templateId: String) = "workout_template/$templateId/preview"
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.composableScreen(
    screen: LeafScreen,
    content: @Composable AnimatedVisibilityScope.(NavBackStackEntry) -> Unit
) {
    composable(
        route = screen.createRoute(),
        arguments = screen.arguments,
        deepLinks = screen.deepLinks,
        content = content
    )
}

@OptIn(ExperimentalMaterialNavigationApi::class)
fun NavGraphBuilder.bottomSheetScreen(
    screen: LeafScreen,
    content: @Composable ColumnScope.(NavBackStackEntry) -> Unit
) =
    bottomSheet(screen.createRoute(), screen.arguments, screen.deepLinks, content)

// https://stackoverflow.com/a/64961032/2897341
@Composable
inline fun <reified VM : ViewModel> NavBackStackEntry.scopedViewModel(navController: NavController): VM {
    val parentId = destination.parent!!.id
    val parentBackStackEntry = navController.getBackStackEntry(parentId)
    return hiltViewModel(parentBackStackEntry)
}
