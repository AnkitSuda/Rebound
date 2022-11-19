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
import com.ankitsuda.base.util.*
import com.ankitsuda.rebound.domain.DistanceUnit
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

/**
 * @param case 0=Pascal Case, 1=Upper Case, 2=Lower Case
 */
@Composable
fun WeightUnit.localizedStr(case: Int = 2): String = when (this) {
    WeightUnit.KG -> stringResource(
        id = when (case) {
            1 -> R.string.kg_uppercase
            2 -> R.string.kg_lowercase
            else -> R.string.kg
        }
    )
    WeightUnit.LBS -> stringResource(
        id = when (case) {
            1 -> R.string.lbs_uppercase
            2 -> R.string.lbs_lowercase
            else -> R.string.lbs
        }
    )
}

/**
 * @param case 0=Pascal Case, 1=Upper Case, 2=Lower Case
 */
@Composable
fun userPrefWeightUnitStr(case: Int = 2): String = LocalAppSettings.current.weightUnit.localizedStr(case = case)

@Composable
fun Double?.kmToUserPrefStr(
    distanceUnit: DistanceUnit = LocalAppSettings.current.distanceUnit,
    addUnitSuffix: Boolean = false,
    spaceBeforeSuffix: Boolean = false
): String =
    when (distanceUnit) {
        DistanceUnit.KM -> (this ?: 0.0).kmToReadable()
        DistanceUnit.MILES -> (this ?: 0.0).fromKmToMilesReadable()
    } + if (addUnitSuffix) {
        if (spaceBeforeSuffix) {
            " "
        } else {
            ""
        } + userPrefDistanceUnitStr()
    } else {
        ""
    }

/**
 * @param case 0=Pascal Case, 1=Upper Case, 2=Lower Case
 */
@Composable
fun DistanceUnit.localizedStr(case: Int = 2): String = when (this) {
    DistanceUnit.KM -> stringResource(
        id = when (case) {
            1 -> R.string.km_uppercase
            2 -> R.string.km_lowercase
            else -> R.string.km
        }
    )
    DistanceUnit.MILES -> stringResource(
        id = when (case) {
            1 -> R.string.miles_uppercase
            2 -> R.string.miles_lowercase
            else -> R.string.miles
        }
    )
}

/**
 * @param case 0=Pascal Case, 1=Upper Case, 2=Lower Case
 */
@Composable
fun userPrefDistanceUnitStr(case: Int = 2): String =
    LocalAppSettings.current.distanceUnit.localizedStr(case = case)