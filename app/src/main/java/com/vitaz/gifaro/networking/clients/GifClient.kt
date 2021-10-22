package com.vitaz.gifaro.networking.clients

import com.vitaz.gifaro.misc.Constants
import com.vitaz.gifaro.networking.dto.GifListResponseJSON
import retrofit2.http.GET
import retrofit2.http.Query

interface GifClient {

    @GET("v1/gifs/trending")
    suspend fun getTrendingGifs (
        @Query("api_key") apiKey: String = Constants.API_KEY,
        @Query("limit") limit: Int = Constants.REQUEST_LIMIT,
        @Query("offset") offset: Int = 0,
        @Query("rating") rating: String = "g"
    ): GifListResponseJSON

    @GET("v1/gifs/search")
    suspend fun getSearchedGifs (
        @Query("api_key") apiKey: String = Constants.API_KEY,
        @Query("q") query: String,
        @Query("lang") lang: String = "en",
        @Query("limit") limit: Int = Constants.REQUEST_LIMIT,
        @Query("offset") offset: Int = 0,
        @Query("rating") rating: String = "g"
    ): GifListResponseJSON
}
