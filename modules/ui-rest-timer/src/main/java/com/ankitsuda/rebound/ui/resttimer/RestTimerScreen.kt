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

package com.ankitsuda.rebound.ui.resttimer

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.ankitsuda.rebound.resttimer.Constants
import com.ankitsuda.rebound.resttimer.RestTimerService
import com.ankitsuda.rebound.resttimer.TimerState
import com.ankitsuda.rebound.ui.components.BottomSheetSurface
import com.ankitsuda.rebound.ui.resttimer.components.TimerCircleComponent

@Composable
fun RestTimerScreen(
    viewModel: RestTimerScreenViewModel = hiltViewModel()
) {
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp
    val screenHeightDp = configuration.screenHeightDp

    val elapsedTime by viewModel.elapsedTime.observeAsState(0L)
    val totalTime by viewModel.totalTime.observeAsState(0L)
    val timerState by viewModel.timerState.observeAsState(TimerState.EXPIRED)
    val timeString by viewModel.timeString.observeAsState("")

    val context = LocalContext.current

    fun sendCommandToService(action: String) {
        Intent(context, RestTimerService::class.java).also {
            it.action = action
            if (action == Constants.ACTION_START) {
                it.putExtra(Constants.EXTRA_TOTAL_TIME, 10000L)
            }
            context.startService(it)
        }
    }
    BottomSheetSurface {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.fillMaxWidth()) {
                TimerCircleComponent(
                    screenWidthDp = screenWidthDp,
                    screenHeightDp = screenHeightDp,
                    time = timeString,
                    state = timerState.stateName,
                    reps = "1",
                    elapsedTime = elapsedTime,
                    totalTime = totalTime
                )
            }

            Button(onClick = { sendCommandToService(Constants.ACTION_START) }) {
                Text(text = "Start")
            }
            Button(onClick = { sendCommandToService(Constants.ACTION_RESUME) }) {
                Text(text = "Resume")
            }
            Button(onClick = { sendCommandToService(Constants.ACTION_PAUSE) }) {
                Text(text = "Pause")
            }
            Button(onClick = { sendCommandToService(Constants.ACTION_CANCEL) }) {
                Text(text = "Cancel")
            }

        }

    }
}