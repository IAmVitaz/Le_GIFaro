package com.vitaz.gifaro.networking.clients

import com.vitaz.gifaro.networking.dto.GifListResponseJSON
import retrofit2.http.GET
import retrofit2.http.Query

private const val API_KEY = "ZIZHJPxYXgyhUmNTYf54X6rFE6VHyFwS"

interface GifClient {

    @GET("v1/gifs/trending")
    suspend fun getTrendingGifs (
        @Query("api_key") apiKey: String = API_KEY,
        @Query("limit") limit: Int = 5,
        @Query("offset") offset: Int = 0,
        @Query("rating") rating: String = "g"
    ): GifListResponseJSON

    @GET("v1/gifs/search")
    suspend fun getSearchedGifs (
        @Query("api_key") apiKey: String = API_KEY,
        @Query("q") query: String,
        @Query("lang") lang: String = "en",
        @Query("limit") limit: Int = 5,
        @Query("offset") offset: Int = 0,
        @Query("rating") rating: String = "g"
    ): GifListResponseJSON
}
