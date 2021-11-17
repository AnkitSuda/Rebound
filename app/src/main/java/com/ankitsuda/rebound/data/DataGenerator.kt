package com.ankitsuda.rebound.data

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