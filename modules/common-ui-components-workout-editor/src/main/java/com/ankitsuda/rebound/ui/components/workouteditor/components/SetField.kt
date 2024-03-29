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

package com.ankitsuda.rebound.ui.components.workouteditor.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.ankitsuda.base.util.fromLbsToKg
import com.ankitsuda.base.util.fromMilesToKm
import com.ankitsuda.base.util.toReadableString
import com.ankitsuda.common.compose.*
import com.ankitsuda.rebound.domain.DistanceUnit
import com.ankitsuda.rebound.domain.SetFieldValueType
import com.ankitsuda.rebound.domain.WeightUnit
import com.ankitsuda.rebound.domain.entities.Barbell
import com.ankitsuda.rebound.domain.entities.Exercise
import com.ankitsuda.rebound.domain.entities.ExerciseLogEntry
import com.ankitsuda.rebound.ui.keyboard.enums.ReboundKeyboardType
import com.ankitsuda.rebound.ui.keyboard.field.ReboundSetTextField
import com.ankitsuda.rebound.ui.theme.ReboundTheme

@Composable
internal fun RowScope.SetFieldTitleColumns(
    exercise: Exercise,
) {
    Text(
        text = stringResource(id = R.string.set_uppercase),
        style = ReboundTheme.typography.caption,
        color = ReboundTheme.colors.onBackground.copy(alpha = 0.5f),
        textAlign = TextAlign.Center,
        modifier = Modifier.weight(0.5f)
    )
    //            Text(
//                text = "PREVIOUS",
//                style = ReboundTheme.typography.caption,
//                color = ReboundTheme.colors.onBackground.copy(alpha = 0.5f),
//                textAlign = TextAlign.Center,
//                modifier = Modifier.weight(1.5f)
//            )

    for (field in exercise.category?.fields ?: emptyList()) {
        Text(
            text = when (field) {
                SetFieldValueType.WEIGHT,
                SetFieldValueType.ADDITIONAL_WEIGHT,
                SetFieldValueType.ASSISTED_WEIGHT -> userPrefWeightUnitStr(1)
                SetFieldValueType.REPS -> stringResource(id = R.string.reps_uppercase)
                SetFieldValueType.RPE -> stringResource(id = R.string.rpe)
                SetFieldValueType.DISTANCE -> userPrefDistanceUnitStr(case = 1)
                SetFieldValueType.DURATION -> stringResource(id = R.string.duration_uppercase)
            },
            style = ReboundTheme.typography.caption,
            color = ReboundTheme.colors.onBackground.copy(alpha = 0.5f),
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(
                when (field) {
                    SetFieldValueType.RPE -> .75f
                    else -> 1.25f
                }
            )
        )
    }

//    val column1Type = getTypeByColumnAndCategory(
//        column = 1,
//        category = exercise.category
//    )
//    val column2Type = getTypeByColumnAndCategory(
//        column = 1,
//        category = exercise.category
//    )
//    val column3Type = getTypeByColumnAndCategory(
//        column = 1,
//        category = exercise.category
//    )
//
//    if (column1Type != -1) {
//        Text(
//            text = when (column1Type) {
//                0 -> {
//                    userPrefWeightUnitStr(case = 1)
//                }
//                1 -> {
//                    userPrefDistanceUnitStr(case = 1)
//                }
//                else -> "?"
//            },
//            style = ReboundTheme.typography.caption,
//            color = ReboundTheme.colors.onBackground.copy(alpha = 0.5f),
//            textAlign = TextAlign.Center,
//            modifier = Modifier.weight(1.25f)
//        )
//    }
//
//    if (column2Type != -1) {
//        Text(
//            text = when (column2Type) {
//                0 -> {
//                    stringResource(id = R.string.reps_uppercase)
//                }
//                1 -> {
//                    stringResource(id = R.string.time_uppercase)
//                }
//                2 -> {
//                    userPrefDistanceUnitStr(case = 1)
//                }
//                else -> ""
//            },
//            style = ReboundTheme.typography.caption,
//            color = ReboundTheme.colors.onBackground.copy(alpha = 0.5f),
//            textAlign = TextAlign.Center,
//            modifier = Modifier.weight(1.25f)
//        )
//    }
//
//    if (column3Type != -1) {
//        Text(
//            text = stringResource(id = R.string.rpe),
//            style = ReboundTheme.typography.caption,
//            color = ReboundTheme.colors.onBackground.copy(alpha = 0.5f),
//            textAlign = TextAlign.Center,
//            modifier = Modifier.weight(.75f)
//        )
//    }
}

