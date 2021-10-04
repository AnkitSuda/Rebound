package com.ankitsuda.rebound.data.repositories

import com.ankitsuda.rebound.data.daos.MusclesDao
import javax.inject.Inject

class MusclesRepository @Inject constructor(private val musclesDao: MusclesDao) {

    fun getMuscles() = musclesDao.getMuscles()
}