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

import com.ankitsuda.base.utils.generateId
import com.ankitsuda.rebound.domain.entities.BodyPart
import com.ankitsuda.rebound.domain.entities.BodyPartUnitType
import com.ankitsuda.rebound.domain.entities.BodyPartsGroup
import com.ankitsuda.rebound.domain.entities.Muscle

class DataGenerator {
    companion object {
        fun getMuscles(): List<Muscle> = listOf(
            Muscle(tag = "abductors", name = "Abductors", isDeletable = false, isHidden = false),
            Muscle(tag = "abs", name = "Abs", isDeletable = false, isHidden = false),
            Muscle(tag = "back", name = "Back", isDeletable = false, isHidden = false),
            Muscle(tag = "biceps", name = "Biceps", isDeletable = false, isHidden = false),
            Muscle(tag = "calves", name = "Calves", isDeletable = false, isHidden = false),
            Muscle(tag = "cardio", name = "Cardio", isDeletable = false, isHidden = false),
            Muscle(tag = "chest", name = "Chest", isDeletable = false, isHidden = false),
            Muscle(tag = "body", name = "Core", isDeletable = false, isHidden = false),
            Muscle(tag = "forearms", name = "Forearms", isDeletable = false, isHidden = false),
            Muscle(tag = "glutes", name = "Glutes", isDeletable = false, isHidden = false),
            Muscle(tag = "hamstrings", name = "Hamstrings", isDeletable = false, isHidden = false),
            Muscle(tag = "lats", name = "Lats", isDeletable = false, isHidden = false),
            Muscle(tag = "quadriceps", name = "Quadriceps", isDeletable = false, isHidden = false),
            Muscle(tag = "shoulders", name = "Shoulders", isDeletable = false, isHidden = false),
            Muscle(tag = "traps", name = "Traps", isDeletable = false, isHidden = false),
            Muscle(tag = "triceps", name = "Triceps", isDeletable = false, isHidden = false),
        )

        fun getBodyPartsGroups(): List<BodyPartsGroup> = listOf(
            BodyPartsGroup(id = "core", name = "Core", isDeletable = false, isHidden = false),
            BodyPartsGroup(id = "body", name = "Body", isDeletable = false, isHidden = false),
        )

        fun getBodyPart(): List<BodyPart> = listOf(
            BodyPart(
                id = "weight",
                name = "Weight",
                groupId = "core",
                unitType = BodyPartUnitType.WEIGHT,
                isDeletable = false,
                isHidden = false
            ),
            BodyPart(
                id = "body_fat_percentage",
                name = "Body fat percentage",
                groupId = "core",
                unitType = BodyPartUnitType.PERCENTAGE,
                isDeletable = false,
                isHidden = false
            ),
            BodyPart(
                id = "calorie_intake",
                name = "Calorie intake",
                groupId = "core",
                unitType = BodyPartUnitType.CALORIES,
                isDeletable = false,
                isHidden = false
            ),
            BodyPart(
                id = "neck",
                name = "Neck",
                groupId = "body",
                unitType = BodyPartUnitType.LENGTH,
                isDeletable = false,
                isHidden = false
            ),
            BodyPart(
                id = "shoulders",
                name = "Shoulders",
                groupId = "body",
                unitType = BodyPartUnitType.LENGTH,
                isDeletable = false,
                isHidden = false
            ),
            BodyPart(
                id = "chest",
                name = "Chest",
                groupId = "body",
                unitType = BodyPartUnitType.LENGTH,
                isDeletable = false,
                isHidden = false
            ),
            BodyPart(
                id = "left_bicep",
                name = "Left bicep",
                groupId = "body",
                unitType = BodyPartUnitType.LENGTH,
                isDeletable = false,
                isHidden = false
            ),
            BodyPart(
                id = "right_bicep",
                name = "Right bicep",
                groupId = "body",
                unitType = BodyPartUnitType.LENGTH,
                isDeletable = false,
                isHidden = false
            ),
            BodyPart(
                id = "left_forearm",
                name = "Left forearm",
                groupId = "body",
                unitType = BodyPartUnitType.LENGTH,
                isDeletable = false,
                isHidden = false
            ),
            BodyPart(
                id = "right_forearm",
                name = "Right forearm",
                groupId = "body",
                unitType = BodyPartUnitType.LENGTH,
                isDeletable = false,
                isHidden = false
            ),
            BodyPart(
                id = "upper_abs",
                name = "Upper abs",
                groupId = "body",
                unitType = BodyPartUnitType.LENGTH,
                isDeletable = false,
                isHidden = false
            ),
            BodyPart(
                id = "waist",
                name = "Waist",
                groupId = "body",
                unitType = BodyPartUnitType.LENGTH,
                isDeletable = false,
                isHidden = false
            ),
            BodyPart(
                id = "lower_abs",
                name = "Lower abs",
                groupId = "body",
                unitType = BodyPartUnitType.LENGTH,
                isDeletable = false,
                isHidden = false
            ),
            BodyPart(
                id = "hips",
                name = "Hips",
                groupId = "body",
                unitType = BodyPartUnitType.LENGTH,
                isDeletable = false,
                isHidden = false
            ),
            BodyPart(
                id = "left_thigh",
                name = "Left thigh",
                groupId = "body",
                unitType = BodyPartUnitType.LENGTH,
                isDeletable = false,
                isHidden = false
            ),
            BodyPart(
                id = "right_thigh",
                name = "Right thigh",
                groupId = "body",
                unitType = BodyPartUnitType.LENGTH,
                isDeletable = false,
                isHidden = false
            ),
            BodyPart(
                id = "left_calf",
                name = "Left calf",
                groupId = "body",
                unitType = BodyPartUnitType.LENGTH,
                isDeletable = false,
                isHidden = false
            ),
            BodyPart(
                id = "right_calf",
                name = "Right calf",
                groupId = "body",
                unitType = BodyPartUnitType.LENGTH,
                isDeletable = false,
                isHidden = false
            ),
        )
    }
}