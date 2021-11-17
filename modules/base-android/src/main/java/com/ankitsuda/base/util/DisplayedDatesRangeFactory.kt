package com.ankitsuda.base.util

/**
 * This class responsible for providing [DatesRange] according to incoming params.
 */
object DisplayedDatesRangeFactory {

    fun getDisplayedDatesRange(
        initialDate: CalendarDate,
        minDate: CalendarDate? = null,
        maxDate: CalendarDate? = null

    ): DatesRange {

        val rangeStart: CalendarDate
        val rangeEnd: CalendarDate

        when {
            minDate == null && maxDate == null -> {
                rangeStart = initialDate.minusMonths(CalendarUtils.MONTHS_PER_PAGE)
                rangeEnd = initialDate.plusMonths(CalendarUtils.MONTHS_PER_PAGE)
            }

            minDate != null && maxDate == null -> {
                rangeStart = calculateRangeStart(dateFrom = minDate, dateTo = initialDate)
                rangeEnd = rangeStart.plusMonths(CalendarUtils.MONTHS_PER_PAGE)
            }

            minDate == null && maxDate != null -> {
                rangeEnd = calculateRangeEnd(dateFrom = initialDate, dateTo = maxDate)
                rangeStart = rangeEnd.minusMonths(CalendarUtils.MONTHS_PER_PAGE)
            }

            minDate != null && maxDate != null -> {
                if (initialDate.isBetween(minDate, maxDate)) {
                    rangeStart = calculateRangeStart(dateFrom = minDate, dateTo = initialDate)
                    rangeEnd = calculateRangeEnd(dateFrom = initialDate, dateTo = maxDate)
                } else {
                    rangeStart = minDate
                    rangeEnd = calculateRangeEnd(dateFrom = minDate, dateTo = maxDate)
                }
            }

            else -> throw IllegalStateException() // unreachable branch
        }

        return DatesRange(dateFrom = rangeStart, dateTo = rangeEnd)
    }

    private fun calculateRangeStart(
        dateFrom: CalendarDate,
        dateTo: CalendarDate
    ): CalendarDate {
        return if (dateFrom.monthsBetween(dateTo) > CalendarUtils.MONTHS_PER_PAGE) {
            dateTo.minusMonths(CalendarUtils.MONTHS_PER_PAGE)
        } else {
            dateFrom
        }
    }

    private fun calculateRangeEnd(
        dateFrom: CalendarDate,
        dateTo: CalendarDate
    ): CalendarDate {
        return if (dateFrom.monthsBetween(dateTo) > CalendarUtils.MONTHS_PER_PAGE) {
            dateFrom.plusMonths(CalendarUtils.MONTHS_PER_PAGE)
        } else {
            dateTo
        }
    }

}