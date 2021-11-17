package com.ankitsuda.base.util

object LabelVisible {
    const val ALWAYS = "always"
    const val NEVER = "never"
    const val SELECTED = "selected"

    fun values() = listOf(ALWAYS, NEVER, SELECTED)
}