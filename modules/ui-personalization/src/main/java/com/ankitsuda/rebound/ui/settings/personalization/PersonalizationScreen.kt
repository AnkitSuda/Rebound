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

package com.ankitsuda.rebound.ui.settings.personalization

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Restore
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ankitsuda.navigation.LeafScreen
import com.ankitsuda.navigation.LocalNavigator
import com.ankitsuda.navigation.Navigator
import com.ankitsuda.rebound.ui.components.*
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState

@Composable
fun PersonalizationScreen(
    navController: NavController, navigator: Navigator = LocalNavigator.current,
) {
    val collapsingState = rememberCollapsingToolbarScaffoldState()

    CollapsingToolbarScaffold(
        scrollStrategy = ScrollStrategy.EnterAlwaysCollapsed,
        state = collapsingState,
        toolbar = {
            TopBar2(
                title = stringResource(id = R.string.personalization),
                toolbarState = collapsingState.toolbarState,
                navigationIcon = {
                    TopBarBackIconButton {
                        navController.popBackStack()
                    }
                },
                actions = {
                    TopBarIconButton(icon = Icons.Outlined.Restore, title = stringResource(id = R.string.reset_to_defaults)) {

                    }
                })
        },
        modifier = Modifier.background(MaterialTheme.colors.background)
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            item {
                MoreItemCard(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = stringResource(id = R.string.theme_presets),
                    onClick = {
                        navigator.navigate(LeafScreen.ThemePresetsPersonalization().createRoute())
                    })
            }
            item {
                MoreItemCard(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = stringResource(R.string.main_colors),
                    onClick = {
                        navigator.navigate(LeafScreen.MainColorsPersonalization().createRoute())
                    })
            }
            item {
                MoreItemCard(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = stringResource(R.string.shapes),
                    onClick = {
                        navigator.navigate(LeafScreen.ShapesPersonalization().createRoute())
                    })
            }
            item {
                MoreItemCard(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = stringResource(R.string.cards),
                    onClick = {
                        navigator.navigate(LeafScreen.CardsPersonalization().createRoute())
                    })
            }
            item {
                MoreItemCard(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = stringResource(R.string.top_bar),
                    onClick = {
                        navigator.navigate(LeafScreen.TopBarPersonalization().createRoute())
                    })
            }
            item {
                MoreItemCard(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = stringResource(R.string.bottom_bar),
                    onClick = {
                        navigator.navigate(LeafScreen.BottomBarPersonalization().createRoute())
                    })
            }
            item {
                MoreItemCard(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = stringResource(R.string.charts),
                    onClick = {
                        navigator.navigate(LeafScreen.ChartsPersonalization().createRoute())
                    })
            }

            item {
                MoreItemCard(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = stringResource(R.string.keyboard),
                    onClick = {
                        navigator.navigate(LeafScreen.KeyboardPersonalization().createRoute())
                    })
            }
            item {
                MoreItemCard(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = stringResource(R.string.color_picker_demo),
                    onClick = {
                        navigator.navigate(LeafScreen.ColorPickerDemo().createRoute())
                    })
            }
            item {
                MoreItemCard(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = stringResource(R.string.set_keyboard_demo),
                    onClick = {
                        navigator.navigate(LeafScreen.ReboundSetKeyboardDemo().createRoute())
                    })
            }
        }
    }
}