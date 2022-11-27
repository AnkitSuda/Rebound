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

import com.ankitsuda.rebound.data.db.daos.BarbellsDao
import com.ankitsuda.rebound.domain.WeightUnit
import com.ankitsuda.rebound.domain.entities.Barbell
import javax.inject.Inject

class BarbellsRepository @Inject constructor(private val barbellsDao: BarbellsDao) {

    fun getBarbells() = barbellsDao.getBarbells()

    fun getActiveBarbells() = barbellsDao.getActiveBarbells()

    fun getBarbell(barbellId: String) = barbellsDao.getBarbell(barbellId)

    suspend fun upsertBarbell(barbell: Barbell) = barbellsDao.insertBarbell(barbell)

    suspend fun updateIsActive(barbellId: String, isActive: Boolean) {
        barbellsDao.updateIsActive(barbellId, isActive)
    }

    suspend fun deleteBarbell(barbellId: String) {
        barbellsDao.deleteBarbell(barbellId)
    }

}