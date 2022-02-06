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

package com.ankitsuda.rebound.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.ankitsuda.rebound.domain.entities.ExerciseLogEntry
import com.ankitsuda.rebound.ui.theme.ReboundTheme

//import com.ankitsuda.rebound.utils.lighterOrDarkerColor

@Composable
fun SessionExerciseCardItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    exerciseName: String,
    entries: List<ExerciseLogEntry>
) {
    AppCard(modifier = modifier, onClick = onClick) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = exerciseName, style = ReboundTheme.typography.body1)
            RSpacer(8.dp)
            if (entries.isNotEmpty()) {
                for (i in entries.indices) {
                    val entry = entries[i]
                    SessionExerciseSetItem(
                        order = i,
                        set = Pair(entry.weight ?: 0f, entry.reps ?: 0)
                    )
                    if (i != entries.size - 1) {
                        RSpacer(8.dp)
                    }
                }
            }
        }
    }
}

@Composable
fun SessionExerciseSetItem(order: Int, set: Pair<Float, Int>) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
//                .background(Color(224, 224, 224))
                .background(ReboundTheme.colors.card/*.lighterOrDarkerColor(0.15f)*/),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = order.toString()
            )
        }

        RSpacer(16.dp)
        Text(text = buildAnnotatedString {
            append(set.first.toString())
            withStyle(style = SpanStyle(ReboundTheme.colors.onBackground.copy(alpha = 0.65f))) {
                append(" kg")
            }
        })
        RSpacer(20.dp)
        Text(text = buildAnnotatedString {
            append(set.second.toString())
            withStyle(style = SpanStyle(ReboundTheme.colors.onBackground.copy(alpha = 0.65f))) {
                append(" reps")
            }
        })
    }
}