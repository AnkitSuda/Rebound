package com.ankitsuda.rebound.data.datastore

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import com.ankitsuda.rebound.utils.LabelVisible
import kotlinx.coroutines.flow.Flow

interface PrefStorage {
    val isLightTheme: Flow<Boolean>
    suspend fun setIsLightTheme(value: Boolean)

    val isDarkStatusBarIcons: Flow<Boolean>
    suspend fun setIsDarkStatusBarIcons(value: Boolean)

    val isDarkNavigationBarIcons: Flow<Boolean>
    suspend fun setIsDarkNavigationBarIcons(value: Boolean)

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

    val bottomBarLabelVisible: Flow<String>
    suspend fun setBottomBarLabelVisible(value: String)

    val bottomBarLabelSpacing: Flow<Int>
    suspend fun setBottomBarLabelSpacing(value: Int)

    val bottomBarLabelWeight: Flow<String>
    suspend fun setBottomBarLabelWeight(value: String)

    val bottomBarIconSize: Flow<Int>
    suspend fun setBottomBarIconSize(value: Int)

    val topBarTitleAlignment: Flow<String>
    suspend fun setTopBarTitleAlignment(value: String)

    val topBarBackgroundColor: Flow<Color>
    suspend fun setTopBarBackgroundColor(value: Color)

    val topBarContentColor: Flow<Color>
    suspend fun setTopBarContentColor(value: Color)

    val topBarElevation: Flow<Int>
    suspend fun setTopBarElevation(value: Int)

    val chartsShaderEnabled: Flow<Boolean>
    suspend fun setChartsShaderEnabled(value: Boolean)

    val chartsLineThickness: Flow<Int>
    suspend fun setChartsLineThickness(value: Int)

    val chartsPointDiameter: Flow<Int>
    suspend fun setChartsPointDiameter(value: Int)

    val chartsPointLineThickness: Flow<Int>
    suspend fun setChartsPointLineThickness(value: Int)

    // Shapes
    /**
     * Dirty hack. Will implement better way to store shape values later.
     */
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