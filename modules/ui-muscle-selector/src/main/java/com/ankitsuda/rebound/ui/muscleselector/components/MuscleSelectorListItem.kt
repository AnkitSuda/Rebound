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

package com.ankitsuda.rebound.ui.muscleselector.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ankitsuda.base.util.kgToReadable
import com.ankitsuda.base.util.lbsToReadable
import com.ankitsuda.common.compose.R
import com.ankitsuda.common.compose.localizedStr
import com.ankitsuda.rebound.domain.WeightUnit
import com.ankitsuda.rebound.domain.entities.Muscle
import com.ankitsuda.rebound.ui.components.listitems.GenericListItemStyle1
import com.ankitsuda.rebound.ui.theme.ReboundTheme

@Composable
internal fun MuscleSelectorListItem(
    modifier: Modifier = Modifier,
    muscle: Muscle,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    GenericListItemStyle1(
        modifier = modifier,
        title = muscle.name,
        isSelected = isSelected,
        onClick = onClick
    )
}