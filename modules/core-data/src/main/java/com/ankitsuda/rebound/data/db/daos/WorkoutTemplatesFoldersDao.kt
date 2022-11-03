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
interface WorkoutFoldersFoldersDao {

    @Query("SELECT * FROM workout_templates_folders ORDER BY list_order")
    fun getFolders(): Flow<List<WorkoutTemplatesFolder?>>

    @Query("SELECT * FROM workout_templates_folders WHERE id = :folderId")
    fun getFolder(folderId: String): Flow<WorkoutTemplatesFolder?>

    @Query("SELECT list_order FROM workout_templates_folders ORDER BY list_order DESC LIMIT 1")
    fun getLastListOrder(): Flow<Int>

    @Insert
    suspend fun insertFolder(folder: WorkoutTemplatesFolder)

    @Update
    suspend fun updateFolder(folder: WorkoutTemplatesFolder)

    @Query("DELETE FROM workout_templates_folders WHERE id = :folderId")
    suspend fun deleteFolder(folderId: String)

}