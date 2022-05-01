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

package com.ankitsuda.rebound.ui.keyboard.field

import android.text.InputType
import android.view.Gravity
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.widget.addTextChangedListener
import com.ankitsuda.base.util.lighterOrDarkerColor
import com.ankitsuda.base.util.toLegacyInt
import com.ankitsuda.rebound.ui.keyboard.LocalReboundSetKeyboard
import com.ankitsuda.rebound.ui.keyboard.models.ClearNumKey
import com.ankitsuda.rebound.ui.theme.ReboundTheme

@Composable
fun RowScope.ReboundSetTextField(
    value: String,
    contentColor: Color,
    bgColor: Color,
    onValueChange: (String) -> Unit
) {
    val keyboard = LocalReboundSetKeyboard.current

    BoxWithConstraints(
        modifier = Modifier
            .height(32.dp)
            .padding(start = 8.dp, end = 8.dp)
            .weight(1.25f)
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor.lighterOrDarkerColor(0.10f)),
    ) {
        val width = with(LocalDensity.current) { constraints.minWidth.toDp() }
        val height = with(LocalDensity.current) { constraints.minHeight.toDp() }

        AndroidView(modifier = Modifier
            .width(width)
            .height(height),
            factory = {
                TextView(it).apply {
                    text = value
                    textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                    gravity = Gravity.CENTER
                    setTextColor(contentColor.toLegacyInt())
                    showSoftInputOnFocus = false

                    addTextChangedListener { e ->
                        val newValue = e.toString()
                        if (newValue != value) {
                            onValueChange(newValue)
                        }
                    }

                    setRawInputType(InputType.TYPE_CLASS_TEXT)
                    setTextIsSelectable(true)
                    val ic = onCreateInputConnection(EditorInfo())

                    setOnClickListener {
                        keyboard.show()
                    }

                    onFocusChangeListener = View.OnFocusChangeListener { _, p1 ->
                        if (p1) {
                            keyboard.show()
                            keyboard.setInputConnection(ic)
                        } else {
                            keyboard.hide()
                        }
                    }
                }
            },
            update = {
                it.setTextColor(contentColor.toLegacyInt())
            }
        )
    }
}