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

package com.ankitsuda.rebound.ui.exercisecategoryselector.components

import androidx.compose.material.Chip
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ankitsuda.common.compose.toI18NString
import com.ankitsuda.common.compose.toI18NStringExamples
import com.ankitsuda.rebound.domain.ExerciseCategory
import com.ankitsuda.rebound.ui.components.listitems.GenericListItemStyle1
import com.google.accompanist.flowlayout.FlowRow

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun ExerciseCategorySelectorListItem(
    modifier: Modifier = Modifier,
    exerciseCategory: ExerciseCategory,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    GenericListItemStyle1(
        modifier = modifier,
        title = exerciseCategory.toI18NString(),
        description = exerciseCategory.toI18NStringExamples(),
        isSelected = isSelected,
        onClick = onClick,
        layoutBelowDescription = {
        }
    )
}