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

package com.ankitsuda.base.util

import android.content.Context
import android.view.Gravity
import android.widget.Toast
import androidx.annotation.StringRes

/**
 * Display the simple Toast message with the [Toast.LENGTH_SHORT] duration.
 *
 * @param message the message text.
 */
fun Context.toast(
    message: CharSequence,
    duration: Int = Toast.LENGTH_SHORT,
    block: Toast.() -> Unit = {}
): Toast = Toast
    .makeText(this, message, Toast.LENGTH_SHORT)
    .apply {
        block(this)
        show()
    }

fun Context.toast(
    @StringRes messageRes: Int,
    duration: Int = Toast.LENGTH_SHORT,
    block: Toast.() -> Unit = {}
) = toast(getString(messageRes), duration, block)

fun Context.centeredToast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
    toast(message) {
        setGravity(Gravity.CENTER, 0, 0)
    }
}