@Composable
internal fun RowScope.SetFieldColumns(
    contentColor: Color,
    bgColor: Color,
    exercise: Exercise,
    barbell: Barbell?,
    exerciseLogEntry: ExerciseLogEntry,
    onWeightChange: (ExerciseLogEntry, Double?) -> Unit,
    onDistanceChange: (ExerciseLogEntry, Double?) -> Unit,
    onRepsChange: (ExerciseLogEntry, Int?) -> Unit,
    onDurationChange: (ExerciseLogEntry, Long?) -> Unit,
    onRpeChange: (ExerciseLogEntry, Float?) -> Unit,
) {
    for (field in exercise.category?.fields ?: emptyList()) {
        SetField(
            contentColor = contentColor,
            bgColor = bgColor,
            barbell = barbell,
            entry = exerciseLogEntry,
            fieldType = field,
            onWeightChange = onWeightChange,
            onDistanceChange = onDistanceChange,
            onRepsChange = onRepsChange,
            onDurationChange = onDurationChange,
            onRpeChange = onRpeChange,
        )
    }
//    SetFieldColumn1(
//        contentColor = contentColor,
//        bgColor = bgColor,
//        exercise = exercise,
//        barbell = barbell,
//        exerciseLogEntry = exerciseLogEntry,
//        onWeightChange = onWeightChange,
//        onDistanceChange = onDistanceChange,
//    )
//    SetFieldColumn2(
//        contentColor = contentColor,
//        bgColor = bgColor,
//        exercise = exercise,
//        barbell = barbell,
//        exerciseLogEntry = exerciseLogEntry,
//        onRepsChange = onRepsChange,
//        onDistanceChange = onDistanceChange,
//        onDurationChange = onDurationChange,
//    )
//    SetFieldColumn3(
//        contentColor = contentColor,
//        bgColor = bgColor,
//        exercise = exercise,
//        barbell = barbell,
//        exerciseLogEntry = exerciseLogEntry,
//        onRpeChange = onRpeChange,
//    )
}

//@Composable
//internal fun RowScope.SetFieldColumn1(
//    contentColor: Color,
//    bgColor: Color,
//    exercise: Exercise,
//    barbell: Barbell?,
//    exerciseLogEntry: ExerciseLogEntry,
//    onWeightChange: (ExerciseLogEntry, Double?) -> Unit,
//    onDistanceChange: (ExerciseLogEntry, Double?) -> Unit,
//) {
//    val type = remember {
//        getTypeByColumnAndCategory(
//            column = 1,
//            category = exercise.category
//        )
//    }
//
//    if (type != -1) {
//        val currentWeightUnit = LocalAppSettings.current.weightUnit
//        val currentDistanceUnit = LocalAppSettings.current.distanceUnit
//
//        val fieldValue = when (type) {
//            0 -> {
//                exerciseLogEntry.weight?.kgToUserPrefStr() ?: ""
//            }
//            1 -> {
//                exerciseLogEntry.distance?.kmToUserPrefStr() ?: ""
//            }
//            else -> ""
//        }
//
//        fun handleOnValueChange(value: String) {
//            when (type) {
//                0 -> {
//                    var newValue =
//                        (if (value.isBlank()) null else value.trim()/*.filter { it.isDigit() }*/
//                            .toDoubleOrNull())
//
//                    if (currentWeightUnit == WeightUnit.LBS) {
//                        newValue = newValue?.fromLbsToKg()
//                    }
//
//                    onWeightChange(exerciseLogEntry, newValue)
//                }
//                1 -> {
//                    var newValue =
//                        (if (value.isBlank()) null else value.trim()
//                            .toDoubleOrNull())
//
//                    if (currentDistanceUnit == DistanceUnit.MILES) {
//                        newValue = newValue?.fromMilesToKm()
//                    }
//
//                    onDistanceChange(exerciseLogEntry, newValue)
//                }
//                else -> {}
//            }
//        }
//
//        ReboundSetTextField(
//            value = fieldValue,
//            onValueChange = ::handleOnValueChange,
//            contentColor = contentColor,
//            bgColor = bgColor,
//            reboundKeyboardType = getReboundKeyboardType(
//                category = exercise.category,
//                column = 1,
//                barbell = barbell,
//            ),
//        )
//
//    }
//
//}

