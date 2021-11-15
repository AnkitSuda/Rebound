package com.ankitsuda.base.utils.extensions

import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun <T> SavedStateHandle.getStateFlow(
    key: String,
    scope: CoroutineScope,
    initialValue: T? = get(key),
): MutableStateFlow<T?> = this.let { handle ->
    val liveData = handle.getLiveData<T?>(key, initialValue).also { liveData ->
        if (liveData.value === initialValue) {
            liveData.value = initialValue
        }
    }
    val mutableStateFlow = MutableStateFlow(liveData.value)

    val observer: Observer<T?> = Observer { value ->
        if (value != mutableStateFlow.value) {
            mutableStateFlow.value = value
        }
    }
    liveData.observeForever(observer)

    scope.launch {
        mutableStateFlow.also { flow ->
            flow.onCompletion {
                withContext(Dispatchers.Main.immediate) {
                    liveData.removeObserver(observer)
                }
            }.collect { value ->
                withContext(Dispatchers.Main.immediate) {
                    if (liveData.value != value) {
                        liveData.value = value
                    }
                }
            }
        }
    }
    mutableStateFlow
}
