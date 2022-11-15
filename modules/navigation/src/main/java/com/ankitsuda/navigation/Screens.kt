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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.*
import com.google.accompanist.navigation.animation.composable
import com.ankitsuda.base.utils.toEpochMillis
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.bottomSheet
import java.time.LocalDate
import java.util.*

const val DATE_KEY = "date"
const val SELECTED_DATE_KEY = "selected_date"
const val WORKOUT_ID_KEY = "workout_id"
const val EXERCISE_ID_KEY = "exercise_id"
const val WORKOUT_TEMPLATE_ID_KEY = "workout_template_id"
const val PART_ID_KEY = "part_id"
const val LOG_ID_KEY = "log_id"
const val PLATE_ID_KEY = "plate_id"
const val IS_TEMPLATE_KEY = "is_template"
const val FOLDER_ID_KEY = "folder_id"
const val SUPERSET_ID_KEY = "superset_id"
const val JUNCTION_ID_KEY = "junction_id"
const val ENTRY_ID_KEY = "entry_id"

const val RESULT_EXERCISES_SCREEN_EXERCISE_ID = "result_exercises_screen_exercise_id"
const val RESULT_SUPERSET_SELECTOR_SUPERSET_ID_KEY = "result_superset_selector_superset_id"

interface Screen {
    val route: String
}

val TAB_ROOT_SCREENS = listOf(
    TabRootScreen.HomeTab,
    TabRootScreen.HistoryTab,
    TabRootScreen.WorkoutTab,
    TabRootScreen.ExercisesTab,
    TabRootScreen.MoreTab
)

sealed class TabRootScreen(
    override val route: String,
    val startScreen: LeafScreen,
    val arguments: List<NamedNavArgument> = emptyList(),
    val deepLinks: List<NavDeepLink> = emptyList(),
) : Screen {
    object HomeTab : TabRootScreen("home_tab", LeafScreen.HomeTab())
    object HistoryTab : TabRootScreen("history_tab", LeafScreen.HistoryTab())
    object WorkoutTab : TabRootScreen("workout_tab", LeafScreen.WorkoutTab())
    object ExercisesTab : TabRootScreen("exercises_tab", LeafScreen.ExercisesTab())
    object MoreTab : TabRootScreen("more_tab", LeafScreen.MoreTab())
}

