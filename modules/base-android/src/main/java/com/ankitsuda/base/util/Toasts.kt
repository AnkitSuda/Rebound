package com.ankitsuda.base.util

import android.content.Context
import android.view.Gravity
import android.widget.Toast
import androidx.annotation.StringRes

/**
 * Display the simple Toast message with the [Toast.LENGTH_SHORT] duration.
 *
 * @param message the message text.
 */
fun Context.toast(
    message: CharSequence,
    duration: Int = Toast.LENGTH_SHORT,
    block: Toast.() -> Unit = {}
): Toast = Toast
    .makeText(this, message, Toast.LENGTH_SHORT)
    .apply {
        block(this)
        show()
    }

fun Context.toast(
    @StringRes messageRes: Int,
    duration: Int = Toast.LENGTH_SHORT,
    block: Toast.() -> Unit = {}
) = toast(getString(messageRes), duration, block)

fun Context.centeredToast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
    toast(message) {
        setGravity(Gravity.CENTER, 0, 0)
    }
}
