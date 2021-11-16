package com.ankitsuda.domain.models

/**
 * A generic class that holds a value with its loading status.
 */
data class Resource(val status: Status, val error: Throwable? = null)

enum class Status {
    SUCCESS,
    ERROR,
    LOADING,
    REFRESHING,
    LOADING_MORE
}
