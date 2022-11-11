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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Chip
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ankitsuda.base.util.colorFromSupersetId
import com.ankitsuda.base.util.isDark
import com.ankitsuda.base.util.lighterOrDarkerColor
import com.ankitsuda.base.util.toReadableString
import com.ankitsuda.rebound.domain.*
import com.ankitsuda.rebound.domain.entities.ExerciseLogEntry
import com.ankitsuda.rebound.ui.theme.LocalThemeState
import com.ankitsuda.rebound.ui.theme.ReboundTheme
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.MainAxisAlignment
import com.google.accompanist.flowlayout.SizeMode


private val ExerciseLogEntryComparator = Comparator<ExerciseLogEntry> { left, right ->
    left.setNumber?.compareTo(right.setNumber ?: 0) ?: 0
}

@Composable
fun SessionExerciseCardItem(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    title: String?,
    subtitle: String? = null,
    supersetId: Int? = null,
    entries: List<ExerciseLogEntry>
) {
    val sortedEntries by remember(key1 = entries) {
        mutableStateOf(entries.sortedWith(ExerciseLogEntryComparator))
    }

    fun getRevisedSetNumbers(): List<Pair<String, Color?>> {
        var counter = 0
        val newPairs = sortedEntries.map {
            when (it.setType ?: LogSetType.NORMAL) {
                LogSetType.NORMAL -> {
                    counter++
                    Pair(counter.toString(), null)
                }
                LogSetType.WARM_UP -> Pair("W", Color.Yellow)
                LogSetType.DROP_SET -> {
                    counter++
                    Pair("D", Color.Magenta)
                }
                LogSetType.FAILURE -> {
                    counter++
                    Pair("F", Color.Red)
                }
            }
        }

        return newPairs
    }

    val revisedSetsTexts by remember(key1 = sortedEntries) {
        mutableStateOf(getRevisedSetNumbers())
    }

    AppCard(modifier = modifier, onClick = onClick) {
        Column(modifier = Modifier.padding(16.dp)) {
            supersetId?.let {
                val supersetColor = colorFromSupersetId(it)
                Box(
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .background(supersetColor, ReboundTheme.shapes.small)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.superset),
                        style = ReboundTheme.typography.body1,
                        color = if (supersetColor.isDark()) Color.White else Color.Black,
                    )
                }
            }
            title?.let {
                Text(
                    text = it, style = ReboundTheme.typography.body1,
                    color = LocalThemeState.current.onBackgroundColor
                )
                RSpacer(8.dp)
            }
            subtitle?.let {
                Text(
                    text = it, style = ReboundTheme.typography.body2,
                    color = LocalThemeState.current.onBackgroundColor.copy(alpha = 0.75f)
                )
                RSpacer(8.dp)
            }
            if (sortedEntries.isNotEmpty()) {
                for (i in sortedEntries.indices) {
                    val entry = sortedEntries[i]
                    SessionExerciseSetItem(
                        entry = entry,
                        revisedSetText = revisedSetsTexts[sortedEntries.indexOf(entry)],
                    )
                    if (i != sortedEntries.size - 1) {
                        RSpacer(8.dp)
                    }
                }
            }
        }
    }
}

@Composable
fun SessionExerciseSetItem(
    entry: ExerciseLogEntry,
    revisedSetText: Pair<String, Color?>,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(ReboundTheme.colors.card.lighterOrDarkerColor(0.10f)),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = revisedSetText.first,
                    style = ReboundTheme.typography.caption,
                    color = revisedSetText.second ?: LocalThemeState.current.onBackgroundColor,
                    textAlign = TextAlign.Center,
                )
            }

            RSpacer(16.dp)
            // TODO: Move to strings.xml
            Text(text = buildAnnotatedString {
                withStyle(style = SpanStyle(ReboundTheme.colors.onBackground)) {
                    append((entry.weight ?: 0.0).toReadableString())
                }
                withStyle(style = SpanStyle(ReboundTheme.colors.onBackground.copy(alpha = 0.65f))) {
                    append(" kg")
                }
            })
            RSpacer(20.dp)
            Text(text = buildAnnotatedString {
                withStyle(style = SpanStyle(ReboundTheme.colors.onBackground)) {
                    append((entry.reps ?: 0).toString())
                }
                withStyle(style = SpanStyle(ReboundTheme.colors.onBackground.copy(alpha = 0.65f))) {
                    append(" reps")
                }
            })
        }

        if (!entry.personalRecords.isNullOrEmpty()) {
            PersonalRecordsRowComponent(
                modifier = Modifier
                    .padding(top = 8.dp, start = 32.dp, end = 16.dp),
                prs = entry.personalRecords!!
            )
        }
    }
}

@Composable
fun PersonalRecordsRowComponent(
    modifier: Modifier,
    prs: List<PersonalRecord>
) {
    FlowRow(
        modifier = modifier,
        mainAxisSpacing = 8.dp,
        crossAxisSpacing = 8.dp,
        mainAxisSize = SizeMode.Expand,
        content = {
            for (pr in prs) {
                PersonalRecordComponent(
                    pr = pr
                )
            }
        }
    )
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PersonalRecordComponent(
    pr: PersonalRecord
) {
    val readableText = when (pr) {
        is BestPacePR -> stringResource(R.string.best_pace)
        is MaxDistancePR -> stringResource(R.string.max_distance)
        is MaxDurationPR -> stringResource(R.string.max_duration)
        is MaxOneRmPR -> stringResource(R.string.max_one_rm)
        is MaxRepsPR -> stringResource(R.string.max_reps)
        is MaxVolumeAddedPR -> stringResource(R.string.max_volume_added)
        is MaxVolumePR -> stringResource(R.string.max_volume)
        is MaxWeightAddedPR -> stringResource(R.string.max_weight_added)
        is MaxWeightPR -> stringResource(R.string.max_weight)
        else -> pr.value
    }

    val contentColor = ReboundTheme.colors.primary
    val bgColor = contentColor.copy(0.1f)

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(bgColor)
    ) {
        Row(
            modifier = Modifier.padding(vertical = 6.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(16.dp),
                imageVector = Icons.Outlined.EmojiEvents,
                tint = contentColor,
                contentDescription = null
            )
            RSpacer(space = 4.dp)
            Text(
                text = readableText,
                color = contentColor,
                fontWeight = FontWeight.Medium,
                style = ReboundTheme.typography.body1,
                fontSize = 14.sp,
            )
        }
    }
}