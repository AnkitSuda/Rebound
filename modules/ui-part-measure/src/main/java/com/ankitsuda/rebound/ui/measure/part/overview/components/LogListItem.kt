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

package com.ankitsuda.rebound.ui.measure.part.overview.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.ankitsuda.base.util.toReadableString
import com.ankitsuda.common.compose.kgToUserPrefStr
import com.ankitsuda.common.compose.userPrefWeightUnitStr
import com.ankitsuda.rebound.domain.entities.BodyPart
import com.ankitsuda.rebound.domain.entities.BodyPartMeasurementLog
import com.ankitsuda.rebound.domain.entities.BodyPartUnitType
import com.ankitsuda.rebound.ui.measure.part.R
import com.ankitsuda.rebound.ui.theme.ReboundTheme
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
internal fun LogListItem(
    modifier: Modifier,
    bodyPart: BodyPart?,
    log: BodyPartMeasurementLog,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(horizontal = 24.dp, vertical = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = log.createdAt?.format(
                    DateTimeFormatter.ofLocalizedDateTime(
                        FormatStyle.MEDIUM,
                        FormatStyle.SHORT
                    )
                ) ?: "",
                style = ReboundTheme.typography.caption,
                color = ReboundTheme.colors.onBackground.copy(alpha = 0.5f),
                modifier = Modifier
            )
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(ReboundTheme.colors.onBackground)) {
                        append(
                            when (bodyPart?.unitType) {
                                BodyPartUnitType.WEIGHT -> log.measurement.kgToUserPrefStr()
                                else -> log.measurement.toReadableString()
                            }
                        )
                    }
                    withStyle(style = SpanStyle(ReboundTheme.colors.onBackground.copy(alpha = 0.65f))) {
                        append(
                            " ${
                                when (bodyPart?.unitType) {
                                    BodyPartUnitType.WEIGHT -> userPrefWeightUnitStr()
                                    BodyPartUnitType.CALORIES -> stringResource(id = R.string.kcal)
                                    BodyPartUnitType.PERCENTAGE -> "%"
                                    BodyPartUnitType.LENGTH -> stringResource(id = R.string.inch_short)
                                    else -> ""
                                }
                            }"
                        )
                    }
                },
                style = ReboundTheme.typography.body2
            )
        }
    }
}