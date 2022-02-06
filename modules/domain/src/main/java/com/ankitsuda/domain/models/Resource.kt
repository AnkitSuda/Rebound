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

package com.ankitsuda.domain.models

/**
 * A generic class that holds a value with its loading status.
 */
data class Resource(val status: Status, val error: Throwable? = null)

enum class Status {
    SUCCESS,
    ERROR,
    LOADING,
    REFRESHING,
    LOADING_MORE
}
