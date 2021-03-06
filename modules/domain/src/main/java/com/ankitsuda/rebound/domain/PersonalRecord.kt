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

package com.ankitsuda.rebound.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


val allPRs = listOf(
    MaxRepsPR(),
    MaxVolumePR(),
    MaxVolumeAddedPR(),
    MaxWeightPR(),
    MaxOneRmPR(),
    MaxDurationPR(),
    MaxDistancePR(),
    MaxWeightAddedPR(),
    BestPacePR(),
)

sealed class PersonalRecord(open val value: String) : Parcelable {
    companion object {

        fun fromCommaSpString(str: String): List<PersonalRecord> {
            val strArray = str.split(",")
            val list = arrayListOf<PersonalRecord>()

            for (item in strArray) {
                list.add(allPRs.find { it.value == item } ?: UnknownPR(item))
            }

            return list
        }
    }
}

@Parcelize
class MaxRepsPR : PersonalRecord(value = "MAX_REPS")

@Parcelize
class MaxVolumePR : PersonalRecord(value = "MAX_VOLUME")

@Parcelize
class MaxVolumeAddedPR : PersonalRecord(value = "MAX_VOLUME_ADDED")

@Parcelize
class MaxWeightPR : PersonalRecord(value = "MAX_WEIGHT")

@Parcelize
class MaxOneRmPR : PersonalRecord(value = "MAX_ONE_RM")

@Parcelize
class MaxDurationPR : PersonalRecord(value = "MAX_DURATION")

@Parcelize
class MaxDistancePR : PersonalRecord(value = "MAX_DISTANCE")

@Parcelize
class MaxWeightAddedPR : PersonalRecord(value = "MAX_WEIGHT_ADDED")

@Parcelize
class BestPacePR : PersonalRecord(value = "BEST_PACE")

@Parcelize
class UnknownPR(override val value: String) : PersonalRecord(value = value)

fun List<PersonalRecord>.toCommaSpString(): String? {
    return if (isNotEmpty()) joinToString(
        separator = ",",
        transform = {
            it.value
        }
    ) else null
}

fun ArrayList<PersonalRecord>.addIfNot(pr: PersonalRecord) {
    if (!contains(pr)) {
        add(pr)
    }
}

//enum class PersonalRecord(val value: String) {
//    MAX_REPS("MAX_REPS"),
//    MAX_VOLUME("MAX_VOLUME"),
//    MAX_VOLUME_ADDED("MAX_VOLUME_ADDED"),
//    MAX_WEIGHT("MAX_WEIGHT"),
//    MAX_ONE_RM("MAX_ONE_RM"),
//    MAX_DURATION("MAX_DURATION"),
//    MAX_DISTANCE("MAX_DISTANCE"),
//    MAX_WEIGHT_ADDED("MAX_WEIGHT_ADDED"),
//    BEST_PACE("BEST_PACE");
//
//    companion object {
//        fun fromString(value: String): PersonalRecord? {
//            return values().find { it.value == value }
//        }
//
//        fun fromCommaSpString(str: String): List<PersonalRecord>
//    }
//}