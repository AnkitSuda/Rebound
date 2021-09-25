package com.ankitsuda.rebound.data.datastore

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.ankitsuda.rebound.ui.theme.DefaultAccentColor
import com.ankitsuda.rebound.utils.LabelVisible
import com.ankitsuda.rebound.utils.toHexString
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppPreferences @Inject constructor(@ApplicationContext private val context: Context) :
    PrefStorage {
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "AppPrefStorage")


    companion object {
        val IS_LIGHT_THEME_KEY = booleanPreferencesKey(name = "is_light_theme")
        val IS_DARK_STATUS_BAR_ICONS_KEY = booleanPreferencesKey(name = "is_dark_status_bar_icons")
        val IS_DARK_NAVIGATION_BAR_ICONS_KEY =
            booleanPreferencesKey(name = "is_dark_navigation_bar_icons")
        val PRIMARY_COLOR_KEY = stringPreferencesKey(name = "primary_color")
        val BACKGROUND_COLOR_KEY = stringPreferencesKey(name = "background_color")
        val ON_PRIMARY_COLOR_KEY = stringPreferencesKey(name = "on_primary_color")
        val ON_BACKGROUND_COLOR_KEY = stringPreferencesKey(name = "on_background_color")
        val CARD_COLOR_KEY = stringPreferencesKey(name = "card_color")
        val CARD_BORDER_COLOR_KEY = stringPreferencesKey(name = "card_border_color")
        val CARD_BORDER_WIDTH_KEY = intPreferencesKey(name = "card_border_width")
        val CARD_ELEVATION_KEY = intPreferencesKey(name = "card_elevation")

        val BOTTOM_BAR_LABEL_VISIBLE_KEY = stringPreferencesKey(name = "bottom_bar_label_visible")
        val BOTTOM_BAR_LABEL_SPACING_KEY = intPreferencesKey(name = "bottom_bar_label_spacing")
        val BOTTOM_BAR_LABEL_WEIGHT_KEY = stringPreferencesKey(name = "bottom_bar_label_weight")
        val BOTTOM_BAR_ICON_SIZE_KEY = intPreferencesKey(name = "bottom_bar_icon_size")

        val TOP_BAR_TITLE_ALIGNMENT_KEY = stringPreferencesKey(name = "top_bar_alignment")
        val TOP_BAR_BACKGROUND_COLOR_KEY = stringPreferencesKey(name = "top_bar_background_color")
        val TOP_BAR_CONTENT_COLOR_KEY = stringPreferencesKey(name = "top_bar_content_color")
        val TOP_BAR_ELEVATION_KEY = intPreferencesKey(name = "top_bar_elevation")

        val CHARTS_SHADER_ENABLED_KEY = booleanPreferencesKey(name = "charts_shader_enabled")
        val CHARTS_LINE_THICKNESS_KEY = intPreferencesKey(name = "charts_line_thickness")
        val CHARTS_POINT_DIAMETER_KEY = intPreferencesKey(name = "charts_point_diameter")
        val CHARTS_POINT_LINE_THICKNESS_KEY =
            intPreferencesKey(name = "charts_point_line_thickness")

        // Small shape
        val SHAPE_SMALL_TOP_START_RADIUS_KEY =
            intPreferencesKey(name = "shape_small_top_start_radius_key")
        val SHAPE_SMALL_TOP_END_RADIUS_KEY =
            intPreferencesKey(name = "shape_small_top_end_radius_key")
        val SHAPE_SMALL_BOTTOM_START_RADIUS_KEY =
            intPreferencesKey(name = "shape_small_bottom_start_radius_key")
        val SHAPE_SMALL_BOTTOM_END_RADIUS_KEY =
            intPreferencesKey(name = "shape_small_bottom_end_radius_key")

        // Medium shape
        val SHAPE_MEDIUM_TOP_START_RADIUS_KEY =
            intPreferencesKey(name = "shape_medium_top_start_radius_key")
        val SHAPE_MEDIUM_TOP_END_RADIUS_KEY =
            intPreferencesKey(name = "shape_medium_top_end_radius_key")
        val SHAPE_MEDIUM_BOTTOM_START_RADIUS_KEY =
            intPreferencesKey(name = "shape_medium_bottom_start_radius_key")
        val SHAPE_MEDIUM_BOTTOM_END_RADIUS_KEY =
            intPreferencesKey(name = "shape_medium_bottom_end_radius_key")

        // Medium shape
        val SHAPE_LARGE_TOP_START_RADIUS_KEY =
            intPreferencesKey(name = "shape_large_top_start_radius_key")
        val SHAPE_LARGE_TOP_END_RADIUS_KEY =
            intPreferencesKey(name = "shape_large_top_end_radius_key")
        val SHAPE_LARGE_BOTTOM_START_RADIUS_KEY =
            intPreferencesKey(name = "shape_large_bottom_start_radius_key")
        val SHAPE_LARGE_BOTTOM_END_RADIUS_KEY =
            intPreferencesKey(name = "shape_large_bottom_end_radius_key")
    }

    override val isLightTheme: Flow<Boolean> = getValue(IS_LIGHT_THEME_KEY, true)

    override suspend fun setIsLightTheme(value: Boolean) {
        setValue(IS_LIGHT_THEME_KEY, value)
    }

    override val isDarkStatusBarIcons: Flow<Boolean> = getValue(IS_DARK_STATUS_BAR_ICONS_KEY, true)

    override suspend fun setIsDarkStatusBarIcons(value: Boolean) {
        setValue(IS_DARK_STATUS_BAR_ICONS_KEY, value)
    }

    override val isDarkNavigationBarIcons: Flow<Boolean> =
        getValue(IS_DARK_NAVIGATION_BAR_ICONS_KEY, true)

    override suspend fun setIsDarkNavigationBarIcons(value: Boolean) {
        setValue(IS_DARK_NAVIGATION_BAR_ICONS_KEY, value)
    }

    override val primaryColor = getColor(PRIMARY_COLOR_KEY, DefaultAccentColor)

    override suspend fun setPrimaryColor(color: Color) {
        setColor(PRIMARY_COLOR_KEY, color)
    }

    override val backgroundColor = getColor(BACKGROUND_COLOR_KEY, Color.White)

    override suspend fun setBackgroundColor(color: Color) {
        setColor(BACKGROUND_COLOR_KEY, color)
    }

    override val onPrimaryColor = getColor(ON_PRIMARY_COLOR_KEY, Color.White)

    override suspend fun setOnPrimaryColor(color: Color) {
        setColor(ON_PRIMARY_COLOR_KEY, color)
    }

    override val onBackgroundColor = getColor(ON_BACKGROUND_COLOR_KEY, Color.Black)


    override suspend fun setOnBackgroundColor(color: Color) {
        setColor(ON_BACKGROUND_COLOR_KEY, color)
    }

    override val cardColor = getColor(CARD_COLOR_KEY, Color(248, 248, 248))

    override suspend fun setCardColor(color: Color) {
        setColor(CARD_COLOR_KEY, color)
    }

    override val cardBorderWidth: Flow<Int> = getValue(CARD_BORDER_WIDTH_KEY, 0)

    override suspend fun setCardBorderWidth(value: Int) {
        setValue(CARD_BORDER_WIDTH_KEY, value)
    }

    override val cardBorderColor = getColor(CARD_BORDER_COLOR_KEY, Color.Gray)


    override suspend fun setCardBorderColor(color: Color) {
        setColor(CARD_BORDER_COLOR_KEY, color)
    }


    override val cardElevation: Flow<Int> = getValue(CARD_ELEVATION_KEY, 0)

    override suspend fun setCardElevation(value: Int) {
        setValue(CARD_ELEVATION_KEY, value)
    }

    override val bottomBarLabelVisible = getValue(
        BOTTOM_BAR_LABEL_VISIBLE_KEY,
        LabelVisible.ALWAYS
    )

    override suspend fun setBottomBarLabelVisible(value: String) {
        setValue(BOTTOM_BAR_LABEL_VISIBLE_KEY, value)
    }

    override val bottomBarLabelSpacing: Flow<Int> = getValue(BOTTOM_BAR_LABEL_SPACING_KEY, 56)

    override suspend fun setBottomBarLabelSpacing(value: Int) {
        setValue(BOTTOM_BAR_LABEL_SPACING_KEY, value)
    }

    override val bottomBarLabelWeight: Flow<String> =
        getValue(BOTTOM_BAR_LABEL_WEIGHT_KEY, "normal")

    override suspend fun setBottomBarLabelWeight(value: String) {
        setValue(BOTTOM_BAR_LABEL_WEIGHT_KEY, value)
    }

    override val bottomBarIconSize: Flow<Int> = getValue(BOTTOM_BAR_ICON_SIZE_KEY, 24)

    override suspend fun setBottomBarIconSize(value: Int) {
        setValue(BOTTOM_BAR_ICON_SIZE_KEY, value)
    }

    override val topBarTitleAlignment: Flow<String> =
        getValue(TOP_BAR_TITLE_ALIGNMENT_KEY, "center")

    override suspend fun setTopBarTitleAlignment(value: String) {
        setValue(TOP_BAR_TITLE_ALIGNMENT_KEY, value)
    }

    override val topBarBackgroundColor = getColor(TOP_BAR_BACKGROUND_COLOR_KEY, Color.White)

    override suspend fun setTopBarBackgroundColor(value: Color) {
        setColor(TOP_BAR_BACKGROUND_COLOR_KEY, value)
    }

    override val topBarContentColor = getColor(TOP_BAR_CONTENT_COLOR_KEY, Color.Black)


    override suspend fun setTopBarContentColor(value: Color) {
        setColor(TOP_BAR_CONTENT_COLOR_KEY, value)
    }

    override val topBarElevation: Flow<Int> = getValue(TOP_BAR_ELEVATION_KEY, 2)

    override suspend fun setTopBarElevation(value: Int) {
        setValue(TOP_BAR_ELEVATION_KEY, value)
    }

    override val chartsShaderEnabled: Flow<Boolean> = getValue(CHARTS_SHADER_ENABLED_KEY, true)

    override suspend fun setChartsShaderEnabled(value: Boolean) {
        setValue(CHARTS_SHADER_ENABLED_KEY, value)
    }

    override val chartsLineThickness: Flow<Int> = getValue(CHARTS_LINE_THICKNESS_KEY, 2)

    override suspend fun setChartsLineThickness(value: Int) {
        setValue(CHARTS_LINE_THICKNESS_KEY, value)
    }

    override val chartsPointDiameter: Flow<Int> = getValue(CHARTS_POINT_DIAMETER_KEY, 6)

    override suspend fun setChartsPointDiameter(value: Int) {
        setValue(CHARTS_POINT_DIAMETER_KEY, value)
    }

    override val chartsPointLineThickness: Flow<Int> = getValue(CHARTS_POINT_LINE_THICKNESS_KEY, 2)

    override suspend fun setChartsPointLineThickness(value: Int) {
        setValue(CHARTS_POINT_LINE_THICKNESS_KEY, value)
    }


    // SMALL SHAPE STARTS
    override val shapeSmallTopStartRadius: Flow<Int> = getValue(SHAPE_SMALL_TOP_START_RADIUS_KEY, 0)

    override suspend fun setShapeSmallTopStartRadius(value: Int) {
        setValue(SHAPE_SMALL_TOP_START_RADIUS_KEY, value)
    }

    override val shapeSmallTopEndRadius: Flow<Int> = getValue(SHAPE_SMALL_TOP_END_RADIUS_KEY, 0)

    override suspend fun setShapeSmallTopEndRadius(value: Int) {
        setValue(SHAPE_SMALL_TOP_END_RADIUS_KEY, value)
    }

    override val shapeSmallBottomStartRadius: Flow<Int> =
        getValue(SHAPE_SMALL_BOTTOM_START_RADIUS_KEY, 0)

    override suspend fun setShapeSmallBottomStartRadius(value: Int) {
        setValue(SHAPE_SMALL_BOTTOM_START_RADIUS_KEY, value)
    }

    override val shapeSmallBottomEndRadius: Flow<Int> =
        getValue(SHAPE_SMALL_BOTTOM_END_RADIUS_KEY, 0)

    override suspend fun setShapeSmallBottomEndRadius(value: Int) {
        setValue(SHAPE_SMALL_BOTTOM_END_RADIUS_KEY, value)
    }
    // SMALL SHAPE ENDS

    override suspend fun clearPreferenceStorage() {
        context.dataStore.edit {
            it.clear()
        }
    }

    private suspend fun setColor(
        key: Preferences.Key<String>,
        color: Color
    ) {
        setValue(key, color.toHexString())
    }

    private fun getColor(
        key: Preferences.Key<String>,
        defaultColor: Color
    ) = context.dataStore.data
        .map { preferences ->
            if (preferences[key] != null) {
                val colorStr = preferences[key]!!
                Color(android.graphics.Color.parseColor(
                    if (colorStr.startsWith("#"))
                        colorStr
                    else
                        "#$colorStr"
                ))
            } else {
                defaultColor
            }
        }


    suspend fun <T> setValue(
        key: Preferences.Key<T>,
        value: T
    ) {
        context.dataStore.setValue(key, value)
    }

    fun <T> getValue(
        key: Preferences.Key<T>,
        defaultValue: T
    ): Flow<T> =
        context.dataStore.getValueAsFlow(key, defaultValue)


    /***
     * handy function to save key-value pairs in Preference. Sets or updates the value in Preference
     * @param key used to identify the preference
     * @param value the value to be saved in the preference
     */
    private suspend fun <T> DataStore<Preferences>.setValue(
        key: Preferences.Key<T>,
        value: T
    ) {
        this.edit { preferences ->
            // save the value in prefs
            preferences[key] = value
        }
    }

    /***
     * handy function to return Preference value based on the Preference key
     * @param key  used to identify the preference
     * @param defaultValue value in case the Preference does not exists
     * @throws Exception if there is some error in getting the value
     * @return [Flow] of [T]
     */
    private fun <T> DataStore<Preferences>.getValueAsFlow(
        key: Preferences.Key<T>,
        defaultValue: T
    ): Flow<T> {
        return this.data.catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                // we try again to store the value in the map operator
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            // return the default value if it doesn't exist in the storage
            preferences[key] ?: defaultValue
        }
    }
}