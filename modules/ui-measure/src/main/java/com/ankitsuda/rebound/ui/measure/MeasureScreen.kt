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

package com.ankitsuda.rebound.ui.measure

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Restore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ankitsuda.navigation.LeafScreen
import com.ankitsuda.navigation.LocalNavigator
import com.ankitsuda.navigation.Navigator
import com.ankitsuda.rebound.ui.components.*
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState

@Composable
fun MeasureScreen(
    navController: NavController,
    navigator: Navigator = LocalNavigator.current,
    viewModel: MeasureScreenViewModel = hiltViewModel()
) {
    val collapsingState = rememberCollapsingToolbarScaffoldState()
    val partsWithGroup by viewModel.bodyPartsWithGroup.collectAsState(initial = listOf())

    CollapsingToolbarScaffold(
        state = collapsingState,
        scrollStrategy = ScrollStrategy.EnterAlwaysCollapsed,
        toolbar = {
            TopBar2(
                title = stringResource(id = R.string.measure),
                toolbarState = collapsingState.toolbarState,
                navigationIcon = {
                    TopBarBackIconButton {
                        navController.popBackStack()
                    }
                },
            )
        },
        modifier = Modifier.background(MaterialTheme.colors.background)
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
        ) {
            for (junction in partsWithGroup) {
                item(key = junction.group.id) {
                    MoreSectionHeader(text = junction.group.name ?: stringResource(id = R.string.group))
                }

                items(junction.parts.size) {
                    val part = junction.parts[it]
                    MoreItemCard(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = part.name ?: stringResource(id = R.string.body_part),
                        onClick = {
                            part.id.let { id ->
                                navigator.navigate(LeafScreen.PartMeasurements.createRoute(id))
                            }
                        })
                }
            }

        }
    }
}