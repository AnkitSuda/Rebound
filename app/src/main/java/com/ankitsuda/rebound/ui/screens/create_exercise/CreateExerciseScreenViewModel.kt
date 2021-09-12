package com.ankitsuda.rebound.ui.screens.create_exercise

import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreateExerciseScreenViewModel @Inject constructor() : ViewModel() {
    private var _name = MutableLiveData("")
    val name = _name

    private var _note = MutableLiveData("")
    val note = _note

    private var _isCreateBtnEnabled = MutableLiveData(false)
    val isCreateBtnEnabled = _isCreateBtnEnabled

    private var _selectedCategory = MutableLiveData("Weights & Reps")
    val selectedCategory = _selectedCategory

    private var _selectedMuscle = MutableLiveData("Abductors")
    val selectedMuscle = _selectedMuscle

    // Dummy
    val allCategories = arrayListOf("Weights & Reps", "Reps", "Distance & Time", "Time")
    val allPrimaryMuscles = arrayListOf(
        "Abductors",
        "Abs",
        "Back",
        "Biceps",
        "Calves",
        "Cardio",
        "Chest",
        "Core",
        "Forearms",
        "Glutes",
        "Hamstrings",
        "Lats",
        "Quadriceps",
        "shoulders",
        "Traps",
        "Triceps",
    )

    fun setName(value: String) {
        _name.value = value
    }

    fun setNote(value: String) {
        _note.value = value
    }

    fun setCategory(value: String) {
        _selectedCategory.value = value
    }

    fun setPrimaryMuscle(value: String) {
        _selectedMuscle.value = value
    }
}