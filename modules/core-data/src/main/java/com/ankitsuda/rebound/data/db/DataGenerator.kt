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

package com.ankitsuda.rebound.data.db

import com.ankitsuda.rebound.domain.entities.Muscle

class DataGenerator {
    companion object {
        fun getMuscles(): List<Muscle> = listOf(
            Muscle(tag = "abductors", name = "Abductors"),
            Muscle(tag = "abs", name = "Abs"),
            Muscle(tag = "back", name = "Back"),
            Muscle(tag = "biceps", name = "Biceps"),
            Muscle(tag = "calves", name = "Calves"),
            Muscle(tag = "cardio", name = "Cardio"),
            Muscle(tag = "chest", name = "Chest"),
            Muscle(tag = "core", name = "Core"),
            Muscle(tag = "forearms", name = "Forearms"),
            Muscle(tag = "glutes", name = "Glutes"),
            Muscle(tag = "hamstrings", name = "Hamstrings"),
            Muscle(tag = "lats", name = "Lats"),
            Muscle(tag = "quadriceps", name = "Quadriceps"),
            Muscle(tag = "shoulders", name = "Shoulders"),
            Muscle(tag = "traps", name = "Traps"),
            Muscle(tag = "triceps", name = "Triceps"),
        )
    }
}