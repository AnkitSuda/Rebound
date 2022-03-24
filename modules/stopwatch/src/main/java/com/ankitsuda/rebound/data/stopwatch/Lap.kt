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

package com.ankitsuda.rebound.data.stopwatch

/**
 * A read-only domain object representing a stopwatch lap.
 */
data class Lap(
    /** The 1-based position of the lap.  */
    val lapNumber: Int,
    /** Elapsed time in ms since the lap was last started.  */
    val lapTime: Long,
    /** Elapsed time in ms accumulated for all laps up to and including this one.  */
    val accumulatedTime: Long
)