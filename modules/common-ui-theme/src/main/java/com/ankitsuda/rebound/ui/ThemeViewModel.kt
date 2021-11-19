package com.ankitsuda.rebound.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ankitsuda.base.ui.ThemeState
import com.ankitsuda.rebound.data.datastore.PrefStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

object PreferenceKeys {
    const val THEME_STATE_KEY = "theme_state"
}

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val handle: SavedStateHandle,
    private val preferences: PrefStorage,
) : ViewModel() {

    val themeState = preferences.themeState

    fun applyThemeState(themeState: ThemeState) {
        viewModelScope.launch {
            preferences.setThemeState(themeState)
        }
    }
}
