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

package com.ankitsuda.rebound.ui.more

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
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
fun MoreScreen(
    navController: NavController, navigator: Navigator = LocalNavigator.current
) {
    val collapsingState = rememberCollapsingToolbarScaffoldState()

    CollapsingToolbarScaffold(
        state = collapsingState,
        scrollStrategy = ScrollStrategy.EnterAlwaysCollapsed,
        toolbar = {
            TopBar2(
                title = stringResource(id = R.string.more),
                toolbarState = collapsingState.toolbarState,
            )
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
                    icon = Icons.Outlined.Straighten,
                    text = stringResource(id = R.string.measure),
                    description = stringResource(id = R.string.body_measurements),
                    onClick = {
                        navigator.navigate(LeafScreen.Measure().createRoute())
                    })
            }
            item {
                MoreItemCard(
                    modifier = Modifier
                        .fillMaxWidth(),
                    icon = Icons.Outlined.SportsScore,
                    text = stringResource(id = R.string.achievements),
                    description = "0 achievements",
                    onClick = {
                    })
            }
            item {
                MoreSectionHeader(text = stringResource(id = R.string.settings))
            }
            item {
                MoreItemCard(
                    modifier = Modifier
                        .fillMaxWidth(),
                    icon = Icons.Outlined.BubbleChart,
                    text = stringResource(id = R.string.personalization),
                    description = stringResource(id = R.string.make_rebound_yours),
                    onClick = {
                        navigator.navigate(LeafScreen.Personalization().createRoute())
                    })
            }
            item {
                MoreItemCard(
                    modifier = Modifier
                        .fillMaxWidth(),
                    icon = Icons.Outlined.Settings,
                    text = stringResource(id = R.string.settings),
                    description = stringResource(id = R.string.settings_description),
                    onClick = {
                        navigator.navigate(LeafScreen.Settings().createRoute())
                    })
            }
        }

    }
}

