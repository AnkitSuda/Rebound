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

package com.ankitsuda.rebound.ui.components.workouteditor.warmupcalculator

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ankitsuda.base.util.toReadableString
import com.ankitsuda.rebound.domain.ExerciseCategory
import com.ankitsuda.rebound.ui.components.workouteditor.SetSwipeWrapperComponent
import com.ankitsuda.rebound.ui.keyboard.enums.ReboundKeyboardType
import com.ankitsuda.rebound.ui.keyboard.field.ReboundSetTextField
import com.ankitsuda.rebound.ui.keyboard.picker.getAllWarmUpReps
import com.ankitsuda.rebound.ui.keyboard.picker.getAllWarmUpWeights
import com.ankitsuda.rebound.ui.theme.ReboundTheme

@Composable
internal fun WarmUpSetComponent(
    modifier: Modifier = Modifier,
    workSetWeight: Double,
    barWeight: Double,
    startingSet: WarmUpSet,
    onDeleteSet: () -> Unit,
    onChangeValue: (WarmUpSet) -> Unit
) {
    val fieldBgColor = ReboundTheme.colors.background
    val fieldContentColor = ReboundTheme.colors.onBackground

    val setWeight =
        if (startingSet.weightPercentage == -1 || startingSet.weightPercentage == null) {
            barWeight
        } else {
            workSetWeight * startingSet.weightPercentage!! / 100
        }

    SetSwipeWrapperComponent(
        modifier = modifier,
        bgColor = ReboundTheme.colors.background,
        onSwipeDelete = {
            onDeleteSet()
        }
    ) {
        Row(
            modifier = Modifier
                .background(
                    color = ReboundTheme.colors.background
                )
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = "W",
                style = ReboundTheme.typography.caption,
                color = Color.Yellow,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(0.5f)
            )
            ReboundSetTextField(
                value = startingSet.findFormula(),
                contentColor = fieldContentColor,
                bgColor = fieldBgColor,
                reboundKeyboardType = ReboundKeyboardType.WARMUP_SET,
                onValueChange = {
                    val arr = it.split(" x ")
                    val weightInt =
                        getAllWarmUpWeights().find { w -> w.second == arr.getOrNull(0) }?.first
                            ?: -1

                    val weight = if (weightInt == -1) {
                        barWeight
                    } else {
                        workSetWeight * weightInt / 100
                    }

                    val reps = arr.getOrNull(1)?.toIntOrNull() ?: 0

                    onChangeValue(
                        startingSet.copy(
                            weight = weight,
                            reps = reps,
                            weightPercentage = weightInt,
                            formula = it
                        )
                    )
                }
            )
            Text(
                text = "${setWeight.toReadableString()} x ${startingSet.reps ?: 0}",
                style = ReboundTheme.typography.caption,
                color = ReboundTheme.colors.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1.25f)
            )
        }
    }
}