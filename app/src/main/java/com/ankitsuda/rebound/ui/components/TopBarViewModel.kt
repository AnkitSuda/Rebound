package com.ankitsuda.rebound.ui.components

import androidx.lifecycle.ViewModel
import com.ankitsuda.rebound.data.datastore.PrefStorage
import com.ankitsuda.rebound.utils.TopBarAlignment
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TopBarViewModel @Inject constructor(prefStorage: PrefStorage): ViewModel() {
    val titleAlignment = prefStorage.topBarTitleAlignment
    val allTitleAlignments = TopBarAlignment.values()

}