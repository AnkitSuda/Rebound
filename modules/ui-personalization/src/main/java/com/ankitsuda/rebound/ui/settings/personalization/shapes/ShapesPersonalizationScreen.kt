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

package com.ankitsuda.rebound.ui.settings.personalization.shapes


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Restore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ankitsuda.base.ui.ThemeState
import com.ankitsuda.common.compose.LocalDialog
import com.ankitsuda.common.compose.rememberFlowWithLifecycle
import com.ankitsuda.rebound.ui.ThemeViewModel
import com.ankitsuda.rebound.ui.components.TopBar2
import com.ankitsuda.rebound.ui.components.TopBarBackIconButton
import com.ankitsuda.rebound.ui.components.TopBarIconButton
import com.ankitsuda.rebound.ui.components.settings.ShapesEditorCardItem
import com.ankitsuda.rebound.ui.settings.personalization.R
import com.ankitsuda.rebound.ui.theme.ReboundTheme
import com.ankitsuda.rebound.ui.theme.ShapeValues
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState

@Composable
fun ShapesPersonalizationScreen(
    navController: NavController,
    themeViewModel: ThemeViewModel = hiltViewModel(),
) {
    val themeState by rememberFlowWithLifecycle(themeViewModel.themeState).collectAsState(initial = null)

    themeState?.let { theme ->
        ShapesPersonalizationScreen(
            navController = navController,
            themeState = theme,
            setThemeState = themeViewModel::applyThemeState,
        )

    }
}

@Composable
fun ShapesPersonalizationScreen(
    navController: NavController,
    themeState: ThemeState,
    setThemeState: (ThemeState) -> Unit,
) {
    val collapsingState = rememberCollapsingToolbarScaffoldState()

    CollapsingToolbarScaffold(
        scrollStrategy = ScrollStrategy.EnterAlwaysCollapsed,
        state = collapsingState,
        toolbar = {
            TopBar2(
                title = stringResource(R.string.shapes),
                toolbarState = collapsingState.toolbarState,
                navigationIcon = {
                    TopBarBackIconButton {
                        navController.popBackStack()
                    }
                },
                actions = {
                    TopBarIconButton(icon = Icons.Outlined.Restore, title = stringResource(R.string.reset_to_defaults)) {

                    }
                }
            )
        },
        modifier = Modifier.background(ReboundTheme.colors.background)
    ) {

        val itemModifier = Modifier
            .fillMaxWidth()


        with(LocalDialog.current) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(ReboundTheme.colors.background),
            ) {


                item {

                    ShapesEditorCardItem(
                        modifier = itemModifier,
                        text = stringResource(R.string.small_shape),
                        defaultValues = ShapeValues(
                            topStart = themeState.shapeSmallTopStartRadius,
                            topEnd = themeState.shapeSmallTopEndRadius,
                            bottomStart = themeState.shapeSmallBottomStartRadius,
                            bottomEnd = themeState.shapeSmallBottomEndRadius,
                        ),
                        onValueChange = {
                            setThemeState(
                                themeState.copy(
                                    shapeSmallTopStartRadius = it.topStart,
                                    shapeSmallTopEndRadius = it.topEnd,
                                    shapeSmallBottomStartRadius = it.bottomStart,
                                    shapeSmallBottomEndRadius = it.bottomEnd
                                )
                            )
                        }
                    )

                }


            }
        }
    }
}
