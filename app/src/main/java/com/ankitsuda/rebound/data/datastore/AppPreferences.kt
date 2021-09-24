package com.ankitsuda.rebound.data.datastore

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.ankitsuda.rebound.ui.theme.DefaultAccentColor
import com.ankitsuda.rebound.utils.LabelVisible
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
        val PRIMARY_COLOR_KEY = intPreferencesKey(name = "primary_color")
        val BACKGROUND_COLOR_KEY = intPreferencesKey(name = "background_color")
        val ON_PRIMARY_COLOR_KEY = intPreferencesKey(name = "on_primary_color")
        val ON_BACKGROUND_COLOR_KEY = intPreferencesKey(name = "on_background_color")
        val CARD_COLOR_KEY = intPreferencesKey(name = "card_color")
        val CARD_BORDER_COLOR_KEY = intPreferencesKey(name = "card_border_color")
        val CARD_BORDER_WIDTH_KEY = intPreferencesKey(name = "card_border_width")
        val CARD_ELEVATION_KEY = intPreferencesKey(name = "card_elevation")

        val BOTTOM_BAR_LABEL_VISIBLE_KEY = stringPreferencesKey(name = "bottom_bar_label_visible")
        val BOTTOM_BAR_LABEL_SPACING_KEY = intPreferencesKey(name = "bottom_bar_label_spacing")
        val BOTTOM_BAR_LABEL_WEIGHT_KEY = stringPreferencesKey(name = "bottom_bar_label_weight")
        val BOTTOM_BAR_ICON_SIZE_KEY = intPreferencesKey(name = "bottom_bar_icon_size")

        val TOP_BAR_TITLE_ALIGNMENT_KEY = stringPreferencesKey(name = "top_bar_alignment")

        val CHARTS_SHADER_ENABLED_KEY = booleanPreferencesKey(name = "charts_shader_enabled")
        val CHARTS_LINE_THICKNESS_KEY = intPreferencesKey(name = "charts_line_thickness")
        val CHARTS_POINT_DIAMETER_KEY = intPreferencesKey(name = "charts_point_diameter")
        val CHARTS_POINT_LINE_THICKNESS_KEY = intPreferencesKey(name = "charts_point_line_thickness")

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

    suspend fun setColor(key: Preferences.Key<Int>, color: Color) {
        context.dataStore.edit { preferences ->
            preferences[key] = color.toArgb()
        }
    }

    override val isLightTheme: Flow<Boolean>
        get() = context.dataStore.getValueAsFlow(IS_LIGHT_THEME_KEY, false)

    override suspend fun setIsLightTheme(value: Boolean) {
        context.dataStore.setValue(IS_LIGHT_THEME_KEY, value)
    }

    override val primaryColor: Flow<Color>
        get() = context.dataStore.data
            .map { preferences ->
                if (preferences[PRIMARY_COLOR_KEY] != null) {
                    Color(preferences[PRIMARY_COLOR_KEY]!!)
                } else {
                    DefaultAccentColor
                }
            }

    override suspend fun setPrimaryColor(color: Color) {
        setColor(PRIMARY_COLOR_KEY, color)
    }

    override val backgroundColor: Flow<Color>
        get() = context.dataStore.data
            .map { preferences ->
                if (preferences[BACKGROUND_COLOR_KEY] != null) {
                    Color(preferences[BACKGROUND_COLOR_KEY]!!)
                } else {
                    Color.White
                }
            }

    override suspend fun setBackgroundColor(color: Color) {
        setColor(BACKGROUND_COLOR_KEY, color)
    }

    override val onPrimaryColor: Flow<Color>
        get() = context.dataStore.data
            .map { preferences ->
                if (preferences[ON_PRIMARY_COLOR_KEY] != null) {
                    Color(preferences[ON_PRIMARY_COLOR_KEY]!!)
                } else {
                    Color.White
                }
            }

    override suspend fun setOnPrimaryColor(color: Color) {
        setColor(ON_PRIMARY_COLOR_KEY, color)
    }

    override val onBackgroundColor: Flow<Color>
        get() = context.dataStore.data
            .map { preferences ->
                if (preferences[ON_BACKGROUND_COLOR_KEY] != null) {
                    Color(preferences[ON_BACKGROUND_COLOR_KEY]!!)
                } else {
                    Color.White
                }
            }

    override suspend fun setOnBackgroundColor(color: Color) {
        setColor(ON_BACKGROUND_COLOR_KEY, color)
    }

    override val cardColor: Flow<Color>
        get() = context.dataStore.data
            .map { preferences ->
                if (preferences[CARD_COLOR_KEY] != null) {
                    Color(preferences[CARD_COLOR_KEY]!!)
                } else {
                    Color(248, 248, 248)
                }
            }

    override suspend fun setCardColor(color: Color) {
        setColor(CARD_COLOR_KEY, color)
    }

    override val cardBorderWidth: Flow<Int>
        get() = context.dataStore.getValueAsFlow(CARD_BORDER_WIDTH_KEY, 0)

    override suspend fun setCardBorderWidth(value: Int) {
        context.dataStore.setValue(CARD_BORDER_WIDTH_KEY, value)
    }

    override val cardBorderColor: Flow<Color>
        get() = context.dataStore.data
            .map { preferences ->
                if (preferences[CARD_BORDER_COLOR_KEY] != null) {
                    Color(preferences[CARD_BORDER_COLOR_KEY]!!)
                } else {
                    Color.Gray
                }
            }

    override suspend fun setCardBorderColor(color: Color) {
        setColor(CARD_BORDER_COLOR_KEY, color)
    }


    override val cardElevation: Flow<Int>
        get() = context.dataStore.getValueAsFlow(CARD_ELEVATION_KEY, 0)

    override suspend fun setCardElevation(value: Int) {
        context.dataStore.setValue(CARD_ELEVATION_KEY, value)
    }

    override val bottomBarLabelVisible: Flow<String>
        get() =
            context.dataStore.getValueAsFlow(
                BOTTOM_BAR_LABEL_VISIBLE_KEY,
                LabelVisible.ALWAYS
            )

    override suspend fun setBottomBarLabelVisible(value: String) {
        context.dataStore.setValue(BOTTOM_BAR_LABEL_VISIBLE_KEY, value)
    }

    override val bottomBarLabelSpacing: Flow<Int>
        get() = context.dataStore.getValueAsFlow(BOTTOM_BAR_LABEL_SPACING_KEY, 56)

    override suspend fun setBottomBarLabelSpacing(value: Int) {
        context.dataStore.setValue(BOTTOM_BAR_LABEL_SPACING_KEY, value)
    }

    override val bottomBarLabelWeight: Flow<String>
        get() = context.dataStore.getValueAsFlow(BOTTOM_BAR_LABEL_WEIGHT_KEY, "normal")

    override suspend fun setBottomBarLabelWeight(value: String) {
        context.dataStore.setValue(BOTTOM_BAR_LABEL_WEIGHT_KEY, value)
    }

    override val bottomBarIconSize: Flow<Int>
        get() = context.dataStore.getValueAsFlow(BOTTOM_BAR_ICON_SIZE_KEY, 24)

    override suspend fun setBottomBarIconSize(value: Int) {
        context.dataStore.setValue(BOTTOM_BAR_ICON_SIZE_KEY, value)
    }

    override val topBarTitleAlignment: Flow<String>
        get() = context.dataStore.getValueAsFlow(TOP_BAR_TITLE_ALIGNMENT_KEY, "center")

    override suspend fun setTopBarTitleAlignment(value: String) {
        context.dataStore.setValue(TOP_BAR_TITLE_ALIGNMENT_KEY, value)
    }

    override val chartsShaderEnabled: Flow<Boolean>
        get() = context.dataStore.getValueAsFlow(CHARTS_SHADER_ENABLED_KEY, true)

    override suspend fun setChartsShaderEnabled(value: Boolean) {
        context.dataStore.setValue(CHARTS_SHADER_ENABLED_KEY, value)
    }

    override val chartsLineThickness: Flow<Int>
        get() = context.dataStore.getValueAsFlow(CHARTS_LINE_THICKNESS_KEY, 2)

    override suspend fun setChartsLineThickness(value: Int) {
        context.dataStore.setValue(CHARTS_LINE_THICKNESS_KEY, value)
    }

    override val chartsPointDiameter: Flow<Int>
        get() = context.dataStore.getValueAsFlow(CHARTS_POINT_DIAMETER_KEY, 6)

    override suspend fun setChartsPointDiameter(value: Int) {
        context.dataStore.setValue(CHARTS_POINT_DIAMETER_KEY, value)
    }

    override val chartsPointLineThickness: Flow<Int>
        get() = context.dataStore.getValueAsFlow(CHARTS_POINT_LINE_THICKNESS_KEY, 2)

    override suspend fun setChartsPointLineThickness(value: Int) {
        context.dataStore.setValue(CHARTS_POINT_LINE_THICKNESS_KEY, value)
    }


    // SMALL SHAPE STARTS
    override val shapeSmallTopStartRadius: Flow<Int>
        get() = context.dataStore.getValueAsFlow(SHAPE_SMALL_TOP_START_RADIUS_KEY, 0)

    override suspend fun setShapeSmallTopStartRadius(value: Int) {
        context.dataStore.setValue(SHAPE_SMALL_TOP_START_RADIUS_KEY, value)
    }

    override val shapeSmallTopEndRadius: Flow<Int>
        get() = context.dataStore.getValueAsFlow(SHAPE_SMALL_TOP_END_RADIUS_KEY, 0)

    override suspend fun setShapeSmallTopEndRadius(value: Int) {
        context.dataStore.setValue(SHAPE_SMALL_TOP_END_RADIUS_KEY, value)
    }

    override val shapeSmallBottomStartRadius: Flow<Int>
        get() = context.dataStore.getValueAsFlow(SHAPE_SMALL_BOTTOM_START_RADIUS_KEY, 0)

    override suspend fun setShapeSmallBottomStartRadius(value: Int) {
        context.dataStore.setValue(SHAPE_SMALL_BOTTOM_START_RADIUS_KEY, value)
    }

    override val shapeSmallBottomEndRadius: Flow<Int>
        get() = context.dataStore.getValueAsFlow(SHAPE_SMALL_BOTTOM_END_RADIUS_KEY, 0)

    override suspend fun setShapeSmallBottomEndRadius(value: Int) {
        context.dataStore.setValue(SHAPE_SMALL_BOTTOM_END_RADIUS_KEY, value)
    }
    // SMALL SHAPE ENDS

    override suspend fun clearPreferenceStorage() {
        context.dataStore.edit {
            it.clear()
        }
    }


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