//@Composable
//internal fun RowScope.SetFieldColumn2(
//    contentColor: Color,
//    bgColor: Color,
//    exercise: Exercise,
//    barbell: Barbell?,
//    exerciseLogEntry: ExerciseLogEntry,
//    onRepsChange: (ExerciseLogEntry, Int?) -> Unit,
//    onDistanceChange: (ExerciseLogEntry, Double?) -> Unit,
//    onDurationChange: (ExerciseLogEntry, Long?) -> Unit,
//) {
//    val type = remember {
//        getTypeByColumnAndCategory(
//            column = 2,
//            category = exercise.category
//        )
//    }
//
//    if (type != -1) {
//        val currentDistanceUnit = LocalAppSettings.current.distanceUnit
//
//        val fieldValue = when (type) {
//            0 -> {
//                exerciseLogEntry.reps?.toString() ?: ""
//            }
//            1 -> {
//                exerciseLogEntry.timeRecorded?.toString()/*.toDurationStr()*/ ?: ""
//            }
//            2 -> {
//                exerciseLogEntry.distance?.kmToUserPrefStr() ?: ""
//            }
//            else -> ""
//        }
//
//        fun handleOnValueChange(value: String) {
//            when (type) {
//                0 -> {
//                    val newValue =
//                        (if (value.isBlank()) null else value.trim()/*.filter { it.isDigit() }*/
//                            .toIntOrNull())
//                    onRepsChange(exerciseLogEntry, newValue)
//                }
//                1 -> {
//                    val newValue =
//                        (if (value.isBlank()) null else value.trim()
//                            .toLongOrNull())
//
//                    onDurationChange(exerciseLogEntry, newValue)
//                }
//                2 -> {
//                    var newValue =
//                        (if (value.isBlank()) null else value.trim()
//                            .toDoubleOrNull())
//
//                    if (currentDistanceUnit == DistanceUnit.MILES) {
//                        newValue = newValue?.fromMilesToKm()
//                    }
//
//                    onDistanceChange(exerciseLogEntry, newValue)
//                }
//                else -> {}
//            }
//        }
//
//        ReboundSetTextField(
//            value = fieldValue,
//            onValueChange = ::handleOnValueChange,
//            contentColor = contentColor,
//            bgColor = bgColor,
//            reboundKeyboardType = getReboundKeyboardType(
//                category = exercise.category,
//                column = 1,
//                barbell = barbell,
//            ),
//        )
//
//    }
//
//}

//@Composable
//internal fun RowScope.SetFieldColumn3(
//    contentColor: Color,
//    bgColor: Color,
//    exercise: Exercise,
//    barbell: Barbell?,
//    exerciseLogEntry: ExerciseLogEntry,
//    onRpeChange: (ExerciseLogEntry, Float?) -> Unit,
//) {
//    val type = remember {
//        getTypeByColumnAndCategory(
//            column = 3,
//            category = exercise.category
//        )
//    }
//
//    if (type != -1) {
//        val fieldValue = when (type) {
//            0 -> {
//                exerciseLogEntry.rpe?.toReadableString() ?: ""
//            }
//            else -> ""
//        }
//
//        fun handleOnValueChange(value: String) {
//            when (type) {
//                0 -> {
//                    onRpeChange(exerciseLogEntry, value.toFloatOrNull())
//                }
//                else -> {}
//            }
//        }
//
//        ReboundSetTextField(
//            value = fieldValue,
//            onValueChange = ::handleOnValueChange,
//            contentColor = contentColor,
//            bgColor = bgColor,
//            reboundKeyboardType = getReboundKeyboardType(
//                category = exercise.category,
//                column = 3,
//                barbell = barbell,
//            ),
//        )
//
//    }
//
//}

@Composable
internal fun RowScope.SetFieldWeight(
    contentColor: Color,
    bgColor: Color,
    barbell: Barbell?,
    weight: Double?,
    onWeightChange: (Double?) -> Unit,
) {
    val currentWeightUnit = LocalAppSettings.current.weightUnit

    val fieldValue = weight?.kgToUserPrefStr() ?: ""

    fun handleOnValueChange(value: String) {
        var newValue =
            (if (value.isBlank()) null else value.trim()
                .toDoubleOrNull())

        if (currentWeightUnit == WeightUnit.LBS) {
            newValue = newValue?.fromLbsToKg()
        }

        onWeightChange(newValue)
    }

    ReboundSetTextField(
        value = fieldValue,
        onValueChange = ::handleOnValueChange,
        contentColor = contentColor,
        bgColor = bgColor,
        reboundKeyboardType = ReboundKeyboardType.Weight(
            barbell = barbell,
        ),
    )
}

