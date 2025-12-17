package dev.prince.gamopedia.model

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class GamesByGenreResponse(
    val results: List<Result>
)

//@kotlinx.serialization.Serializable
//data class GamesByGenre(
//    val id: Int,
//    val name: String,
//    @SerialName("background_image")
//    val backgroundImage: String? = null
//)