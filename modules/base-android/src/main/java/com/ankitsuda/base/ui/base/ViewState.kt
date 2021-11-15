package com.ankitsuda.base.ui.base

interface ViewState

open class TypedViewState<out V>(val value: V) : ViewState