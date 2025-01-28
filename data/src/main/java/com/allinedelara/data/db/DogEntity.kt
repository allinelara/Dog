package com.allinedelara.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dog")
data class DogEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val image: String,
)