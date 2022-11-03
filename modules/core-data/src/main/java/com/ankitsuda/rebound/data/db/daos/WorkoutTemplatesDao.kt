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

package com.ankitsuda.rebound.data.db.daos

import androidx.room.*
import com.ankitsuda.rebound.domain.entities.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutTemplatesDao {

    @Query("SELECT * FROM workout_templates WHERE id = :templateId")
    fun getTemplate(templateId: String): Flow<WorkoutTemplate?>

    @Query("SELECT * FROM workout_templates WHERE is_hidden = 0 AND is_archived = :archived ORDER BY list_order ASC")
    fun getVisibleTemplatesWithWorkouts(archived: Boolean): Flow<List<TemplateWithWorkout>>

    @Query("SELECT is_archived FROM workout_templates WHERE id = :templateId")
    fun isTemplateArchived(templateId: String): Flow<Boolean?>

    @Query("UPDATE workout_templates SET is_archived = :isArchived WHERE id = :templateId")
    suspend fun updateTemplateIsArchived(templateId: String, isArchived: Boolean)

    @Query("SELECT list_order FROM workout_templates ORDER BY list_order DESC LIMIT 1")
    fun getLastListOrder(): Flow<Int>

    @Insert
    suspend fun insertTemplate(workoutTemplate: WorkoutTemplate)

    @Update
    suspend fun updateTemplate(workoutTemplate: WorkoutTemplate)

    @Query("DELETE FROM workout_templates WHERE id = :templateId")
    suspend fun deleteTemplate(templateId: String)

    @Query("UPDATE workout_templates SET folder_id = null WHERE folder_id = :folderId")
    suspend fun deleteFolderIdFromTemplates(folderId: String)

}