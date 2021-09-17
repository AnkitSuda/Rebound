package com.ankitsuda.rebound.utils

object LabelVisible {
    const val ALWAYS = "always"
    const val NEVER = "never"
    const val SELECTED = "selected"

    fun values() = listOf(ALWAYS, NEVER, SELECTED)
}