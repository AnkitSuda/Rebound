package com.ankitsuda.rebound.ui.components.calpose.model

import org.threeten.bp.DayOfWeek
import org.threeten.bp.YearMonth


/**
 * Contains metadata information about a certain day
 *
 * @property day Indicates the day number (1-31)
 *
 * @property dayOfWeek Contains DayOfWeek information about the day (Monday-Sunday)
 *
 * @property day Information about the month the date belongs to
 *
 */
data class CalposeDate (val day: Int, val dayOfWeek: DayOfWeek, val month: YearMonth)