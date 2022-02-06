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
 * This class represents a range of dates where [dateFrom] and [dateTo] are optional.
 */
data class NullableDatesRange(
    val dateFrom: CalendarDate? = null,
    val dateTo: CalendarDate? = null

) : Parcelable {

    constructor(parcel: Parcel) : this(
        dateFrom = parcel.readParcelable(CalendarDate::class.java.classLoader),
        dateTo = parcel.readParcelable(CalendarDate::class.java.classLoader)
    )

    companion object {
        @JvmField
        val CREATOR = object : Parcelable.Creator<NullableDatesRange> {
            override fun createFromParcel(parcel: Parcel) = NullableDatesRange(parcel)

            override fun newArray(size: Int) = arrayOfNulls<NullableDatesRange>(size)
        }

    }

    override fun writeToParcel(
        dest: Parcel, flags: Int
    ) {
        dest.writeParcelable(dateFrom, flags)
        dest.writeParcelable(dateTo, flags)
    }

    override fun describeContents() = 0

    fun isDateOutOfRange(date: CalendarDate): Boolean {
        return (dateFrom != null && date < dateFrom) || (dateTo != null && date > dateTo)
    }

}