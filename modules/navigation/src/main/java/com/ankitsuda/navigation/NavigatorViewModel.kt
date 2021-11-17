package com.ankitsuda.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NavigatorViewModel @Inject constructor(
    val navigator: Navigator,
    private val handle: SavedStateHandle,
) : ViewModel()
