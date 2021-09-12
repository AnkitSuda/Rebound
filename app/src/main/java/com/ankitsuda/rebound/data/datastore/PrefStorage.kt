package com.ankitsuda.rebound.data.datastore

import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.flow.Flow

interface PrefStorage {
    val primaryColor: Flow<Color>
    suspend fun setPrimaryColor(color: Color)

    val backgroundColor: Flow<Color>
    suspend fun setBackgroundColor(color: Color)

    val onPrimaryColor: Flow<Color>
    suspend fun setOnPrimaryColor(color: Color)

    val onBackgroundColor: Flow<Color>
    suspend fun setOnBackgroundColor(color: Color)

    val cardColor: Flow<Color>
    suspend fun setCardColor(color: Color)

    val cardBorderEnabled: Flow<Boolean>
    suspend fun setCardBorderEnabled(enabled: Boolean)

    val cardElevation: Flow<Int>
    suspend fun setCardElevation(value: Int)

    /***
     * clears all the stored data
     */
    suspend fun clearPreferenceStorage()
}