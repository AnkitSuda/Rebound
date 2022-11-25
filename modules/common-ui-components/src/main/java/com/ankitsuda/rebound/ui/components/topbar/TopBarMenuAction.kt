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

package com.ankitsuda.rebound.ui.components.topbar

import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.ankitsuda.rebound.ui.components.R
import com.ankitsuda.rebound.ui.components.TopBarIconButton

@Composable
fun TopBarMenuAction(
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Outlined.MoreVert,
    title: String = stringResource(id = R.string.open_menu),
    enabled: Boolean = true,
    customTint: Color? = null,
    actions: List<MenuActionItem>
) {
    var isMenuExpanded by remember {
        mutableStateOf(false)
    }

    fun openMenu() {
        isMenuExpanded = true
    }

    fun closeMenu() {
        isMenuExpanded = false
    }

    TopBarIconButton(
        modifier = modifier,
        icon = icon,
        title = title,
        enabled = enabled,
        customTint = customTint,
        onClick = ::openMenu
    )

    DropdownMenu(
        modifier = modifier,
        expanded = isMenuExpanded,
        onDismissRequest = ::closeMenu
    ) {
        for (action in actions) {
            DropdownMenuItem(
                onClick = {
                    closeMenu()
                    action.onClick()
                }
            ) {
                Text(action.title)
            }
        }
    }
}