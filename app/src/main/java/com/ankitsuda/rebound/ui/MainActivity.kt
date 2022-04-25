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

package com.ankitsuda.rebound.ui

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.core.view.WindowCompat
import com.ankitsuda.rebound.data.datastore.PrefStorage
import com.ankitsuda.rebound.resttimer.RestTimerService
import com.ankitsuda.rebound.ui.main.MainScreen
import com.google.accompanist.insets.ProvideWindowInsets
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var prefStorage: PrefStorage

    private var bound: Boolean = false

    /*This acts as a dummy to trigger onBind/onRebind/onUnbind in TimerService*/
    private val mConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {}
        override fun onServiceDisconnected(className: ComponentName) {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Makes content draw under status bar and navigation bar
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            ProvideWindowInsets {
                // A surface container using the 'background' color from the theme

                Surface(color = MaterialTheme.colors.background) {
                    MainScreen()
                }
            }

        }
    }

    override fun onStart() {
        super.onStart()
        Intent(this, RestTimerService::class.java).also { intent ->
            bindService(intent, mConnection, Context.BIND_AUTO_CREATE)
        }
        bound = true
    }

    override fun onStop() {
        super.onStop()
        // Unbind from the service
        if (bound) {
            //Timber.d("Trying to unbind service")
            unbindService(mConnection)
            bound = false
        }
    }

}