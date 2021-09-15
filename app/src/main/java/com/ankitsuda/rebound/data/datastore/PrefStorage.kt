package com.ankitsuda.rebound.data.datastore

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
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

    val cardBorderWidth: Flow<Int>
    suspend fun setCardBorderWidth(value: Int)

    val cardBorderColor: Flow<Color>
    suspend fun setCardBorderColor(color: Color)

    val cardElevation: Flow<Int>
    suspend fun setCardElevation(value: Int)

    // Shapes
    // Small Shape
    val shapeSmallTopStartRadius: Flow<Int>
    suspend fun setShapeSmallTopStartRadius(value: Int)

    val shapeSmallTopEndRadius: Flow<Int>
    suspend fun setShapeSmallTopEndRadius(value: Int)

    val shapeSmallBottomStartRadius: Flow<Int>
    suspend fun setShapeSmallBottomStartRadius(value: Int)

    val shapeSmallBottomEndRadius: Flow<Int>
    suspend fun setShapeSmallBottomEndRadius(value: Int)


    /***
     * clears all the stored data
     */
    suspend fun clearPreferenceStorage()
}