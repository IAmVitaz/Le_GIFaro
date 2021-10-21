package com.vitaz.gifaro.database.tables.favourite

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "RMFavourite")
class Favourite (
    @PrimaryKey(autoGenerate = true)
    val entityId: Long = 0,
    val id: String,
    val title: String,
    val bit: String,
)