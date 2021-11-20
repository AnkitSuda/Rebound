package com.ankitsuda.domain.models

import kotlinx.serialization.json.Json

val DEFAULT_JSON_FORMAT = Json {
    ignoreUnknownKeys = true
}
