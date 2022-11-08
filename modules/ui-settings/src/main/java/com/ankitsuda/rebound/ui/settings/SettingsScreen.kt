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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ankitsuda.navigation.LeafScreen
import com.ankitsuda.navigation.LocalNavigator
import com.ankitsuda.navigation.Navigator
import com.ankitsuda.rebound.ui.components.*
import com.ankitsuda.rebound.ui.icons.Plates
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
                title = stringResource(R.string.settings),
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
        ) {
            item {
                MoreItemCard(
                    modifier = Modifier
                        .fillMaxWidth(),
                    icon = Icons.Outlined.BubbleChart,
                    text = stringResource(R.string.personalization),
                    description = stringResource(R.string.make_rebound_yours),
                    onClick = {
                        navigator.navigate(LeafScreen.Personalization().createRoute())
                    })
            }
            item {
                MoreItemCard(
                    modifier = Modifier
                        .fillMaxWidth(),
                    icon = Icons.Filled.Plates,
                    text = stringResource(R.string.plates),
                    description = stringResource(R.string.customize_barbell_plates),
                    onClick = {
                        navigator.navigate(LeafScreen.Plates().createRoute())
                    })
            }
            item {
                MoreSectionHeader(text = stringResource(R.string.defaults))
            }
            item {
                MoreItemCard(
                    modifier = Modifier
                        .fillMaxWidth(),
                    icon = Icons.Outlined.FitnessCenter,
                    text = stringResource(R.string.weight_unit),
                    description = stringResource(R.string.metric_kg),
                    onClick = {

                    })
            }
            item {
                MoreItemCard(
                    modifier = Modifier
                        .fillMaxWidth(),
                    icon = Icons.Outlined.DirectionsRun,
                    text = stringResource(R.string.distance_unit),
                    description = stringResource(R.string.metric_m_km),
                    onClick = {

                    })
            }
            item {
                MoreItemCard(
                    modifier = Modifier
                        .fillMaxWidth(),
                    icon = Icons.Outlined.Event,
                    text = stringResource(R.string.first_day_of_the_week),
                    description = stringResource(R.string.sunday),
                    onClick = {

                    })
            }
            item {
                MoreSectionHeader(text = stringResource(R.string.your_data))
            }
            item {
                MoreItemCard(
                    modifier = Modifier
                        .fillMaxWidth(),
                    icon = Icons.Outlined.Folder,
                    text = stringResource(R.string.backup_data),
                    description = stringResource(R.string.to_json),
                    onClick = {

                    })
            }
            item {
                MoreItemCard(
                    modifier = Modifier
                        .fillMaxWidth(),
                    icon = Icons.Outlined.Restore,
                    text = stringResource(R.string.restore_data),
                    description = stringResource(R.string.from_a_previous_backup),
                    onClick = {

                    })
            }

            item {
                MoreSectionHeader(text = stringResource(R.string.feedback))
            }
            item {
                MoreItemCard(
                    modifier = Modifier
                        .fillMaxWidth(),
                    icon = Icons.Outlined.ThumbsUpDown,
                    text = stringResource(R.string.write_a_review),
                    description = stringResource(R.string.write_review_description),
                    onClick = {

                    })
            }
            item {
                MoreItemCard(
                    modifier = Modifier
                        .fillMaxWidth(),
                    icon = Icons.Outlined.BugReport,
                    text = stringResource(R.string.suggestions_and_bug_report),
                    description = stringResource(R.string.suggestions_and_bug_report_description),
                    onClick = {

                    })
            }
            item {
                MoreSectionHeader(text = stringResource(R.string.about))
            }
            item {
                MoreItemCard(
                    modifier = Modifier
                        .fillMaxWidth(),
                    icon = Icons.Outlined.Info,
                    text = stringResource(R.string.about_app),
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