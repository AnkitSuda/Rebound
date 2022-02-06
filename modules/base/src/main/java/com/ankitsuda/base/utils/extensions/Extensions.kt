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

package com.ankitsuda.base.utils.extensions

import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES
import android.os.Bundle
import java.util.*
import kotlin.math.max

inline val <T : Any> T.simpleName: String get() = this.javaClass.kotlin.simpleName ?: "Unknown"

fun now() = System.currentTimeMillis()
fun nowNano() = System.nanoTime()

fun Array<out Any>.asString(): String {
    return joinToString { it.toString() }
}