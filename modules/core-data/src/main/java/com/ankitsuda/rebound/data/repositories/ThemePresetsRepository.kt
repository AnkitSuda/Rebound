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

package com.ankitsuda.rebound.data.repositories

import com.ankitsuda.base.ui.ThemeState
import com.ankitsuda.base.utils.generateId
import com.ankitsuda.rebound.data.db.daos.ThemePresetsDao
import com.ankitsuda.rebound.domain.entities.ThemePreset
import kotlinx.serialization.json.Json
import java.time.LocalDateTime
import javax.inject.Inject

class ThemePresetsRepository @Inject constructor(private val presetsDao: ThemePresetsDao) {

    fun getPresets() = presetsDao.getPresets()

    suspend fun deletePreset(preset: ThemePreset) = presetsDao.deletePreset(preset)

    suspend fun addPreset(themeState: ThemeState, title: String?, description: String?) {
        val date = LocalDateTime.now()
        val json = Json.encodeToString(ThemeState.serializer(), themeState)
        presetsDao.insertPreset(
            ThemePreset(
                id = generateId(),
                title = title,
                description = description,
                themeJson = json,
                createdAt = date,
                updatedAt = date
            )
        )
    }
}