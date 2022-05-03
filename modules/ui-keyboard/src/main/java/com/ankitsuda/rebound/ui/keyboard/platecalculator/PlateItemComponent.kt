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

package com.ankitsuda.rebound.ui.keyboard.platecalculator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ankitsuda.base.util.isDark
import com.ankitsuda.base.util.toColor
import com.ankitsuda.base.util.toReadableString
import com.ankitsuda.rebound.domain.entities.Plate
import com.ankitsuda.rebound.ui.theme.ReboundTheme
import timber.log.Timber

@Composable
internal fun PlateItemComponent(maxHeightF: Float, plate: Plate) {
    val maxWidthF = 40F
    val height = ((plate.height ?: 1F) * maxHeightF).dp
    val width = ((plate.width ?: 1F) * maxWidthF).dp

    val bgColor = plate.color?.toColor() ?: ReboundTheme.colors.onBackground

    val textColor = if (bgColor.isDark()) Color.White else Color.Black

    val text = plate.weight?.toReadableString() ?: ""

    Timber.d("width $width height $height")

    Box(
        modifier = Modifier
            .size(
                width = width,
                height = height
            )
            .clip(RoundedCornerShape(2.dp))
            .background(color = bgColor),
    ) {
        Text(
            modifier = Modifier
                .layout { measurable, constraints ->
                    val placeable = measurable.measure(constraints)
                    layout(placeable.height, placeable.width) {
                        placeable.place(
                            x = -(placeable.width / 2 - placeable.height / 2),
                            y = -(placeable.height / 2 - placeable.width / 2)
                        )
                    }
                }
                .rotate(-90F)
                .align(Alignment.Center),
            text = text,
            color = textColor,
            fontSize = 12.sp,
            maxLines = 1,
            overflow = TextOverflow.Visible
        )
    }

}