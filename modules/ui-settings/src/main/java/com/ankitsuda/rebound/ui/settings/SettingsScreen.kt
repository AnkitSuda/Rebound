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

package com.ankitsuda.rebound.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
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
fun SettingsScreen(
    navController: NavController, navigator: Navigator = LocalNavigator.current,
) {
    val collapsingState = rememberCollapsingToolbarScaffoldState()

    CollapsingToolbarScaffold(
        scrollStrategy = ScrollStrategy.EnterAlwaysCollapsed,
        state = collapsingState,
        toolbar = {
            TopBar2(
                title = "Settings",
                toolbarState = collapsingState.toolbarState,
                navigationIcon = {
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
            contentPadding = PaddingValues(16.dp)
        ) {
            item {
                MoreItemCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    icon = Icons.Outlined.BubbleChart,
                    text = "Personalization",
                    description = "Make rebound yours",
                    onClick = {
                        navigator.navigate(LeafScreen.Personalization().route)
                    })
            }
            item {
                MoreSectionHeader(text = "Defaults")
            }
            item {
                MoreItemCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    icon = Icons.Outlined.FitnessCenter,
                    text = "Weight Unit",
                    description = "Metric (kg)",
                    onClick = {

                    })
            }
            item {
                MoreItemCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    icon = Icons.Outlined.DirectionsRun,
                    text = "Distance Unit",
                    description = "Metric (m/km)",
                    onClick = {

                    })
            }
            item {
                MoreItemCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    icon = Icons.Outlined.Event,
                    text = "First Day of The Week",
                    description = "Sunday",
                    onClick = {

                    })
            }
            item {
                MoreSectionHeader(text = "Your Data")
            }
            item {
                MoreItemCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    icon = Icons.Outlined.Folder,
                    text = "Backup Data",
                    description = "To JSON",
                    onClick = {

                    })
            }
            item {
                MoreItemCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    icon = Icons.Outlined.Restore,
                    text = "Restore Data",
                    description = "From a previous backup",
                    onClick = {

                    })
            }

            item {
                MoreSectionHeader(text = "Feedback")
            }
            item {
                MoreItemCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    icon = Icons.Outlined.ThumbsUpDown,
                    text = "Write a Review",
                    description = "It will motivate us to make rebound more better.",
                    onClick = {

                    })
            }
            item {
                MoreItemCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    icon = Icons.Outlined.BugReport,
                    text = "Suggestions & Bug Report",
                    description = "You can open an issue in Github repository",
                    onClick = {

                    })
            }
            item {
                MoreSectionHeader(text = "About")
            }
            item {
                MoreItemCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    icon = Icons.Outlined.Info,
                    text = "About app",
                    onClick = {

                    })
            }
//            item {
//                Text(
//                    text = "v${BuildConfig.VERSION_NAME}",
//                    modifier = Modifier
//                        .padding(16.dp)
//                        .fillMaxWidth(),
//                    style = MaterialTheme.typography.caption,
//                    textAlign = TextAlign.Center
//                )
//            }
        }

    }
}