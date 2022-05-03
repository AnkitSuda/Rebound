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

package com.ankitsuda.rebound.ui.settings.personalization.presets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Restore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ankitsuda.common.compose.rememberFlowWithLifecycle
import com.ankitsuda.navigation.LocalNavigator
import com.ankitsuda.navigation.Navigator
import com.ankitsuda.rebound.domain.entities.ThemePreset
import com.ankitsuda.rebound.ui.ThemeViewModel
import com.ankitsuda.rebound.ui.components.TopBar2
import com.ankitsuda.rebound.ui.components.TopBarBackIconButton
import com.ankitsuda.rebound.ui.components.TopBarIconButton
import com.ankitsuda.rebound.ui.theme.ReboundTheme
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState

@Composable
fun ThemePresetsPersonalizationScreen(
    navigator: Navigator = LocalNavigator.current,
    themeViewModel: ThemeViewModel = hiltViewModel(),
    viewModel: ThemePresetsPersonalizationScreenViewModel = hiltViewModel()
) {
    val themeState by rememberFlowWithLifecycle(themeViewModel.themeState).collectAsState(initial = null)
    val presets by viewModel.presets.collectAsState(initial = emptyList())

    ThemePresetsPersonalizationScreen(
        navigator = navigator,
        presets = presets,
        onApplyPreset = {
            themeViewModel.applyThemePreset(it)
        },
        onSaveCurrentTheme = {
            if (themeState != null) {
                viewModel.addPreset(themeState!!)
            }
        },
        onDeletePreset = {
            viewModel.deletePreset(it)
        }
    )

}

@Composable
private fun ThemePresetsPersonalizationScreen(
    navigator: Navigator,
    presets: List<ThemePreset>,
    onApplyPreset: (ThemePreset) -> Unit,
    onDeletePreset: (ThemePreset) -> Unit,
    onSaveCurrentTheme: () -> Unit,
) {
    val collapsingState = rememberCollapsingToolbarScaffoldState()

    CollapsingToolbarScaffold(
        scrollStrategy = ScrollStrategy.EnterAlwaysCollapsed,
        state = collapsingState,
        toolbar = {
            TopBar2(
                title = "Presets",
                toolbarState = collapsingState.toolbarState,
                navigationIcon = {
                    TopBarBackIconButton {
                        navigator.goBack()
                    }
                },
                actions = {
                    TopBarIconButton(icon = Icons.Outlined.Restore, title = "Reset to defaults") {

                    }
                }
            )
        },
        modifier = Modifier.background(ReboundTheme.colors.background)
    ) {
        val itemModifier = Modifier
            .fillMaxWidth()

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(ReboundTheme.colors.background),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            item {
                SaveCurrentThemeComponent(
                    modifier = itemModifier,
                    onClick = onSaveCurrentTheme
                )
            }

            items(presets.size) {
                val preset = presets[it]


                ThemeItemComponent(
                    modifier = itemModifier,
                    preset = preset,
                    onClickApply = {
                        onApplyPreset(preset)
                    },
                    onClickDelete = {
                        onDeletePreset(preset)
                    }
                )

            }

        }

    }
}