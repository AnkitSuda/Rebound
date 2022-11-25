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

package com.ankitsuda.rebound.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.ankitsuda.rebound.ui.theme.ReboundTheme

enum class BasicInfoCardType {
    INFO,
    WARNING,
    ERROR,
    SUCCESS;
}

@Composable
fun BasicInfoCard(
    modifier: Modifier = Modifier,
    type: BasicInfoCardType = BasicInfoCardType.INFO,
    iconEnabled: Boolean = true,
    customIcon: ImageVector? = null,
    title: String? = null,
    message: String,
) {
    val icon = customIcon ?: when (type) {
        BasicInfoCardType.INFO -> Icons.Outlined.Info
        BasicInfoCardType.WARNING -> Icons.Outlined.Warning
        BasicInfoCardType.ERROR -> Icons.Outlined.Error
        BasicInfoCardType.SUCCESS -> Icons.Outlined.Done
    }

    AppCard(
        modifier = modifier,
//        backgroundColor = cardColor,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (iconEnabled) {
                Icon(
                    imageVector = icon, contentDescription = null
                )
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                title?.let {
                    Text(
                        text = it,
                        style = ReboundTheme.typography.body1.copy(color = ReboundTheme.colors.onBackground)
                    )
                }
                Text(
                    text = message,
                    style = ReboundTheme.typography.body2.copy(
                        color = ReboundTheme.colors.onBackground.copy(
                            alpha = 0.75f
                        )
                    )
                )
            }
        }
    }
}