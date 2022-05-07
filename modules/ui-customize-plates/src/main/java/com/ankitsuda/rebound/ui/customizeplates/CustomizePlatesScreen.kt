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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.ankitsuda.navigation.LeafScreen
import com.ankitsuda.navigation.LocalNavigator
import com.ankitsuda.navigation.Navigator
import com.ankitsuda.rebound.domain.entities.Plate
import com.ankitsuda.rebound.ui.components.TopBar2
import com.ankitsuda.rebound.ui.components.TopBarBackIconButton
import com.ankitsuda.rebound.ui.components.TopBarIconButton
import com.ankitsuda.rebound.ui.customizeplates.components.PlateListItemComponent
import com.ankitsuda.rebound.ui.theme.ReboundTheme
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState

@Composable
fun CustomizePlatesScreen(
    navigator: Navigator = LocalNavigator.current,
    viewModel: CustomizePlatesScreenViewModel = hiltViewModel()
) {
    val plates by viewModel.plates.collectAsState(initial = emptyList())

    CustomizePlatesScreenLayout(
        navigator = navigator,
        plates = plates,
        onUpdateIsActive = { id, isActive ->
            viewModel.updateIsActive(id, isActive)
        },
        onDeletePlate = {
            viewModel.deletePlate(it)
        }
    )
}

@Composable
private fun CustomizePlatesScreenLayout(
    navigator: Navigator,
    plates: List<Plate>,
    onUpdateIsActive: (id: String, isActive: Boolean) -> Unit,
    onDeletePlate: (id: String) -> Unit,
) {

    val collapsingState = rememberCollapsingToolbarScaffoldState()

    CollapsingToolbarScaffold(
        scrollStrategy = ScrollStrategy.EnterAlwaysCollapsed,
        state = collapsingState,
        toolbar = {
            TopBar2(
                title = "Plates",
                toolbarState = collapsingState.toolbarState,
                navigationIcon = {
                    TopBarBackIconButton {
                        navigator.goBack()
                    }
                },
                actions = {
                    TopBarIconButton(
                        icon = Icons.Outlined.Add,
                        title = "Add plate",
                        onClick = {
                            navigator.navigate(LeafScreen.PlateEdit.createRoute())
                        }
                    )
                })
        },
        modifier = Modifier.background(ReboundTheme.colors.background)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(ReboundTheme.colors.background),
        ) {
            items(items = plates, key = { it.id }) {
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