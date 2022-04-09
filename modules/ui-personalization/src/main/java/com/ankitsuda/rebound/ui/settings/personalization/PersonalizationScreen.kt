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
                title = "Personalization",
                toolbarState = collapsingState.toolbarState,
                navigationIcon = {
                    TopBarBackIconButton {
                        navController.popBackStack()
                    }
                },
                actions = {
                    TopBarIconButton(icon = Icons.Outlined.Restore, title = "Reset to defaults") {

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
                    text = "Main Colors",
                    onClick = {
                        navigator.navigate(LeafScreen.MainColorsPersonalization().route)
                    })
            }
            item {
                MoreItemCard(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = "Shapes",
                    onClick = {
                        navigator.navigate(LeafScreen.ShapesPersonalization().route)
                    })
            }
            item {
                MoreItemCard(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = "Cards",
                    onClick = {
                        navigator.navigate(LeafScreen.CardsPersonalization().route)
                    })
            }
            item {
                MoreItemCard(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = "Top Bar",
                    onClick = {
                        navigator.navigate(LeafScreen.TopBarPersonalization().route)
                    })
            }
            item {
                MoreItemCard(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = "Bottom Bar",
                    onClick = {
                        navigator.navigate(LeafScreen.BottomBarPersonalization().route)
                    })
            }
            item {
                MoreItemCard(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = "Charts",
                    onClick = {
                        navigator.navigate(LeafScreen.ChartsPersonalization().route)
                    })
            }
            item {
                MoreItemCard(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = "Color Picker Demo",
                    onClick = {
                        navigator.navigate(LeafScreen.ColorPickerDemo().route)
                    })
            }
        }
    }
}