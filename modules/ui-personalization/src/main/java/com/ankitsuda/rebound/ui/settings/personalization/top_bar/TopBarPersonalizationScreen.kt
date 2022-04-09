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

package com.ankitsuda.rebound.ui.settings.personalization.top_bar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Restore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ankitsuda.base.ui.ThemeState
import com.ankitsuda.base.util.TopBarAlignment
import com.ankitsuda.common.compose.LocalDialog
import com.ankitsuda.common.compose.rememberFlowWithLifecycle
import com.ankitsuda.rebound.ui.ThemeViewModel
import com.ankitsuda.rebound.ui.components.TopBar
import com.ankitsuda.rebound.ui.components.TopBar2
import com.ankitsuda.rebound.ui.components.TopBarBackIconButton
import com.ankitsuda.rebound.ui.components.TopBarIconButton
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState
import com.ankitsuda.rebound.ui.components.settings.ColorPickerCardItem
import com.ankitsuda.rebound.ui.components.settings.RadioGroupCardItem
import com.ankitsuda.rebound.ui.components.settings.SliderCardItem
import kotlinx.serialization.KSerializer
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.onebone.toolbar.ScrollStrategy
import timber.log.Timber

@Composable
fun TopBarPersonalizationScreen(
    navController: NavController,
    themeViewModel: ThemeViewModel = hiltViewModel(),
//    viewModel: TopBarPersonalizationScreenViewModel = hiltViewModel()
) {

    val themeState by rememberFlowWithLifecycle(themeViewModel.themeState).collectAsState(initial = null)

    themeState?.let { theme ->
        TopBarPersonalizationScreen(
            navController = navController,
            themeState = theme,
            setThemeState = themeViewModel::applyThemeState,
        )

    }
}

@Composable
private fun TopBarPersonalizationScreen(
    navController: NavController,
    themeState: ThemeState,
    setThemeState: (ThemeState) -> Unit,
) {
    val collapsingState = rememberCollapsingToolbarScaffoldState()
    val allTitleAlignments = TopBarAlignment.values()

//    val titleAlignment by viewModel.titleAlignment.collectAsState("center")
//    val backgroundColor by viewModel.backgroundColor.collectAsState(Color.White)
//    val contentColor by viewModel.contentColor.collectAsState(Color.White)
//    val elevation by viewModel.elevation.collectAsState(0)
//
    CollapsingToolbarScaffold(
        scrollStrategy = ScrollStrategy.EnterAlwaysCollapsed,
        state = collapsingState,
        toolbar = {
            TopBar2(
                title = "Top Bar",
                toolbarState = collapsingState.toolbarState,
                navigationIcon = {
                    TopBarBackIconButton {
                        navController.popBackStack()
                    }
                },
                actions = {
                    TopBarIconButton(icon = Icons.Outlined.Restore, title = "Reset to defaults") {

                    }
                }
            )
        },
        modifier = Modifier.background(MaterialTheme.colors.background)
    ) {

        val itemModifier = Modifier
            .fillMaxWidth()

        with(LocalDialog.current) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.background),
            ) {

                item {

                    RadioGroupCardItem(
                        modifier = itemModifier,
                        text = "Title alignment",
                        onSelectionChange = { _, value ->
                            setThemeState(themeState.copy(topBarTitleAlignment = value))
                        },
                        items = allTitleAlignments,
                        selected = themeState.topBarTitleAlignment
                    )
                }

                item {
                    ColorPickerCardItem(
                        modifier = itemModifier,
                        text = "Background color",
                        selectedColor = themeState.topBarBackgroundColor,
                        onNewColorSelected = {
                            setThemeState(themeState.copy(topBarBackgroundColor = it))
                        })

                }

                item {
                    ColorPickerCardItem(
                        modifier = itemModifier,
                        text = "Content color",
                        selectedColor = themeState.topBarContentColor,
                        onNewColorSelected = {
                            setThemeState(themeState.copy(topBarContentColor = it))
                        })

                }

                item {

                    SliderCardItem(
                        modifier = itemModifier,
                        text = "Elevation",
                        value = themeState.topBarElevation,
                        steps = 50,
                        valueRange = 0f..50f,
                        onChange = {
                            setThemeState(themeState.copy(topBarElevation = it))
                        }
                    )

                }

            }
        }
    }

}
