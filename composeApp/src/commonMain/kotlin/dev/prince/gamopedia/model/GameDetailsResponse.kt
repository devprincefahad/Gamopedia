package dev.prince.gamopedia.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GameDetailsResponse(
    val id: Int,
    val name: String,
    @SerialName("description_raw")
    val description: String,
    @SerialName("background_image")
    val backgroundImage: String
)
