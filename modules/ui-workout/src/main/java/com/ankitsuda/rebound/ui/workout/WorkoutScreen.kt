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

package com.ankitsuda.rebound.ui.workout

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material.icons.outlined.OpenInFull
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ankitsuda.common.compose.LocalPanel
import com.ankitsuda.navigation.LeafScreen
import com.ankitsuda.navigation.LocalNavigator
import com.ankitsuda.navigation.Navigator
import com.ankitsuda.navigation.TabRootScreen
import com.ankitsuda.rebound.domain.entities.TemplateWithWorkout
import com.ankitsuda.rebound.ui.components.*
import com.ankitsuda.rebound.ui.components.dialogs.DiscardActiveWorkoutDialog
import com.ankitsuda.rebound.ui.theme.LocalThemeState
import com.ankitsuda.rebound.ui.theme.ReboundTheme
import kotlinx.coroutines.launch
import me.onebone.toolbar.*
import me.onebone.toolbar.FabPosition

@OptIn(
    ExperimentalFoundationApi::class,
    ExperimentalAnimationApi::class,
    ExperimentalToolbarApi::class
)
@Composable
fun WorkoutScreen(
    navController: NavController,
    navigator: Navigator = LocalNavigator.current,
    viewModel: WorkoutScreenViewModel = hiltViewModel(),
) {
    var isDiscardActiveWorkoutDialogVisible by rememberSaveable {
        mutableStateOf(false)
    }
    var isDiscardActiveWorkoutDialogTemplateId: String? by rememberSaveable {
        mutableStateOf(null)
    }
    var areArchivedTemplatesVisible by rememberSaveable {
        mutableStateOf(false)
    }
    val collapsingState = rememberCollapsingToolbarScaffoldState()
    val currentWorkout by viewModel.currentWorkout.collectAsState(initial = null)
    val currentWorkoutDurationStr by viewModel.currentWorkoutDurationStr.collectAsState(initial = null)
    val archivedTemplates by viewModel.archivedTemplates.collectAsState(emptyList())
    val unarchivedTemplates by viewModel.unarchivedTemplates.collectAsState(emptyList())
    val coroutine = rememberCoroutineScope()
    val mainPanel = LocalPanel.current

    fun expandPanel() {
        coroutine.launch {
            mainPanel.expand()
        }
    }

    fun createAndNavigateToTemplate() {
        viewModel.createTemplate {
            if (it.workoutId != null) {
                navigator.navigate(
                    LeafScreen.WorkoutEdit.createRoute(
                        workoutId = it.workoutId!!,
                        isTemplate = false,
                        root = TabRootScreen.WorkoutTab
                    )
                )
            }
        }
    }

    fun startWorkoutFromTemplateId(templateId: String, discardActive: Boolean) {
        viewModel.startWorkoutFromTemplateId(
            templateId = templateId,
            discardActive = discardActive,
            onWorkoutAlreadyActive = {
                isDiscardActiveWorkoutDialogTemplateId = templateId
                isDiscardActiveWorkoutDialogVisible = true
            }
        )
    }

    ToolbarWithFabScaffold(
        scrollStrategy = ScrollStrategy.EnterAlwaysCollapsed,
        state = collapsingState,
        toolbar = {
            TopBar2(
                title = "Workout",
                toolbarState = collapsingState.toolbarState,
            )
        },
        fab = {

            if (currentWorkout == null) {
                ExtendedFloatingActionButton(
                    modifier = Modifier,
                    elevation = FloatingActionButtonDefaults.elevation(
                        defaultElevation = 2.dp,
                        pressedElevation = 4.dp
                    ),
                    text = { Text(text = "Empty Workout") },
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.Add,
                            contentDescription = null
                        )
                    },
                    onClick = {
                        viewModel.startEmptyWorkout()
                    })

            }
        },
        fabPosition = FabPosition.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {


        // User routines
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            contentPadding = PaddingValues(bottom = 64.dp)
        ) {
            if (currentWorkout != null) {
                item("current_workout_overview") {
                    AppCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 8.dp)
                            .animateItemPlacement(),
                        backgroundColor = ReboundTheme.colors.primary,
                        onClick = {
                            expandPanel()
                        }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column() {
                                Text(
                                    text = "Ongoing Workout",
                                    style = ReboundTheme.typography.h6,
                                    color = ReboundTheme.colors.onPrimary
                                )
                                currentWorkoutDurationStr?.let {
                                    RSpacer(space = 4.dp)
                                    Text(
                                        text = it,
                                        style = ReboundTheme.typography.body2,
                                        color = ReboundTheme.colors.onPrimary.copy(alpha = 0.7f)

                                    )
                                }
                            }
                            IconButton(onClick = { expandPanel() }) {
                                Icon(
                                    imageVector = Icons.Outlined.OpenInFull,
                                    contentDescription = "Open",
                                    tint = ReboundTheme.colors.onPrimary
                                )
                            }
                        }
                    }
                }
            }

            item(key = "plans_row") {
                Column(
                    modifier = Modifier.animateItemPlacement(),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Plans", style = MaterialTheme.typography.body1,
                            color = LocalThemeState.current.onBackgroundColor
                        )
                        TextButton(onClick = {
                        }) {
                            Icon(
                                imageVector = Icons.Outlined.Add,
                                contentDescription = "New Plan"
                            )
                            RSpacer(space = 4.dp)
                            Text(text = "NEW", style = MaterialTheme.typography.button)
                        }
                    }

                    LazyRow() {
                        items(5) {
                            RoutineItemCard(
                                name = "Push",
                                date = "2 Aug 2021",
                                totalExercises = 7,
                                modifier = Modifier
                                    .width(((LocalConfiguration.current.screenWidthDp / 2) - 24).dp)
                                    .padding(
                                        start = if (it == 0) 16.dp else 0.dp,
                                        end = 16.dp
                                    )
                            ) {

                            }
                        }
                    }
                }
            }
            item(key = "templates_header") {

                Column(
                    modifier = Modifier
                        .animateItemPlacement()
                ) {


                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Templates", style = MaterialTheme.typography.body1,
                            color = LocalThemeState.current.onBackgroundColor
                        )
                        TextButton(onClick = {
                            createAndNavigateToTemplate()
                        }) {
                            Icon(
                                imageVector = Icons.Outlined.Add,
                                contentDescription = "New Template"
                            )
                            Text(text = "NEW", style = MaterialTheme.typography.button)
                        }
                    }


                }
            }

            items(unarchivedTemplates, key = { it.template.id }) {
                TemplateListItem(navigator = navigator, templateWithWorkout = it, onClickPlay = {
                    startWorkoutFromTemplateId(templateId = it.template.id, discardActive = false)
                })
            }

            if (archivedTemplates.isNotEmpty()) {
                if (areArchivedTemplatesVisible) {
                    item(key = "archived_templates_count_text") {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp)
                                .animateItemPlacement(),
                            text = "${archivedTemplates.size} archived template${if (archivedTemplates.size > 1) "s" else ""}",
                            textAlign = TextAlign.Center,
                            style = ReboundTheme.typography.caption,
                            color = ReboundTheme.colors.onBackground.copy(alpha = 0.7f)
                        )
                    }

                    items(archivedTemplates, key = { it.template.id }) {
                        TemplateListItem(
                            navigator = navigator,
                            templateWithWorkout = it,
                            onClickPlay = {
                                startWorkoutFromTemplateId(
                                    templateId = it.template.id,
                                    discardActive = false
                                )
                            })
                    }
                }

                item(key = "archived_templates_visibility_button") {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItemPlacement()
                            .clickable {
                                areArchivedTemplatesVisible = !areArchivedTemplatesVisible
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Row(modifier = Modifier.padding(12.dp)) {
                            Icon(
                                modifier = Modifier.size(18.dp),
                                imageVector = if (areArchivedTemplatesVisible) Icons.Outlined.KeyboardArrowUp else Icons.Outlined.KeyboardArrowDown,
                                contentDescription = null,
                                tint = ReboundTheme.colors.onBackground.copy(alpha = 0.7f)
                            )
                            RSpacer(space = 4.dp)
                            Text(
                                textAlign = TextAlign.Center,
                                style = ReboundTheme.typography.caption,
                                color = ReboundTheme.colors.onBackground.copy(alpha = 0.7f),
                                text = if (areArchivedTemplatesVisible) "Hide archived" else "Show archived"
                            )
                        }
                    }
                }

            }

        }

    }

    if (isDiscardActiveWorkoutDialogVisible && isDiscardActiveWorkoutDialogTemplateId != null) {
        DiscardActiveWorkoutDialog(
            onClickDiscard = {
                isDiscardActiveWorkoutDialogVisible = false
                startWorkoutFromTemplateId(
                    discardActive = true,
                    templateId = isDiscardActiveWorkoutDialogTemplateId!!
                );
            },
            onDismissRequest = {
                isDiscardActiveWorkoutDialogTemplateId = null
                isDiscardActiveWorkoutDialogVisible = false
            }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun LazyItemScope.TemplateListItem(
    navigator: Navigator,
    templateWithWorkout: TemplateWithWorkout,
    onClickPlay: () -> Unit,
) {
    with(templateWithWorkout) {
        TemplateItemCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                .animateItemPlacement(),
            name = (workout.name ?: "").ifBlank { "Unnamed Template" },
            italicName = (workout.name ?: "").isBlank(),
            totalExercises = exerciseWorkoutJunctions.size,
            onClick = {
                navigator.navigate(LeafScreen.WorkoutTemplatePreview.createRoute(template.id))
            },
            onClickPlay = onClickPlay
        )
    }
}