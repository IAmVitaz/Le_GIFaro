package com.vitaz.gifaro.database.tables.favourite

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "RMFavourite")
class Favourite (
    @PrimaryKey
    val id: String,
    val title: String,
    val bit: String,
)