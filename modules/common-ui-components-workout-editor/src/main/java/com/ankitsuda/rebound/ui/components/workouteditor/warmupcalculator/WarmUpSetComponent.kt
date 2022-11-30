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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.LocalAbsoluteElevation
import androidx.compose.material.LocalElevationOverlay
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ankitsuda.base.util.fromKgToLbs
import com.ankitsuda.common.compose.LocalAppSettings
import com.ankitsuda.common.compose.kgToUserPrefStr
import com.ankitsuda.rebound.domain.WeightUnit
import com.ankitsuda.rebound.domain.WeightUnit.*
import com.ankitsuda.rebound.ui.components.workouteditor.SetSwipeWrapperComponent
import com.ankitsuda.rebound.ui.keyboard.enums.ReboundKeyboardType
import com.ankitsuda.rebound.ui.keyboard.field.ReboundSetTextField
import com.ankitsuda.rebound.ui.keyboard.picker.getAllWarmUpWeights
import com.ankitsuda.rebound.ui.theme.ReboundTheme

@Composable
internal fun WarmUpSetComponent(
    modifier: Modifier = Modifier,
    workSetWeightKg: Double,
    barWeightKg: Double,
    startingSet: WarmUpSet,
    onDeleteSet: () -> Unit,
    onChangeValue: (WarmUpSet) -> Unit
) {
    val fieldContentColor = ReboundTheme.colors.onBackground
    val weightUnit = LocalAppSettings.current.weightUnit

    val setWeight =
        if (startingSet.weightPercentage == -1 || startingSet.weightPercentage == null) {
            barWeightKg
        } else {
            workSetWeightKg * startingSet.weightPercentage!! / 100
        }

//    val setWeight = when (weightUnit) {
//        KG -> setWeightKg
//        LBS -> setWeightKg.fromKgToLbs()
//    }

    val bgColor =
        LocalElevationOverlay.current?.apply(
            color = ReboundTheme.colors.background,
            elevation = LocalAbsoluteElevation.current
        )
            ?: ReboundTheme.colors.background

    SetSwipeWrapperComponent(
        modifier = modifier,
        bgColor = bgColor,
        onSwipeDelete = {
            onDeleteSet()
        }
    ) {
        Row(
            modifier = Modifier
                .background(
                    color = bgColor
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
                bgColor = bgColor,
                reboundKeyboardType = ReboundKeyboardType.WarmupSet,
                onValueChange = {
                    val arr = it.split(" x ")
                    val weightInt =
                        getAllWarmUpWeights().find { w -> w.second == arr.getOrNull(0) }?.first
                            ?: -1

                    val weight = if (weightInt == -1) {
                        barWeightKg
                    } else {
                        workSetWeightKg * weightInt / 100
                    }

                    val reps = arr.getOrNull(1)?.toIntOrNull() ?: 1

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
                text = "${setWeight.kgToUserPrefStr(addUnitSuffix = true)} x ${startingSet.reps ?: 0}",
                style = ReboundTheme.typography.caption,
                color = ReboundTheme.colors.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1.25f)
            )
        }
    }
}