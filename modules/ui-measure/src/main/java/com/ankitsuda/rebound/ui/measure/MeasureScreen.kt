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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ankitsuda.navigation.LeafScreen
import com.ankitsuda.navigation.LocalNavigator
import com.ankitsuda.navigation.Navigator
import com.ankitsuda.rebound.ui.components.MoreItemCard
import com.ankitsuda.rebound.ui.components.MoreSectionHeader
import com.ankitsuda.rebound.ui.components.TopBar
import com.ankitsuda.rebound.ui.components.TopBarBackIconButton
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState

@Composable
fun MeasureScreen(
    navController: NavController, navigator: Navigator = LocalNavigator.current,
) {
    val collapsingState = rememberCollapsingToolbarScaffoldState()

    val core = arrayListOf("Weight", "Body fat percentage", "Calorie intake")
    val body = arrayListOf(
        "Neck",
        "Shoulders",
        "Chest",
        "Left bicep",
        "Right bicep",
        "Left forearm",
        "Right forearm",
        "Upper abs",
        "Waist",
        "Lower abs",
        "Hips",
        "Left thigh",
        "Right thigh",
        "Left calf",
        "Right calf",
    )

    CollapsingToolbarScaffold(
        state = collapsingState,
        scrollStrategy = ScrollStrategy.EnterAlwaysCollapsed,
        toolbar = {
            TopBar(title = "Measure", strictLeftIconAlignToStart = true, leftIconBtn = {
                TopBarBackIconButton {
                    navController.popBackStack()
                }
            })
        },
        modifier = Modifier.background(MaterialTheme.colors.background)
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp)
        ) {
            item {
                MoreSectionHeader(text = "Core")
            }
            items(core.size) {
                val part = core[it]
                MoreItemCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    text = part,
                    onClick = {
//                        navController.navigate(Route.PartMeasurements.createRoute(it.toLong()))
                        navigator.navigate(LeafScreen.PartMeasurements.createRoute(it.toLong()))
                    })
            }

            item {
                MoreSectionHeader(text = "Body")
            }
            items(body.size) {
                val part = body[it]
                MoreItemCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    text = part,
                    onClick = {
//                        navController.navigate(Route.PartMeasurements.createRoute((it + core.size).toLong()))
                        navigator.navigate(LeafScreen.PartMeasurements.createRoute((it + core.size).toLong()))
                    })
            }


        }
    }
}