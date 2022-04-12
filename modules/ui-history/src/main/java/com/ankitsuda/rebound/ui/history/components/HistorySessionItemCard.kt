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

package com.ankitsuda.rebound.ui.history.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ankitsuda.base.utils.toDurationStr
import com.ankitsuda.rebound.ui.components.AppCard
import com.ankitsuda.rebound.ui.components.SessionCompleteQuickInfo
import com.ankitsuda.rebound.ui.theme.ReboundTheme

/**
 * Dummy content
 */
@Composable
fun HistorySessionItemCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    title: String,
    totalExercises: Int,
    duration: Long?,
    volume: String,
    prs: Int,
) {
    val durationStr: String by rememberSaveable(inputs = arrayOf(duration)) {
        mutableStateOf(duration?.toDurationStr() ?: "NA")
    }

    AppCard(modifier = modifier, onClick = onClick) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, style = ReboundTheme.typography.body1)
            Spacer(Modifier.height(4.dp))
            Text(text = "$totalExercises Exercises", style = ReboundTheme.typography.caption)
            Spacer(Modifier.height(4.dp))
            SessionCompleteQuickInfo(time = durationStr, volume = volume, prs = prs)
        }
    }
}