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

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable
enum class WeightUnit(val value: String) {
    KG("kg"),
    LBS("lbs");

    companion object {
        fun fromValue(value: String): WeightUnit {
            return values().find { it.value == value } ?: KG
        }
    }
}

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = WeightUnit::class)
object WeightUnitSerializer : KSerializer<WeightUnit> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("WeightUnit", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: WeightUnit) {
        encoder.encodeString(value.value)
    }

    override fun deserialize(decoder: Decoder): WeightUnit {
        return try {
            val key = decoder.decodeString()
            WeightUnit.fromValue(key)
        } catch (e: IllegalArgumentException) {
            WeightUnit.KG
        }
    }
}
