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

package com.ankitsuda.rebound.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.ankitsuda.base.util.TopBarAlignment
import com.ankitsuda.rebound.ui.theme.LocalThemeState
import com.ankitsuda.rebound.ui.theme.ReboundTheme
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.statusBarsHeight
import com.google.accompanist.insets.statusBarsPadding
import me.onebone.toolbar.CollapsingToolbarScope
import me.onebone.toolbar.CollapsingToolbarState

/**
 * Variant of TopBar,
 * Title font size and offset changes as user scrolls
 *
 * @param title Title text
 * @param toolbarState Collapsing toolbar state
 * @param statusBarEnabled Adds status bar padding when true
 * @param navigationIcon Navigation icon (start icon)
 * @param actions Menu icons (end icons)
 * @param elevationEnabled Adds elevation to TopBar when true (no effect as of now)
 * @param bottomLayout Layout to show at bottom of the TopBar
 */
@Composable
fun CollapsingToolbarScope.TopBar2(
    title: String,
    toolbarState: CollapsingToolbarState? = null,
    statusBarEnabled: Boolean = true,
    navigationIcon: (@Composable BoxScope.() -> Unit)? = null,
    actions: (@Composable RowScope.() -> Unit)? = null,
    elevationEnabled: Boolean = true,
    bottomLayout: (@Composable () -> Unit)? = null
) {
    val theme = LocalThemeState.current

    val backgroundColor = theme.topBarBackgroundColor
    val contentColor = theme.topBarContentColor
    val elevation = theme.topBarElevation

    val titleSize = if (toolbarState != null) (20 + (30 - 20) * toolbarState.progress).sp else 20.sp

    val toolbarHeight = 58.dp

    val statusBarHeight =
        with(LocalDensity.current) { if (statusBarEnabled) LocalWindowInsets.current.statusBars.top.toDp() else 0.dp }

    val maxTitleOffset = 80
    val minTitleOffset = 16
    val titleOffset = if (navigationIcon != null && toolbarState != null) {
        (maxTitleOffset + (minTitleOffset - maxTitleOffset) * toolbarState.progress).dp
    } else {
        minTitleOffset.dp
    }

    var bottomLayoutHeight by rememberSaveable {
        mutableStateOf(0)
    }

    val bottomLayoutHeightDp = with(LocalDensity.current) { bottomLayoutHeight.toDp() }

    Box(
        modifier = Modifier
            .background(backgroundColor)
            .fillMaxWidth()
            .height(150.dp + statusBarHeight + bottomLayoutHeightDp)
            .pin()
    )


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(toolbarHeight + statusBarHeight + bottomLayoutHeightDp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(toolbarHeight + statusBarHeight)
                .align(Alignment.TopStart)
        ) {

            navigationIcon?.let {
                Box(
                    modifier = Modifier
                        .padding(top = statusBarHeight, start = 8.dp)
                        .align(Alignment.CenterStart)
                ) {
                    navigationIcon()
                }
            }

            actions?.let {
                Row(
                    modifier = Modifier
                        .padding(top = statusBarHeight, end = 8.dp)
                        .align(Alignment.CenterEnd),
                    verticalAlignment = Alignment.CenterVertically,
                    content = actions
                )
            }
        }
    }

//    Box(
//        Modifier
//            .height(statusBarHeight + toolbarHeight)
//            .offset(titleOffset, 0.dp)
//            .road(
//                whenCollapsed = Alignment.CenterStart,
//                whenExpanded = Alignment.BottomStart
//            ),
//        contentAlignment = Alignment.CenterStart,
//    ) {
//
//        Text(
//            modifier = Modifier.padding(top = statusBarHeight),
//            text = title,
//            style = ReboundTheme.typography.h6,
//            fontWeight = FontWeight.Bold,
//            fontSize = titleSize,
//            textAlign = TextAlign.Start,
//            color = contentColor,
//        )
//    }

    Column(
        Modifier
            .defaultMinSize(minHeight = statusBarHeight + toolbarHeight + bottomLayoutHeightDp)
            .fillMaxWidth()
            .road(
                whenCollapsed = Alignment.BottomEnd,
                whenExpanded = Alignment.BottomEnd
            )
    ) {

        Box(
            modifier = Modifier
                .height(toolbarHeight + statusBarHeight)
                .offset(titleOffset, 0.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                modifier = Modifier
                    .padding(top = statusBarHeight),
                text = title,
                style = ReboundTheme.typography.h6,
                fontWeight = FontWeight.Bold,
                fontSize = titleSize,
                textAlign = TextAlign.Start,
                color = contentColor,
                maxLines = 1
            )
        }

        bottomLayout?.let {
            Box(modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned {
                    bottomLayoutHeight = it.size.height
                }
            ) {
                it()
            }

        }
    }

}
