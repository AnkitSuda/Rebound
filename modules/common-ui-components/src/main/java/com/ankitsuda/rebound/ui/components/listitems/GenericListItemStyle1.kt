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

package com.ankitsuda.rebound.ui.components.listitems

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ankitsuda.common.compose.R
import com.ankitsuda.rebound.ui.theme.ReboundTheme

@Composable
fun GenericListItemStyle1(
    modifier: Modifier = Modifier,
    title: String?,
    description: String? = null,
    isSelected: Boolean = false,
    layoutBelowDescription: (@Composable () -> Unit)? = null,
    onClick: (() -> Unit)? = null
) {

    Box(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .run {
                    if (onClick != null) {
                        clickable(onClick = onClick)
                    } else {
                        this
                    }
                }
                .padding(start = 24.dp, end = 24.dp, bottom = 12.dp, top = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                title?.let {
                    Text(
                        text = title,
                        style = ReboundTheme.typography.body1,
                        color = ReboundTheme.colors.onBackground
                    )
                }
                description?.let {
                    Text(
                        text = it,
                        style = ReboundTheme.typography.body2,
                        color = ReboundTheme.colors.onBackground.copy(alpha = 0.75f)
                    )
                }
                layoutBelowDescription?.let {
                    it()
                }
            }

            if (isSelected) {
                Icon(
                    imageVector = Icons.Outlined.Check,
                    contentDescription = stringResource(R.string.selected),
                    tint = ReboundTheme.colors.primary
                )
            }
        }

    }
}