package com.vitaz.gifaro.fragments

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vitaz.gifaro.networking.clients.GifService
import com.vitaz.gifaro.networking.dto.GifObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException

class GifsViewModel: ViewModel() {

    private val gifService = GifService.getGifs()
    var gifList = MutableLiveData<List<GifObject>>()

    fun getTrending() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.d("API REQUEST", "Sending Api request getTrendingGifs ...")
                val response = gifService.getTrendingGifs()
                gifList.postValue(response.data)
                Log.d("API REQUEST", "Api request getTrendingGifs has been sent and response received")
            } catch (e: HttpException) {
                handleHttpException("GetTrendingGifs", e)
            } catch (e: Exception) {
                Log.i("GetTrendingGifs", "An Error Occurred: ${e.message}")
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
}
