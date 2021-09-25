package com.ankitsuda.rebound.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "body_parts")
data class BodyPart(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    var name: String? = null,
    var image: String? = null,
)