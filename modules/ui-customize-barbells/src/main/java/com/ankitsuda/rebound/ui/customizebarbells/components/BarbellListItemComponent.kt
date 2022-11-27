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

package com.ankitsuda.rebound.ui.customizebarbells.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ankitsuda.base.util.kgToReadable
import com.ankitsuda.base.util.lbsToReadable
import com.ankitsuda.base.util.toReadableString
import com.ankitsuda.common.compose.kgToUserPrefStr
import com.ankitsuda.common.compose.localizedStr
import com.ankitsuda.rebound.domain.WeightUnit
import com.ankitsuda.rebound.domain.entities.Barbell
import com.ankitsuda.rebound.ui.components.RSpacer
import com.ankitsuda.rebound.ui.customizebarbells.R
import com.ankitsuda.rebound.ui.theme.ReboundTheme

@Composable
internal fun BarbellListItemComponent(
    modifier: Modifier = Modifier,
    barbell: Barbell,
    onUpdateIsActive: (Boolean) -> Unit,
    onDeleteBarbell: () -> Unit,
    onEditBarbell: () -> Unit
) {
    var isActive by remember(barbell.isActive) {
        mutableStateOf(barbell.isActive ?: false)
    }

    var menuExpanded by remember {
        mutableStateOf(false)
    }

    fun updateIsActive(active: Boolean) {
        isActive = active
        onUpdateIsActive(active)
    }

    Box(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .padding(start = 24.dp, end = 8.dp, bottom = 12.dp, top = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = barbell.name ?: "",
                    style = ReboundTheme.typography.body1,
                    color = ReboundTheme.colors.onBackground
                )
                Text(
                    text = "${
                        barbell.weightKg?.kgToReadable()
                    } ${WeightUnit.KG.localizedStr()} / ${
                        barbell.weightLbs?.lbsToReadable()
                    } ${WeightUnit.LBS.localizedStr()}",
                    style = ReboundTheme.typography.body2,
                    color = ReboundTheme.colors.onBackground.copy(alpha = 0.75f)
                )
            }
            Switch(
                checked = isActive,
                onCheckedChange = {
                    updateIsActive(it)
                }
            )
            RSpacer(space = 4.dp)
            IconButton(onClick = {
                menuExpanded = true
            }) {
                Icon(
                    imageVector = Icons.Outlined.MoreVert,
                    contentDescription = stringResource(R.string.more),
                    tint = ReboundTheme.colors.onBackground
                )

                if (menuExpanded) {
                    BarbellMenuComponent(
                        expanded = menuExpanded,
                        onDismissRequest = {
                            menuExpanded = false
                        },
                        onEditBarbell = onEditBarbell,
                        onDeleteBarbell = onDeleteBarbell
                    )
                }

            }
        }

    }
}