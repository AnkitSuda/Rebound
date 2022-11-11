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

import androidx.compose.foundation.BorderStroke
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ankitsuda.rebound.ui.theme.ReboundTheme

/**
 * This card matches look and feel the app and user choices
 * @param content card body
 */
@Composable
fun AppCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color = ReboundTheme.colors.card,
    content: @Composable () -> Unit
) {

    val border = if (ReboundTheme.dimens.cardBorderWidth == 0.dp) null else BorderStroke(
        width = ReboundTheme.dimens.cardBorderWidth,
        color = ReboundTheme.colors.cardBorder
    )
    Card(
        modifier = modifier,
        elevation = ReboundTheme.dimens.cardElevation,
        shape = MaterialTheme.shapes.medium,
        backgroundColor = backgroundColor,
        border = border,
        content = content
    )
}

/**
 * This card matches look and feel the app and user choices,
 * User can interact with this card.
 * @param onClick callback when card is clicked
 * @param content card body
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AppCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color = ReboundTheme.colors.card,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {

    val border = if (ReboundTheme.dimens.cardBorderWidth == 0.dp) null else BorderStroke(
        width = ReboundTheme.dimens.cardBorderWidth,
        color = ReboundTheme.colors.cardBorder
    )

    val elevation = ReboundTheme.dimens.cardElevation
    val shape = MaterialTheme.shapes.medium

    if (onClick != null) {
        Card(
            modifier = modifier,
            elevation = elevation,
            shape = shape,
            backgroundColor = backgroundColor,
            border = border,
            onClick = onClick,
            content = content
        )
    } else {
        Card(
            modifier = modifier,
            elevation = elevation,
            shape = shape,
            backgroundColor = backgroundColor,
            border = border,
            content = content
        )
    }
}