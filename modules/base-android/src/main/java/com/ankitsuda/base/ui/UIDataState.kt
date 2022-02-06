package com.ankitsuda.base.ui


/**
 * State for UI containing data.
 */
sealed class UIDataState<T> {
    class Loading<T> : UIDataState<T>()
    class Success<T>(val data: T) : UIDataState<T>()
    class Failed<T>(val message: String) : UIDataState<T>()

    val isLoading get() = this is Loading

    val isSuccess get() = this is Success

    val isFailed get() = this is Failed

    companion object {
        fun <T> loading() = Loading<T>()
        fun <T> success(data: T) = Success(data)
        fun <T> failed(message: String) = Failed<T>(message)
    }
}