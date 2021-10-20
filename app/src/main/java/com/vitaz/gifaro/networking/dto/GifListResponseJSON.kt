package com.vitaz.gifaro.networking.dto


data class GifListResponseJSON(
    val data: List<GifObject>
)

data class GifObject(
    val id: String,
    val title: String,
    val rating: String,
    val images: ImageOption
)

data class ImageOption(
    val original: OriginalImageOption
)

data class OriginalImageOption (
    val height: Int,
    val width: Int,
    val url: String
)
