package com.vitaz.gifaro.networking.dto


data class GifListResponseJSON(
    val data: List<GifObject>
)

data class GifObject(
    val id: String,
    val title: String,
    val rating: String,
    val url: String
)
