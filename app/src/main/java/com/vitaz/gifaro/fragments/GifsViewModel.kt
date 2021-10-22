package com.vitaz.gifaro.fragments

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vitaz.gifaro.MainApplication
import com.vitaz.gifaro.database.GifaroRoomDatabase
import com.vitaz.gifaro.database.tables.favourite.Favourite
import com.vitaz.gifaro.database.tables.favourite.FavouriteRepository
import com.vitaz.gifaro.misc.Constants
import com.vitaz.gifaro.networking.clients.GifService
import com.vitaz.gifaro.networking.dto.GifListResponseJSON
import com.vitaz.gifaro.networking.dto.GifObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException

class GifsViewModel: ViewModel() {

    private val gifService = GifService.getGifs()
    var gifList = MutableLiveData<List<GifObject>>()
    var searchText = MutableLiveData<String>("")
    var currentPage: Int = 0

    var favouriteList = MutableLiveData<MutableList<Favourite>>()

    private val favouriteRepository: FavouriteRepository

    init {
        val favouriteDao =
            GifaroRoomDatabase.getDatabase(
                MainApplication.instance.applicationContext,
                viewModelScope
            ).favouriteDao()

        favouriteRepository = FavouriteRepository(favouriteDao)
    }

    fun getNewData() {
        viewModelScope.launch(Dispatchers.IO) {
            var currentOperation: String = ""
            try {
                val response = if (searchText.value == "") {
                    currentOperation = "GetTrendingGifs, offset: $currentPage"
                    gifService.getTrendingGifs(offset = currentPage * Constants.REQUEST_LIMIT)
                }
                else {
                    currentOperation = "GetSearchableGifs, offset: $currentPage"
                    gifService.getSearchedGifs(query = searchText.value!!, offset = currentPage * Constants.REQUEST_LIMIT)
                }
                val newList: List<GifObject> = if (currentPage == 0) response.data
                else gifList.value!!.plus( response.data )

                gifList.postValue(newList)
                currentPage ++
            } catch (e: HttpException) {
                handleHttpException(currentOperation, e)
            } catch (e: Exception) {
                Log.i(currentOperation, "An Error Occurred: ${e.message}")
            }
        }
    }

    private fun handleHttpException(tag: String, e: HttpException) {
        if (e.code() != 500) {
            try {
                val message = e.response()?.errorBody()
                if (message != null) {
                    val messageJSON = JSONObject(message.string())
                    Log.i(tag, "HttpException: $messageJSON")
                }
            } catch (e: JSONException) {
                Log.i(tag, "JSONException: $e")
            }
        }
    }

    fun addNewFavourite(gif: GifObject) {
        viewModelScope.launch(Dispatchers.IO) {
            favouriteRepository.insertFavourite(gif)

            val newFavouriteList = favouriteList.value
            newFavouriteList?.add(Favourite(
                    id = gif.id,
                    title = gif.title,
                    bit = gif.images.original.url
                )
            )

            if (newFavouriteList != null) {
                favouriteList.postValue(newFavouriteList!!)
            }
        }
    }

    fun deleteFromFavourite(gifId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            favouriteRepository.deleteFavouriteById(gifId)

            val newFavouriteList = favouriteList.value
            val elementToDelete = newFavouriteList?.find { it.id == gifId }
            newFavouriteList?.remove(elementToDelete)

            if (newFavouriteList != null) {
                favouriteList.postValue(newFavouriteList!!)
            }
        }
    }

    fun getFavouriteList() {
        viewModelScope.launch(Dispatchers.IO) {
            favouriteList.postValue(favouriteRepository.allFavourites())
        }
    }
}
