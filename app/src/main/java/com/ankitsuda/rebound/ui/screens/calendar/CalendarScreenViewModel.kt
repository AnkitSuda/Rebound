package com.ankitsuda.rebound.ui.screens.calendar

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ankitsuda.rebound.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CalendarScreenViewModel @Inject constructor() : ViewModel() {
    private var _calendar: SnapshotStateList<CalendarItem> = mutableStateListOf()
    val calendar = _calendar

    fun getMonth(year: Int, month: Int): List<CalendarItem> {
        Timber.d("Getting month $month year $year")
        return CalendarUtils(1).generateCalendarItemsForMonth(year, month)
    }

    fun getCalendar() {
        NullableDatesRange(dateFrom = null, dateTo = null)

        val datesRange = DisplayedDatesRangeFactory.getDisplayedDatesRange(
            initialDate = CalendarDate.today,
            minDate = null,
            maxDate = null
        )


        val calendarItems = CalendarUtils(1).generateCalendarItems(
            dateFrom = datesRange.dateFrom,
            dateTo = datesRange.dateTo
        )

        _calendar.addAll(calendarItems)
    }


}