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

package com.ankitsuda.rebound.ui.customizebarbells

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.ankitsuda.navigation.LeafScreen
import com.ankitsuda.navigation.LocalNavigator
import com.ankitsuda.navigation.Navigator
import com.ankitsuda.rebound.domain.entities.Barbell
import com.ankitsuda.rebound.ui.components.TopBar2
import com.ankitsuda.rebound.ui.components.TopBarBackIconButton
import com.ankitsuda.rebound.ui.components.TopBarIconButton
import com.ankitsuda.rebound.ui.customizebarbells.components.BarbellListItemComponent
import com.ankitsuda.rebound.ui.theme.ReboundTheme
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState

@Composable
fun CustomizeBarbellsScreen(
    navigator: Navigator = LocalNavigator.current,
    viewModel: CustomizeBarbellsScreenViewModel = hiltViewModel()
) {
    val barbells by viewModel.barbells.collectAsState(initial = null)

    CustomizeBarbellsScreenLayout(
        navigator = navigator,
        barbells = barbells,
        onUpdateIsActive = { id, isActive ->
            viewModel.updateIsActive(id, isActive)
        },
        onDeleteBarbell = { id ->
            viewModel.deleteBarbell(id)
        }
    )

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CustomizeBarbellsScreenLayout(
    navigator: Navigator,
    barbells: List<Barbell>?,
    onUpdateIsActive: (id: String, isActive: Boolean) -> Unit,
    onDeleteBarbell: (id: String) -> Unit,
) {
    val collapsingState = rememberCollapsingToolbarScaffoldState()

    CollapsingToolbarScaffold(
        modifier = Modifier.background(ReboundTheme.colors.background),
        scrollStrategy = ScrollStrategy.EnterAlwaysCollapsed,
        state = collapsingState,
        toolbar = {
            TopBar2(
                title = stringResource(id = R.string.barbells),
                toolbarState = collapsingState.toolbarState,
                navigationIcon = {
                    TopBarBackIconButton {
                        navigator.goBack()
                    }
                },
                actions = {
                    TopBarIconButton(
                        icon = Icons.Outlined.Add,
                        title = stringResource(id = R.string.add_barbell),
                        onClick = {
                            navigator.navigate(LeafScreen.BarbellEdit.createRoute())
                        }
                    )
                }
            )
        }) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(ReboundTheme.colors.background),
        ) {
            if (barbells != null) {
                items(items = barbells, key = { it.id }) {
                    BarbellListItemComponent(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItemPlacement(),
                        barbell = it,
                        onUpdateIsActive = { newIsActive ->
                            onUpdateIsActive(it.id, newIsActive)
                        },
                        onEditBarbell = {
                            navigator.navigate(LeafScreen.BarbellEdit.createRoute(it.id))
                        },
                        onDeleteBarbell = {
                            onDeleteBarbell(it.id)
                        }
                    )
                }
            }
        }
    }
}