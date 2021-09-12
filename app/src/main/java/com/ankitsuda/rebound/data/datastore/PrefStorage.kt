package com.ankitsuda.rebound.data.datastore

import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.flow.Flow

interface PrefStorage {
    val cardColor: Flow<Color>
    suspend fun setCardColor(color: Color)

    val cardBorderEnabled: Flow<Boolean>
    suspend fun setCardBorderEnabled(enabled: Boolean)

    /***
     * clears all the stored data
     */
    suspend fun clearPreferenceStorage()
}