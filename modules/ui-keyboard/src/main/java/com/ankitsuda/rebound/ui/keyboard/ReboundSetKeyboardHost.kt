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
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.ankitsuda.rebound.ui.keyboard.LocalReboundSetKeyboard
import com.ankitsuda.rebound.ui.keyboard.ReboundSetKeyboard
import com.ankitsuda.rebound.ui.keyboard.ReboundSetKeyboardComponent
import com.ankitsuda.rebound.ui.keyboard.enums.ReboundKeyboardType
import com.ankitsuda.rebound.ui.theme.LocalThemeState
import com.ankitsuda.rebound.ui.theme.ReboundTheme
import com.google.accompanist.insets.navigationBarsPadding

@Composable
fun ReboundKeyboardHost(content: @Composable () -> Unit) {
    val theme = LocalThemeState.current

    var reboundSetKeyboardType by remember {
        mutableStateOf(ReboundKeyboardType.WEIGHT)
    }
    var reboundSetKeyboardVisible by remember {
        mutableStateOf(false)
    }
    var reboundSetKeyboardInputConnection: InputConnection? by remember {
        mutableStateOf(null)
    }

    val reboundSetKeyboard by remember {
        mutableStateOf(
            ReboundSetKeyboard(
                onChangeKeyboardType = {
                    reboundSetKeyboardType = it
                },

                onChangeVisibility = {
                    reboundSetKeyboardVisible = it
                },
                onChangeInputConnection = {
                    reboundSetKeyboardInputConnection = it
                }
            )
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        CompositionLocalProvider(
            LocalReboundSetKeyboard provides reboundSetKeyboard
        ) {
            content()
        }

        AnimatedVisibility(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            enter = slideInVertically { it },
            exit = slideOutVertically { it },
            visible = reboundSetKeyboardVisible && reboundSetKeyboardInputConnection != null
        ) {
            Surface(
                modifier = Modifier
                    .zIndex(10f)
                    .fillMaxWidth(),
                elevation = 8.dp,
                shape = RoundedCornerShape(0.dp)
            ) {
                Box(
                    modifier = Modifier
                        .background(theme.keyboardBackgroundColor)
                        .navigationBarsPadding()
                ) {
                    ReboundSetKeyboardComponent(
                        reboundKeyboardType = reboundSetKeyboardType,
                        inputConnection = reboundSetKeyboardInputConnection,
                        onHideKeyboard = {
                            reboundSetKeyboard.hide()
                        }
                    )
                }
            }
        }
    }
}