sealed class LeafScreen(
    override val route: String,
    open val root: TabRootScreen? = null,
    protected open val path: String = "",
    val arguments: List<NamedNavArgument> = emptyList(),
    val deepLinks: List<NavDeepLink> = emptyList(),
) : Screen {


    fun createRoute(root: TabRootScreen? = null) =
        when (val rootPath = (root ?: this.root)?.route) {
            is String -> "$rootPath/$route"
            else -> route
        }

    data class HomeTab(override val route: String = "home_tab") :
        LeafScreen(route, TabRootScreen.HomeTab)

    data class HistoryTab(override val route: String = "history_tab") :
        LeafScreen(route, TabRootScreen.HistoryTab)

    data class WorkoutTab(override val route: String = "workout_tab") :
        LeafScreen(route, TabRootScreen.WorkoutTab)

    data class ExercisesTab(override val route: String = "exercises_tab") :
        LeafScreen(route, TabRootScreen.ExercisesTab)

    data class MoreTab(override val route: String = "more_tab") :
        LeafScreen(route, TabRootScreen.MoreTab)


    data class Home(
        override val route: String = "home",
        override val root: TabRootScreen = TabRootScreen.HomeTab
    ) : LeafScreen(route = route, root = root)

    data class History(
        override val route: String = "history?$DATE_KEY={$DATE_KEY}",
        override val root: TabRootScreen = TabRootScreen.HistoryTab
    ) :
        LeafScreen(
            route = route,
            root = root,
            arguments = listOf(
                navArgument(DATE_KEY) {
                    nullable = true
                    type = NavType.StringType
                }
            )) {
        fun createRoute(date: Date, root: TabRootScreen = TabRootScreen.HistoryTab) =
            "${root.route}/history?$DATE_KEY=${date.time}"
    }

    data class Workout(
        override val route: String = "workout",
        override val root: TabRootScreen = TabRootScreen.WorkoutTab
    ) : LeafScreen(route, root)

    data class Exercises(
        override val route: String = "exercises",
        override val root: TabRootScreen = TabRootScreen.ExercisesTab
    ) : LeafScreen(route, root)

    data class More(
        override val route: String = "more",
        override val root: TabRootScreen = TabRootScreen.MoreTab
    ) : LeafScreen(route, root)

    data class ExercisesBottomSheet(
        override val route: String = "exercises_bottom_sheet"
    ) :
        LeafScreen(route)


    data class Measure(
        override val route: String = "measure",
        override val root: TabRootScreen = TabRootScreen.MoreTab
    ) : LeafScreen(route, root)

    data class PartMeasurements(
        override val route: String = "part_measurements/{${PART_ID_KEY}}",
        override val root: TabRootScreen = TabRootScreen.MoreTab
    ) :
        LeafScreen(route) {
        companion object {
            fun createRoute(partId: String, root: TabRootScreen = TabRootScreen.MoreTab) =
                "${root.route}/part_measurements/$partId"
        }
    }

    data class Settings(
        override val route: String = "settings",
        override val root: TabRootScreen = TabRootScreen.MoreTab
    ) : LeafScreen(route, root)

    data class Plates(
        override val route: String = "plates",
        override val root: TabRootScreen = TabRootScreen.MoreTab
    ) : LeafScreen(route, root)

    data class PlateEdit(
        override val route: String = "plates/edit?${PLATE_ID_KEY}={${PLATE_ID_KEY}}",
        override val root: TabRootScreen = TabRootScreen.MoreTab
    ) : LeafScreen(
        route = route,
        root = root,
        arguments = listOf(
            navArgument(PLATE_ID_KEY) {
                type = NavType.StringType
                nullable = true
            },
        ),
    ) {
        companion object {
            fun createRoute(
                plateId: String? = null,
                root: TabRootScreen = TabRootScreen.MoreTab
            ): String {
                var str = "${root.route}/plates/edit"
                plateId?.let {
                    str += "?${PLATE_ID_KEY}=$it"
                }
                return str;
            }
        }
    }

    data class Personalization(
        override val route: String = "personalization",
        override val root: TabRootScreen = TabRootScreen.MoreTab
    ) : LeafScreen(route, root)

    data class MainColorsPersonalization(
        override val route: String = "personalization/main_colors",
        override val root: TabRootScreen = TabRootScreen.MoreTab
    ) :
        LeafScreen(route, root)

    data class ShapesPersonalization(
        override val route: String = "personalization/shapes",
        override val root: TabRootScreen = TabRootScreen.MoreTab
    ) :
        LeafScreen(route, root)

    data class CardsPersonalization(
        override val route: String = "personalization/cards",
        override val root: TabRootScreen = TabRootScreen.MoreTab
    ) :
        LeafScreen(route, root)

    data class TopBarPersonalization(
        override val route: String = "personalization/top_bar",
        override val root: TabRootScreen = TabRootScreen.MoreTab
    ) :
        LeafScreen(route, root)

    data class BottomBarPersonalization(
        override val route: String = "personalization/bottom_bar",
        override val root: TabRootScreen = TabRootScreen.MoreTab
    ) :
        LeafScreen(route, root)

    data class ChartsPersonalization(
        override val route: String = "personalization/charts",
        override val root: TabRootScreen = TabRootScreen.MoreTab
    ) :
        LeafScreen(route, root)

    data class KeyboardPersonalization(
        override val route: String = "personalization/keyboard",
        override val root: TabRootScreen = TabRootScreen.MoreTab
    ) :
        LeafScreen(route, root)

    data class ThemePresetsPersonalization(
        override val route: String = "personalization/presets",
        override val root: TabRootScreen = TabRootScreen.MoreTab
    ) :
        LeafScreen(route, root)

    data class ColorPickerDemo(override val route: String = "demo/color_picker_demo") :
        LeafScreen(route)

    data class ReboundSetKeyboardDemo(override val route: String = "demo/rebound_set_keyboard_demo_screen") :
        LeafScreen(route)

    data class CreateExercise(override val route: String = "create_exercise") :
        LeafScreen(route)

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

    data class Calendar(
        override val route: String = "calendar/{$SELECTED_DATE_KEY}",
        override val root: TabRootScreen = TabRootScreen.HistoryTab
    ) :
        LeafScreen(route, root) {
        companion object {

            fun createRoute(
                selectedDate: LocalDate,
                root: TabRootScreen = TabRootScreen.HistoryTab
            ) = "${root.route}/calendar/${selectedDate.toEpochMillis()}"
        }
    }

    data class Session(
        override val route: String = "session/{${WORKOUT_ID_KEY}}",
        override val root: TabRootScreen = TabRootScreen.HistoryTab
    ) : LeafScreen(
        route = route,
        root = root,
        arguments = listOf(
            navArgument(WORKOUT_ID_KEY) {
                type = NavType.StringType
            }
        ),
    ) {
        companion object {

            fun createRoute(workoutId: String, root: TabRootScreen = TabRootScreen.HistoryTab) =
                "${root.route}/session/$workoutId"

        }
    }

    data class WorkoutEdit(
        override val route: String = "workout_edit/{${WORKOUT_ID_KEY}}?$IS_TEMPLATE_KEY={$IS_TEMPLATE_KEY}",
        override val root: TabRootScreen = TabRootScreen.HistoryTab
    ) : LeafScreen(
        route = route,
        root = root,
        arguments = listOf(
            navArgument(WORKOUT_ID_KEY) {
                type = NavType.StringType
            },
            navArgument(IS_TEMPLATE_KEY) {
                type = NavType.BoolType
            },
        ),
    ) {
        companion object {

            fun createRoute(
                workoutId: String,
                isTemplate: Boolean,
                root: TabRootScreen = TabRootScreen.HistoryTab
            ) =
                "${root.route}/workout_edit/$workoutId?$IS_TEMPLATE_KEY=$isTemplate"

        }
    }

    data class ExerciseDetails(
        override val route: String = "exercises/{${EXERCISE_ID_KEY}}",
        override val root: TabRootScreen = TabRootScreen.ExercisesTab
    ) :
        LeafScreen(
            route = route,
            root = root,
            arguments = listOf(
                navArgument(EXERCISE_ID_KEY) {
                    type = NavType.StringType
                }
            )
        ) {
        companion object {
            fun createRoute(
                exerciseId: String,
                root: TabRootScreen = TabRootScreen.ExercisesTab
            ) =
                "${root.route}/exercises/$exerciseId"
        }
    }

    data class WorkoutTemplatePreview(
        override val route: String = "workout_template/{${WORKOUT_TEMPLATE_ID_KEY}}/preview",
        override val root: TabRootScreen = TabRootScreen.WorkoutTab
    ) :
        LeafScreen(
            route = route,
            root = root,
            arguments = listOf(
                navArgument(WORKOUT_TEMPLATE_ID_KEY) {
                    type = NavType.StringType
                }
            ),
        ) {
        companion object {
            fun createRoute(
                templateId: String,
                root: TabRootScreen = TabRootScreen.WorkoutTab
            ) =
                "${root.route}/workout_template/$templateId/preview"
        }
    }

    data class TemplatesFolderEdit(override val route: String = "edit_templates_folder?${FOLDER_ID_KEY}={${FOLDER_ID_KEY}}") :
        LeafScreen(
            route = route,
            arguments = listOf(
                navArgument(FOLDER_ID_KEY) {
                    type = NavType.StringType
                    nullable = true
                },
            ),
        ) {
        companion object {
            fun createRoute(folderId: String? = null): String {
                var str = "edit_templates_folder?"
                folderId?.let {
                    str = "$str${FOLDER_ID_KEY}=$it"
                }
                return str;
            }
        }
    }

    data class RestTimer(override val route: String = "rest_timer") :
        LeafScreen(route) {
        companion object {
            fun createRoute() = "rest_timer"
        }
    }

    data class SupersetSelector(override val route: String = "superset_selector?$WORKOUT_ID_KEY={$WORKOUT_ID_KEY}&$JUNCTION_ID_KEY={$JUNCTION_ID_KEY}") :
        LeafScreen(
            route = route,
            arguments = listOf(
                navArgument(WORKOUT_ID_KEY) {
                    type = NavType.StringType
                    nullable = false
                },
                navArgument(JUNCTION_ID_KEY) {
                    type = NavType.StringType
                    nullable = false
                }
            )
        ) {
        companion object {
            fun createRoute(workoutId: String, junctionId: String) =
                "superset_selector?$WORKOUT_ID_KEY=$workoutId&$JUNCTION_ID_KEY=$junctionId"
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
