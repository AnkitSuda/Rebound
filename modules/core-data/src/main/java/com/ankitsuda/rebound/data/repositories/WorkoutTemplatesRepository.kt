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

import com.ankitsuda.base.utils.generateId
import com.ankitsuda.rebound.data.db.daos.WorkoutFoldersFoldersDao
import com.ankitsuda.rebound.data.db.daos.WorkoutTemplatesDao
import com.ankitsuda.rebound.data.db.daos.WorkoutsDao
import com.ankitsuda.rebound.domain.entities.Workout
import com.ankitsuda.rebound.domain.entities.WorkoutTemplate
import com.ankitsuda.rebound.domain.entities.WorkoutTemplatesFolder
import kotlinx.coroutines.flow.firstOrNull
import java.time.LocalDateTime
import javax.inject.Inject

class WorkoutTemplatesRepository @Inject constructor(
    private val workoutTemplatesDao: WorkoutTemplatesDao,
    private val foldersDao: WorkoutFoldersFoldersDao,
    private val workoutsDao: WorkoutsDao
) {

    fun getTemplate(templateId: String) = workoutTemplatesDao.getTemplate(templateId = templateId)

    fun getVisibleTemplatesWithWorkouts(archived: Boolean) =
        workoutTemplatesDao.getVisibleTemplatesWithWorkouts(archived)

    suspend fun createTemplate(folderId: String? = null): WorkoutTemplate {
        val workoutId = generateId()
        workoutsDao.insertWorkout(
            Workout(
                id = workoutId,
                isHidden = true,
                inProgress = false,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
            )
        )

        val routineId = generateId()
        val lastListOrder = workoutTemplatesDao.getLastListOrder(folderId).firstOrNull() ?: 0

        val template =
            WorkoutTemplate(
                id = routineId,
                workoutId = workoutId,
                isArchived = false,
                isHidden = false,
                folderId = folderId,
                listOrder = lastListOrder + 1,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            )

        workoutTemplatesDao.insertTemplate(
            template
        )

        return template
    }

    suspend fun deleteTemplate(templateId: String) {
        val template = workoutTemplatesDao.getTemplate(templateId).firstOrNull()
        template?.let {
            workoutTemplatesDao.deleteTemplate(it.id)
            if (it.workoutId != null) {
                workoutsDao.deleteWorkoutById(it.workoutId!!)
            }
        }
    }

    suspend fun toggleArchiveTemplate(templateId: String) {
        val isArchived = workoutTemplatesDao.isTemplateArchived(templateId).firstOrNull()
        workoutTemplatesDao.updateTemplateIsArchived(
            templateId = templateId,
            isArchived = !(isArchived ?: false)
        )
    }

    fun getFolders() =
        foldersDao.getFolders()

    fun getFolder(folderId: String) =
        foldersDao.getFolder(folderId)

    suspend fun deleteFolder(folderId: String) {
        foldersDao.deleteFolder(folderId);
        workoutTemplatesDao.deleteFolderIdFromTemplates(folderId)
    }

    suspend fun updateFolder(folder: WorkoutTemplatesFolder) {
        foldersDao.updateFolder(folder);
    }

    suspend fun updateFolderListOrder(folderId: String, listOrder: Int) {
        foldersDao.updateFolderListOrder(folderId, listOrder);
    }

    suspend fun addFolder(name: String) {
        val date = LocalDateTime.now()
        foldersDao.insertFolder(
            WorkoutTemplatesFolder(
                id = generateId(),
                name = name,
                createdAt = date,
                updatedAt = date,
            )
        );
    }
}