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

package com.ankitsuda.common.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.ankitsuda.base.util.fromKgToLbsReadable
import com.ankitsuda.base.util.kgToReadable
import com.ankitsuda.base.util.toReadableString
import com.ankitsuda.rebound.domain.WeightUnit

@Composable
fun Double?.kgToUserPrefStr(
    weightUnit: WeightUnit = LocalAppSettings.current.weightUnit,
    addUnitSuffix: Boolean = false,
    spaceBeforeSuffix: Boolean = false
): String =
    when (weightUnit) {
        WeightUnit.KG -> (this ?: 0.0).kgToReadable()
        WeightUnit.LBS -> (this ?: 0.0).fromKgToLbsReadable()
    } + if (addUnitSuffix) {
        if (spaceBeforeSuffix) {
            " "
        } else {
            ""
        } + userPrefWeightUnitStr()
    } else {
        ""
    }

@Composable
fun WeightUnit.localizedStr(): String = when (this) {
    WeightUnit.KG -> stringResource(id = R.string.kg)
    WeightUnit.LBS -> stringResource(id = R.string.lbs)
}

@Composable
fun userPrefWeightUnitStr(): String = LocalAppSettings.current.weightUnit.localizedStr()