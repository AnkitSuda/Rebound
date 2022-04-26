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

package com.ankitsuda.base.utils

import java.time.LocalDateTime

enum class TimePeriod(val str: String) {
    MORNING("Morning"),
    AFTERNOON("Afternoon"),
    EVENING("Evening"),
    NIGHT("Night");

    override fun toString(): String {
        return str
    }

    companion object {
        fun now() = fromDateTime(LocalDateTime.now())
        fun fromDateTime(localDateTime: LocalDateTime): TimePeriod {
            val hours = localDateTime.hour
            return when {
                hours <= 12 -> {
                    MORNING
                }
                hours <= 16 -> {
                    AFTERNOON
                }
                hours <= 21 -> {
                    EVENING
                }
                else -> {
                    NIGHT
                }
            }
        }
    }
}