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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ankitsuda.base.util.isDark
import com.ankitsuda.base.util.toColor
import com.ankitsuda.rebound.domain.entities.Plate
import com.ankitsuda.rebound.ui.theme.ReboundTheme
import timber.log.Timber

@Composable
internal fun PlateItemComponent(plate: Plate) {
    val maxHeightF = 200F
    val maxWidthF = 48F
    val height = ((plate.height ?: 1F) * maxHeightF).dp
    val width = ((plate.width ?: 1F) * maxWidthF).dp

//    val height = 100.dp
//    val width = 200.dp

    val bgColor = plate.color?.toColor() ?: ReboundTheme.colors.onBackground

    val textColor = if (bgColor.isDark()) Color.White else Color.Black

    val text = plate.weight.toString()

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
                .rotate(-90F)
                .align(Alignment.Center),
            text = text,
            color = textColor,
            fontSize = 12.sp,
        )
    }

}