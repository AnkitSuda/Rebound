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

package com.ankitsuda.rebound.ui.settings.personalization.keyboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Restore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ankitsuda.base.ui.ThemeState
import com.ankitsuda.common.compose.rememberFlowWithLifecycle
import com.ankitsuda.rebound.ui.ThemeViewModel
import com.ankitsuda.rebound.ui.components.TopBar2
import com.ankitsuda.rebound.ui.components.TopBarBackIconButton
import com.ankitsuda.rebound.ui.components.TopBarIconButton
import com.ankitsuda.rebound.ui.components.settings.ColorPickerCardItem
import com.ankitsuda.rebound.ui.theme.ReboundTheme
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState
import com.ankitsuda.common.compose.R

@Composable
fun KeyboardPersonalizationScreen(
    navController: NavController,
    themeViewModel: ThemeViewModel = hiltViewModel(),
//    viewModel: TopBarPersonalizationScreenViewModel = hiltViewModel()
) {
    val themeState by rememberFlowWithLifecycle(themeViewModel.themeState).collectAsState(initial = null)

    themeState?.let { theme ->
        KeyboardPersonalizationScreen(
            navController = navController,
            themeState = theme,
            setThemeState = themeViewModel::applyThemeState,
        )

    }
}

@Composable
private fun KeyboardPersonalizationScreen(
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
                title = stringResource(id = R.string.keyboard),
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
        modifier = Modifier.background(MaterialTheme.colors.background)
    ) {

        val itemModifier = Modifier
            .fillMaxWidth()

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(ReboundTheme.colors.background),
        ) {
            item {
                ColorPickerCardItem(
                    modifier = itemModifier,
                    text = stringResource(id = R.string.background_color),
                    selectedColor = themeState.keyboardBackgroundColor,
                    onNewColorSelected = {
                        setThemeState(themeState.copy(keyboardBackgroundColor = it))
                    })

            }

            item {
                ColorPickerCardItem(
                    modifier = itemModifier,
                    text = stringResource(id = R.string.content_color),
                    selectedColor = themeState.keyboardContentColor,
                    onNewColorSelected = {
                        setThemeState(themeState.copy(keyboardContentColor = it))
                    })
            }

            item {
                ColorPickerCardItem(
                    modifier = itemModifier,
                    text = stringResource(id = R.string.barbell_color),
                    selectedColor = themeState.keyboardBarbellColor,
                    onNewColorSelected = {
                        setThemeState(themeState.copy(keyboardBarbellColor = it))
                    })
            }

            item {
                ColorPickerCardItem(
                    modifier = itemModifier,
                    text = stringResource(id = R.string.on_barbell_color),
                    selectedColor = themeState.keyboardBarbellColor,
                    onNewColorSelected = {
                        setThemeState(themeState.copy(keyboardBarbellColor = it))
                    })
            }
        }
    }

}
