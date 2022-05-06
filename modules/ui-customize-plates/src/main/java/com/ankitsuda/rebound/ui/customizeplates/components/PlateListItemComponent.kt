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

package com.ankitsuda.rebound.ui.customizeplates.components

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
import androidx.compose.ui.unit.dp
import com.ankitsuda.base.util.toReadableString
import com.ankitsuda.rebound.domain.entities.Plate
import com.ankitsuda.rebound.ui.components.RSpacer

@Composable
internal fun PlateListItemComponent(
    modifier: Modifier = Modifier.fillMaxWidth(),
    plate: Plate,
    onUpdateIsActive: (Boolean) -> Unit
) {
    var isActive by remember(plate.isActive) {
        mutableStateOf(plate.isActive ?: false)
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
                .padding(start = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${plate.weight?.toReadableString()} kg",
            )
            Spacer(modifier = Modifier.weight(1f))
            Switch(
                checked = isActive,
                onCheckedChange = {
                    updateIsActive(it)
                }
            )
            RSpacer(space = 4.dp)
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Outlined.MoreVert, contentDescription = "More")
            }
        }
    }
}