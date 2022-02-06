/*
 * Copyright (c) 2022 Ankit Suda.
 *
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 */

package com.ankitsuda.base.util

import android.os.Parcel
import android.os.Parcelable

/**
 * This class represents a range of dates from [dateFrom] to [dateTo].
 */
 data class DatesRange(
    val dateFrom: CalendarDate,
    val dateTo: CalendarDate

) : Parcelable {

    constructor(parcel: Parcel) : this(
        dateFrom = requireNotNull(parcel.readParcelable(CalendarDate::class.java.classLoader)),
        dateTo = requireNotNull(parcel.readParcelable(CalendarDate::class.java.classLoader))
    )

    companion object {

        fun emptyRange(): DatesRange {
            val todayCalendarDate = CalendarDate.today
            return DatesRange(todayCalendarDate, todayCalendarDate)
        }

        @JvmField
        val CREATOR = object : Parcelable.Creator<DatesRange> {
            override fun createFromParcel(parcel: Parcel) = DatesRange(parcel)

            override fun newArray(size: Int) = arrayOfNulls<DatesRange>(size)
        }
    }

    val isEmptyRange: Boolean get() = dateFrom == dateTo

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeParcelable(dateFrom, flags)
        dest.writeParcelable(dateTo, flags)
    }

    override fun describeContents() = 0

}