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

package com.ankitsuda.common.compose

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.ankitsuda.base.util.toast

@Composable
fun toast(
    message: String,
    duration: Int = Toast.LENGTH_SHORT,
    block: Toast.() -> Unit = {}
) {
    val context = LocalContext.current
    Toast
        .makeText(context, message, duration)
        .apply {
            block(this)
            show()
        }
}