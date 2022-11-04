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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.ankitsuda.base.util.lighterOrDarkerColor
import com.ankitsuda.rebound.ui.components.modifiers.dashedBorder
import com.ankitsuda.rebound.ui.theme.ReboundTheme

@Composable
fun RButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    elevation: ButtonElevation? = ButtonDefaults.elevation(),
    shape: Shape = ReboundTheme.shapes.small,
    border: BorderStroke? = null,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        interactionSource = interactionSource,
        elevation = elevation,
        shape = shape,
        border = border,
        colors = colors,
        contentPadding = contentPadding,
        content = content,
    )
}

@Composable
fun RButtonStyle2(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = ReboundTheme.shapes.small,
    border: BorderStroke? = null,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    text: String,
    icon: ImageVector? = null,
    onClick: () -> Unit,
) {
    RButton(
        modifier = modifier,
        elevation = ButtonDefaults.elevation(0.dp, 0.dp, 0.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = ReboundTheme.colors.background.lighterOrDarkerColor(0.05f),
            contentColor = ReboundTheme.colors.onBackground
        ),
        onClick = onClick,
        enabled = enabled,
        interactionSource = interactionSource,
        shape = shape,
        border = border,
        contentPadding = contentPadding,
    ) {
        icon?.let {
            Icon(imageVector = icon, contentDescription = null)
            RSpacer(space = 8.dp)
        }
        Text(text = text)
    }
}

@Composable
fun RTextButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = ReboundTheme.shapes.small,
    colors: ButtonColors = ButtonDefaults.textButtonColors(),
    border: BorderStroke? = null,
    contentPadding: PaddingValues = ButtonDefaults.TextButtonContentPadding,
    text: String,
    icon: ImageVector? = null,
    onClick: () -> Unit,
) {
    TextButton(
        modifier = modifier,
        colors = colors,
        onClick = onClick,
        enabled = enabled,
        interactionSource = interactionSource,
        shape = shape,
        border = border,
        contentPadding = contentPadding,
    ) {
        icon?.let {
            Icon(imageVector = icon, contentDescription = null)
            RSpacer(space = 8.dp)
        }
        Text(text = text)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RDashedButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = ReboundTheme.shapes.small,
    color: Color = ReboundTheme.colors.primary,
    contentPadding: PaddingValues = PaddingValues(
        horizontal = 16.dp,
        vertical = 12.dp
    ),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    text: String,
    icon: ImageVector? = null,
    onClick: () -> Unit,
) {
    CompositionLocalProvider(
        LocalContentColor provides color,
        LocalContentAlpha provides color.alpha
    ) {
        Box(
            modifier = modifier
                .dashedBorder(
                    width = 1.dp,
                    color = color,
                    shape = shape,
                    on = 6.dp,
                    off = 2.dp,
                )
                .clip(shape)
                .clickable(
                    interactionSource = interactionSource,
                    indication = rememberRipple(),
                    enabled = enabled,
                    role = Role.Button,
                    onClick = onClick
                ),
//        contentColor = color,
//        onClick = onClick
        ) {
            ProvideTextStyle(
                value = ReboundTheme.typography.button
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(contentPadding),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    icon?.let {
                        Icon(imageVector = icon, contentDescription = null, tint = color)
                        RSpacer(space = 8.dp)
                    }
                    Text(text = text, color = color)
                }
            }
        }
    }
}

@Composable
fun BottomSheetRButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    elevation: ButtonElevation? = null,
    shape: Shape = ReboundTheme.shapes.small,
    border: BorderStroke? = null,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit
) {
    RButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        interactionSource = interactionSource,
        elevation = elevation,
        shape = shape,
        border = border,
        colors = colors,
        contentPadding = contentPadding,
        content = content,
    )
}

@Composable
fun BottomSheetSecondaryRButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    elevation: ButtonElevation? = null,
    shape: Shape = MaterialTheme.shapes.small,
    border: BorderStroke? = null,
    colors: ButtonColors = ButtonDefaults.textButtonColors(),
    contentPadding: PaddingValues = ButtonDefaults.TextButtonContentPadding,
    content: @Composable RowScope.() -> Unit
) {
    TextButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        interactionSource = interactionSource,
        elevation = elevation,
        shape = shape,
        border = border,
        colors = colors,
        contentPadding = contentPadding,
        content = content,
    )
}
