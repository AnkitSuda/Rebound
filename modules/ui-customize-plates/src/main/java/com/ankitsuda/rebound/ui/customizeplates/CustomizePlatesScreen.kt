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

package com.ankitsuda.rebound.ui.customizeplates

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ankitsuda.common.compose.LocalAppSettings
import com.ankitsuda.common.compose.localizedStr
import com.ankitsuda.common.compose.userPrefWeightUnitStr
import com.ankitsuda.navigation.LeafScreen
import com.ankitsuda.navigation.LocalNavigator
import com.ankitsuda.navigation.Navigator
import com.ankitsuda.rebound.domain.WeightUnit
import com.ankitsuda.rebound.domain.entities.Plate
import com.ankitsuda.rebound.ui.components.TopBar2
import com.ankitsuda.rebound.ui.components.TopBarBackIconButton
import com.ankitsuda.rebound.ui.components.TopBarIconButton
import com.ankitsuda.rebound.ui.customizeplates.components.PlateListItemComponent
import com.ankitsuda.rebound.ui.theme.ReboundTheme
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState

@Composable
fun CustomizePlatesScreen(
    navigator: Navigator = LocalNavigator.current,
    viewModel: CustomizePlatesScreenViewModel = hiltViewModel()
) {
    val groupedPlates by viewModel.groupedPlates.collectAsState(initial = null)

    groupedPlates?.let {
        CustomizePlatesScreenLayout(
            navigator = navigator,
            groupedPlates = it,
            onUpdateIsActive = { id, isActive ->
                viewModel.updateIsActive(id, isActive)
            },
            onDeletePlate = { id ->
                viewModel.deletePlate(id)
            }
        )
    } ?: run {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(ReboundTheme.colors.background))
    }
}

@Composable
private fun CustomizePlatesScreenLayout(
    navigator: Navigator,
    groupedPlates: Map<WeightUnit?, List<Plate>>,
    onUpdateIsActive: (id: String, isActive: Boolean) -> Unit,
    onDeletePlate: (id: String) -> Unit,
) {
    val tabData = groupedPlates.keys.toList().map {
        it?.localizedStr()
    }

    val pagerState = rememberPagerState(
        initialPage = with(LocalAppSettings.current.weightUnit) {
            if (this == WeightUnit.KG) 0 else 1
        },
    )
    val tabIndex = pagerState.currentPage
    val coroutineScope = rememberCoroutineScope()
    val collapsingState = rememberCollapsingToolbarScaffoldState()

    CollapsingToolbarScaffold(
        scrollStrategy = ScrollStrategy.EnterAlwaysCollapsed,
        state = collapsingState,
        toolbar = {
            TopBar2(
                title = stringResource(id = R.string.plates),
                toolbarState = collapsingState.toolbarState,
                navigationIcon = {
                    TopBarBackIconButton {
                        navigator.goBack()
                    }
                },
                actions = {
                    TopBarIconButton(
                        icon = Icons.Outlined.Add,
                        title = stringResource(id = R.string.add_plate),
                        onClick = {
                            navigator.navigate(LeafScreen.PlateEdit.createRoute())
                        }
                    )
                },
                bottomLayout = {

                    TabRow(
                        selectedTabIndex = tabIndex,
                        backgroundColor = Color.Transparent,
                        contentColor = contentColorFor(backgroundColor = ReboundTheme.colors.topBar),
                        divider = { Divider(thickness = 0.dp) },
                        indicator = { tabPositions ->
                            TabRowDefaults.Indicator(
                                modifier = Modifier.pagerTabIndicatorOffset(
                                    pagerState,
                                    tabPositions
                                ),
                                color = ReboundTheme.colors.primary
                            )
                        }
                    ) {
                        tabData.forEachIndexed { index, pair ->
                            Tab(
                                selectedContentColor = ReboundTheme.colors.primary,
                                unselectedContentColor = ReboundTheme.colors.topBarTitle.copy(
                                    0.5f
                                ),
                                selected = tabIndex == index,
                                onClick = {
                                    coroutineScope.launch {
                                        pagerState.animateScrollToPage(index)
                                    }
                                },
                                text = {
                                    Text(text = pair ?: "")

                                }
                            )
                        }
                    }
                }
            )
        },
        modifier = Modifier.background(ReboundTheme.colors.background)
    ) {
        HorizontalPager(
            state = pagerState,
            verticalAlignment = Alignment.Top,
            count = tabData.size,
        ) { index ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(ReboundTheme.colors.background),
            ) {
                items(items = groupedPlates.values.toList()[index], key = { it.id }) {
                    PlateListItemComponent(
                        plate = it,
                        onUpdateIsActive = { newIsActive ->
                            onUpdateIsActive(it.id, newIsActive)
                        },
                        onEditPlate = {
                            navigator.navigate(LeafScreen.PlateEdit.createRoute(it.id))
                        },
                        onDeletePlate = {
                            onDeletePlate(it.id)
                        }
                    )
                }
            }
        }
    }
}