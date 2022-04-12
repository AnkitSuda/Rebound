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

package com.ankitsuda.rebound.ui.measure

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ankitsuda.base.utils.extensions.shareWhileObserved
import com.ankitsuda.rebound.data.repositories.MeasurementsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class MeasureScreenViewModel @Inject constructor(
    private val measurementsRepository: MeasurementsRepository
) : ViewModel() {

    val bodyPartsWithGroup = measurementsRepository
        .getBodyPartsWithGroup()
        .distinctUntilChanged()
        .shareWhileObserved(viewModelScope)

}