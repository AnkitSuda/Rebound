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

package com.ankitsuda.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

val LocalNavigator = staticCompositionLocalOf<Navigator> {
    error("No LocalNavigator given")
}

@Composable
fun NavigatorHost(
    viewModel: NavigatorViewModel = hiltViewModel(),
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalNavigator provides viewModel.navigator, content = content)
}

sealed class NavigationEvent(open val route: String) {
    object Back : NavigationEvent("Back")
    data class Destination(override val route: String, val root: String? = null) : NavigationEvent(route)

    override fun toString() = route
}

class Navigator {
    private val navigationQueue = Channel<NavigationEvent>(Channel.CONFLATED)

    fun navigate(route: String) {
        val basePath = route.split("/").firstOrNull()
        val root = if (TAB_ROOT_SCREENS.any { it.route == basePath }) basePath else null
        navigationQueue.trySend(NavigationEvent.Destination(route, root))
    }

    fun goBack() {
        navigationQueue.trySend(NavigationEvent.Back)
    }

    val queue = navigationQueue.receiveAsFlow()
}
