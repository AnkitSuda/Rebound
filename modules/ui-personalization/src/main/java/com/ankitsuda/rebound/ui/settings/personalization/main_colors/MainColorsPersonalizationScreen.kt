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

package com.ankitsuda.rebound.ui.settings.personalization.main_colors

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Restore
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ankitsuda.base.ui.ThemeState
import com.ankitsuda.common.compose.LocalDialog
import com.ankitsuda.common.compose.rememberFlowWithLifecycle
import com.ankitsuda.rebound.ui.ThemeViewModel
import com.ankitsuda.rebound.ui.settings.personalization.charts.ChartsPersonalizationScreen


import com.ankitsuda.rebound.ui.components.TopBar
import com.ankitsuda.rebound.ui.components.TopBar2
import com.ankitsuda.rebound.ui.components.TopBarBackIconButton
import com.ankitsuda.rebound.ui.components.TopBarIconButton
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState
import com.ankitsuda.rebound.ui.components.settings.ColorPickerCardItem
import com.ankitsuda.rebound.ui.components.settings.SliderCardItem
import com.ankitsuda.rebound.ui.components.settings.SwitchCardItem
import com.ankitsuda.rebound.ui.theme.ReboundTheme
import me.onebone.toolbar.ScrollStrategy
import com.ankitsuda.common.compose.R

@Composable
fun MainColorsPersonalizationScreen(
    navController: NavController,
    themeViewModel: ThemeViewModel = hiltViewModel(),
) {
    val themeState by rememberFlowWithLifecycle(themeViewModel.themeState).collectAsState(initial = null)

    themeState?.let { theme ->
        MainColorsPersonalizationScreen(
            navController = navController,
            themeState = theme,
            setThemeState = themeViewModel::applyThemeState,
        )

    }
}

@Composable
fun MainColorsPersonalizationScreen(
    navController: NavController,
    themeState: ThemeState,
    setThemeState: (ThemeState) -> Unit,
) {
    val collapsingState = rememberCollapsingToolbarScaffoldState()

    CollapsingToolbarScaffold(
        scrollStrategy = ScrollStrategy.EnterAlwaysCollapsed,
        state = collapsingState,
        toolbar = {
            TopBar2(
                title = stringResource(id = R.string.main_colors),
                toolbarState = collapsingState.toolbarState,
                navigationIcon = {
                    TopBarBackIconButton {
                        navController.popBackStack()
                    }
                },
                actions = {
                    TopBarIconButton(icon = Icons.Outlined.Restore, title = stringResource(id = R.string.reset_to_defaults)) {

                    }
                }
            )
        },
        modifier = Modifier.background(ReboundTheme.colors.background)
    ) {

        val itemModifier = Modifier
            .fillMaxWidth()

        with(LocalDialog.current) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(ReboundTheme.colors.background),
            ) {

                item {

                    ColorPickerCardItem(
                        modifier = itemModifier,
                        text = stringResource(id = R.string.primary_color),
                        selectedColor = themeState.primaryColor,
                        onNewColorSelected = {
                            setThemeState(themeState.copy(primaryColor = it))
                        },
                    )

                }

                item {

                    ColorPickerCardItem(
                        modifier = itemModifier,
                        text = stringResource(id = R.string.background_color),
                        selectedColor = themeState.backgroundColor,
                        onNewColorSelected = {
                            setThemeState(themeState.copy(backgroundColor = it))
                        }
                    )

                }

                item {
                    ColorPickerCardItem(
                        modifier = itemModifier,
                        text = stringResource(id = R.string.on_primary_color),
                        selectedColor = themeState.onPrimaryColor,
                        useAltColorPicker = true,
                        onNewColorSelected = {
                            setThemeState(themeState.copy(onPrimaryColor = it))
                        })
                }
                item {
                    ColorPickerCardItem(
                        modifier = itemModifier,
                        text = stringResource(id = R.string.on_background_color),
                        selectedColor = themeState.onBackgroundColor,
                        onNewColorSelected = {
                            setThemeState(themeState.copy(onBackgroundColor = it))
                        })
                }

                item {

                    SwitchCardItem(
                        modifier = itemModifier,
                        text = stringResource(id = R.string.is_light_theme),
                        checked = themeState.isLightTheme,
                        onChange = {
                            setThemeState(themeState.copy(isLightTheme = it))
                        }
                    )

                }

                item {

                    SwitchCardItem(
                        modifier = itemModifier,
                        text = stringResource(id = R.string.dark_status_bar_icons),
                        checked = themeState.isDarkStatusBarIcons,
                        onChange = {
                            setThemeState(themeState.copy(isDarkStatusBarIcons = it))
                        }
                    )

                }
                item {

                    SwitchCardItem(
                        modifier = itemModifier,
                        text = stringResource(id = R.string.dark_navigation_bar_icons),
                        checked = themeState.isDarkNavigationBarIcons,
                        onChange = {
                            setThemeState(themeState.copy(isDarkNavigationBarIcons = it))
                        }
                    )

                }
            }
        }
    }

}

