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

package com.ankitsuda.rebound.ui.resttimer.components

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.ankitsuda.rebound.resttimer.getFormattedStopWatchTime
import com.ankitsuda.rebound.ui.resttimer.R
import timber.log.Timber
import java.util.concurrent.TimeUnit

@Composable
internal fun TimesListComponent(
    contentPadding: PaddingValues,
    onClickStart: (Long) -> Unit,
) {
    val context = LocalContext.current
    val times by remember {
        mutableStateOf(getTimes(context))
    }

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = contentPadding,
        content = {
            items(times) {
                if (it is Long) {
                    val textStr by rememberSaveable(inputs = arrayOf(it)) {
                        mutableStateOf(getFormattedStopWatchTime(ms = it, spaces = false))
                    }

                    Button(onClick = { onClickStart(it) }) {
                        Text(text = textStr)
                    }
                } else if (it is String) {
                    Text(text = it)
                }
            }
        })
}

fun getTimes(context: Context): List<Any> {
    val list = arrayListOf<Any>()

    list.add(context.getString(R.string.select_time))

    list.add(TimeUnit.MINUTES.toMillis(1))
    list.add(TimeUnit.SECONDS.toMillis(105))
    list.add(TimeUnit.SECONDS.toMillis(110))
    list.add(TimeUnit.MINUTES.toMillis(5))

    list.add(context.getString(R.string.all))

    var lastI = 0
    for (i in 5..600) {
        if (i == lastI + 5) {
            list.add(TimeUnit.SECONDS.toMillis(i.toLong()))
            lastI = i
        }
    }
    return list
}