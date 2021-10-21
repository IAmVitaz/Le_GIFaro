package com.vitaz.gifaro.database.tables.favourite

import androidx.lifecycle.LiveData
import com.vitaz.gifaro.networking.dto.GifObject

class FavouriteRepository(
    private val favouriteDao: FavouriteDao
) {
    suspend fun allFavourites(): MutableList<Favourite> = favouriteDao.getAllFavourites()

    suspend fun insertFavourite(gif: GifObject, entityId: Long = 0): Long {
        return favouriteDao.insert(
            Favourite(
                id = gif.id,
                title = gif.title,
                bit = gif.images.original.url
            )
        )
    }

    fun deleteFavouriteById(id: String) {
        favouriteDao.deleteFavouriteById(id)
    }


}