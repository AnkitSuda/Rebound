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

package com.ankitsuda.rebound.ui.keyboard

import android.view.inputmethod.InputConnection
import androidx.compose.runtime.staticCompositionLocalOf
import com.ankitsuda.rebound.ui.keyboard.enums.ReboundKeyboardType

class ReboundSetKeyboard(
    private val onChangeKeyboardType: (ReboundKeyboardType) -> Unit,
    private val onChangeVisibility: (Boolean) -> Unit,
    private val onChangeInputConnection: (InputConnection) -> Unit
) {
    private var _inputConnection: InputConnection? = null

    fun setInputConnection(ic: InputConnection) {
        _inputConnection = ic
        onChangeInputConnection(ic)
    }

    fun setKeyboardType(type: ReboundKeyboardType) {
        onChangeKeyboardType(type)
    }

    fun show() {
        onChangeVisibility(true)
    }

    fun hide() {
        _inputConnection?.finishComposingText()
        onChangeVisibility(false)
    }
}

val LocalReboundSetKeyboard = staticCompositionLocalOf<ReboundSetKeyboard> {
    error("No LocalReboundSetKeyboard given")
}