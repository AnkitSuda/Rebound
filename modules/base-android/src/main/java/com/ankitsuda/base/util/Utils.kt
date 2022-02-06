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

fun Float.cmprs(min: Float, max: Float) = min + (max - min) * this
fun Float.cmprs(min: Int, max: Int) = this.cmprs(min.toFloat(), max.toFloat())
fun Int.cmprs(min: Int, max: Int) = this.toFloat().cmprs(min.toFloat(), max.toFloat())
fun Int.cmprs(min: Float, max: Float) = this.toFloat().cmprs(min, max)

val NONE_WORKOUT_ID = (-1).toLong()

class Utils {
    companion object {


    }
}