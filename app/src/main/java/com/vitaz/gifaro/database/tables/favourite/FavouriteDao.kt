package com.vitaz.gifaro.database.tables.favourite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavouriteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(favourite: Favourite) : Long

    @Query("SELECT * FROM RMFavourite")
    fun getAllFavourites(): MutableList<Favourite>

    @Query("DELETE FROM RMFavourite WHERE id = :id")
    fun deleteFavouriteById(id: String)
}