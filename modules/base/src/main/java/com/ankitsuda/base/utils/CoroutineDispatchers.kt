package com.ankitsuda.base.utils

import kotlinx.coroutines.CoroutineDispatcher

data class CoroutineDispatchers(
    val network: CoroutineDispatcher,
    val io: CoroutineDispatcher,
    val computation: CoroutineDispatcher,
    val main: CoroutineDispatcher,
)
