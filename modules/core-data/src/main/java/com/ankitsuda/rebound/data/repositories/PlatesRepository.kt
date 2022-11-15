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

import com.ankitsuda.rebound.data.db.daos.MusclesDao
import com.ankitsuda.rebound.data.db.daos.PlatesDao
import com.ankitsuda.rebound.domain.WeightUnit
import com.ankitsuda.rebound.domain.entities.Plate
import javax.inject.Inject

class PlatesRepository @Inject constructor(private val platesDao: PlatesDao) {

    fun getPlates() = platesDao.getPlates()

    fun getActivePlates(forWeightUnit: WeightUnit) = platesDao.getActivePlates(forWeightUnit)

    fun getPlate(plateId: String) = platesDao.getPlate(plateId)

    suspend fun upsertPlate(plate: Plate) = platesDao.insertPlate(plate)

    suspend fun updateIsActive(plateId: String, isActive: Boolean) {
        platesDao.updateIsActive(plateId, isActive)
    }

    suspend fun deletePlate(plateId: String) {
        platesDao.deletePlate(plateId)
    }
}