@Composable
internal fun RowScope.SetFieldDistance(
    contentColor: Color,
    bgColor: Color,
    distance: Double?,
    onDistanceChange: (Double?) -> Unit,
) {
    val currentDistanceUnit = LocalAppSettings.current.distanceUnit

    val fieldValue = distance?.kmToUserPrefStr() ?: ""

    fun handleOnValueChange(value: String) {
        var newValue =
            (if (value.isBlank()) null else value.trim()
                .toDoubleOrNull())

        if (currentDistanceUnit == DistanceUnit.MILES) {
            newValue = newValue?.fromMilesToKm()
        }

        onDistanceChange(newValue)
    }

    ReboundSetTextField(
        value = fieldValue,
        onValueChange = ::handleOnValueChange,
        contentColor = contentColor,
        bgColor = bgColor,
        reboundKeyboardType = ReboundKeyboardType.Distance,
    )
}

@Composable
internal fun RowScope.SetFieldReps(
    contentColor: Color,
    bgColor: Color,
    reps: Int?,
    onRepsChange: (Int?) -> Unit,
) {
    val fieldValue = reps?.toString() ?: ""

    fun handleOnValueChange(value: String) {
        val newValue =
            (if (value.isBlank()) null else value.trim()
                .toIntOrNull())
        onRepsChange(newValue)
    }

    ReboundSetTextField(
        value = fieldValue,
        onValueChange = ::handleOnValueChange,
        contentColor = contentColor,
        bgColor = bgColor,
        reboundKeyboardType = ReboundKeyboardType.Reps,
    )
}

@Composable
internal fun RowScope.SetFieldDuration(
    contentColor: Color,
    bgColor: Color,
    duration: Long?,
    onDurationChange: (Long?) -> Unit,
) {
    val fieldValue = duration?.toString()/*.toDurationStr()*/ ?: ""

    fun handleOnValueChange(value: String) {
        val newValue =
            (if (value.isBlank()) null else value.trim()
                .toLongOrNull())

        onDurationChange(newValue)
    }

    ReboundSetTextField(
        value = fieldValue,
        onValueChange = ::handleOnValueChange,
        contentColor = contentColor,
        bgColor = bgColor,
        reboundKeyboardType = ReboundKeyboardType.Time,
    )
}

@Composable
internal fun RowScope.SetFieldRpe(
    contentColor: Color,
    bgColor: Color,
    rpe: Float?,
    onRpeChange: (Float?) -> Unit,
) {
    val fieldValue = rpe?.toReadableString() ?: ""

    fun handleOnValueChange(value: String) {
        onRpeChange(value.toFloatOrNull())
    }

    ReboundSetTextField(
        layoutWeight = 0.75f,
        value = fieldValue,
        onValueChange = ::handleOnValueChange,
        contentColor = contentColor,
        bgColor = bgColor,
        reboundKeyboardType = ReboundKeyboardType.Rpe,
    )
}

@Composable
internal fun RowScope.SetField(
    contentColor: Color,
    bgColor: Color,
    barbell: Barbell?,
    entry: ExerciseLogEntry,
    fieldType: SetFieldValueType,
    onWeightChange: (ExerciseLogEntry, Double?) -> Unit,
    onDistanceChange: (ExerciseLogEntry, Double?) -> Unit,
    onRepsChange: (ExerciseLogEntry, Int?) -> Unit,
    onDurationChange: (ExerciseLogEntry, Long?) -> Unit,
    onRpeChange: (ExerciseLogEntry, Float?) -> Unit,
) {
    when (fieldType) {
        SetFieldValueType.WEIGHT,
        SetFieldValueType.ADDITIONAL_WEIGHT,
        SetFieldValueType.ASSISTED_WEIGHT -> SetFieldWeight(
            contentColor = contentColor,
            bgColor = bgColor,
            barbell = barbell,
            weight = entry.weight,
            onWeightChange = {
                onWeightChange(entry, it)
            },
        )
        SetFieldValueType.REPS -> SetFieldReps(
            contentColor = contentColor,
            bgColor = bgColor,
            reps = entry.reps,
            onRepsChange = {
                onRepsChange(entry, it)
            }
        )
        SetFieldValueType.DISTANCE -> SetFieldDistance(
            contentColor = contentColor,
            bgColor = bgColor,
            distance = entry.distance,
            onDistanceChange = {
                onDistanceChange(entry, it)
            }
        )
        SetFieldValueType.DURATION -> SetFieldDuration(
            contentColor = contentColor,
            bgColor = bgColor,
            duration = entry.timeRecorded,
            onDurationChange = {
                onDurationChange(entry, it)
            }
        )
        SetFieldValueType.RPE -> SetFieldRpe(
            contentColor = contentColor,
            bgColor = bgColor,
            rpe = entry.rpe,
            onRpeChange = {
                onRpeChange(entry, it)
            }
        )
    }
}
