package com.ankitsuda.base.utils.extensions

import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES
import android.os.Bundle
import java.util.*
import kotlin.math.max

inline val <T : Any> T.simpleName: String get() = this.javaClass.kotlin.simpleName ?: "Unknown"

fun now() = System.currentTimeMillis()
fun nowNano() = System.nanoTime()

fun Array<out Any>.asString(): String {
    return joinToString { it.toString() }
}