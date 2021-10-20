package com.vitaz.gifaro.networking.clients

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object GifService {
    private val BASE_URL = "https://api.giphy.com/"

    fun getGifs(): GifClient {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GifClient::class.java)
    }
}
