package com.ankitsuda.rebound.ui.screens.personalization.shapes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ankitsuda.rebound.data.datastore.PrefStorage
import com.ankitsuda.ui.theme.ShapeValues
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShapesPersonalizationScreenViewModel @Inject constructor(val prefStorage: PrefStorage) :
    ViewModel() {

    val smallTopStart = prefStorage.shapeSmallTopStartRadius
    val smallTopEnd = prefStorage.shapeSmallTopEndRadius
    val smallBottomStart = prefStorage.shapeSmallBottomStartRadius
    val smallBottomEnd = prefStorage.shapeSmallBottomEndRadius

    fun setSmalShapeValues(shapeValues: ShapeValues) {
        setSmallShapeTopStartRadius(shapeValues.topStart)
        setShapeSmallTopEndRadius(shapeValues.topEnd)
        setShapeSmallBottomStartRadius(shapeValues.bottomStart)
        setShapeSmallBottomEndRadius(shapeValues.bottomEnd)
    }

    fun setSmallShapeTopStartRadius(radius: Int) {
        viewModelScope.launch {
            prefStorage.setShapeSmallTopStartRadius(radius)
        }
    }

    fun setShapeSmallTopEndRadius(radius: Int) {
        viewModelScope.launch {
            prefStorage.setShapeSmallTopEndRadius(radius)
        }
    }

    fun setShapeSmallBottomStartRadius(radius: Int) {
        viewModelScope.launch {
            prefStorage.setShapeSmallBottomStartRadius(radius)
        }
    }

    fun setShapeSmallBottomEndRadius(radius: Int) {
        viewModelScope.launch {
            prefStorage.setShapeSmallBottomEndRadius(radius)
        }
    }


}