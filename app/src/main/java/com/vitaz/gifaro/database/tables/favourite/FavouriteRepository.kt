package com.vitaz.gifaro.database.tables.favourite

import androidx.lifecycle.LiveData
import com.vitaz.gifaro.networking.dto.GifObject

class FavouriteRepository(
    private val favouriteDao: FavouriteDao
) {
    val allFavourites: LiveData<List<Favourite>> = favouriteDao.getAllFavourites()

    suspend fun insertFavourite(gif: GifObject, entityId: Long = 0): Long {
        return favouriteDao.insert(
            Favourite(
                entityId = entityId,
                id = gif.id,
                title = gif.title,
                bit = gif.images.original.url
            )
        )
    }

    fun deleteGroup(id: String) {
        favouriteDao.deleteFavouriteById(id)
    }


}