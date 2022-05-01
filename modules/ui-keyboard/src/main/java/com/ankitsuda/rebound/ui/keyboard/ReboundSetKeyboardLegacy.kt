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

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.ViewGroup
import android.view.inputmethod.InputConnection
import android.widget.FrameLayout
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.core.view.updateLayoutParams
import com.ankitsuda.rebound.ui.keyboard.models.ClearNumKey
import com.ankitsuda.rebound.ui.keyboard.models.DecimalNumKey
import com.ankitsuda.rebound.ui.keyboard.models.NumKey
import com.ankitsuda.rebound.ui.keyboard.models.NumberNumKey

class ReboundSetKeyboardLegacy @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var inputConnection: InputConnection? = null

    init {
        addView(ComposeView(context = context).apply {
            setContent {
//                ReboundSetKeyboardComponent(
//                    onClickNumKey = {
//                        onClickNumKey(it)
//                    }
//                )
            }
        }).apply {
            updateLayoutParams {
                width = ViewGroup.LayoutParams.MATCH_PARENT
                height = ViewGroup.LayoutParams.WRAP_CONTENT
            }
        }
    }

    private fun onClickNumKey(numKey: NumKey) {
        if (inputConnection == null) return

        when (numKey) {
            is NumberNumKey, DecimalNumKey -> inputConnection?.commitText(numKey.toString(), 1)
            is ClearNumKey -> {
                val selectedText = inputConnection?.getSelectedText(0)

                if (TextUtils.isEmpty(selectedText)) {
                    inputConnection?.deleteSurroundingText(1, 0)
                } else {
                    inputConnection?.commitText("", 1)
                }
            }
        }

    }

    fun setInputConnection(ic: InputConnection) {
        inputConnection = ic
    }
}