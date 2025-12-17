package dev.prince.gamopedia.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GenreResponse(
    val results: List<Genre>
)

@Serializable
data class Genre(
    val id: Int,
    val name: String,
    @SerialName("image_background")
    val backgroundImage: String? = null
)