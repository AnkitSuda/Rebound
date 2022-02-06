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

package com.ankitsuda.base.ui


/**
 * State for UI containing data.
 */
sealed class UIDataState<T> {
    class Loading<T> : UIDataState<T>()
    class Success<T>(val data: T) : UIDataState<T>()
    class Failed<T>(val message: String) : UIDataState<T>()

    val isLoading get() = this is Loading

    val isSuccess get() = this is Success

    val isFailed get() = this is Failed

    companion object {
        fun <T> loading() = Loading<T>()
        fun <T> success(data: T) = Success(data)
        fun <T> failed(message: String) = Failed<T>(message)
    }
}