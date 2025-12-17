package dev.prince.gamopedia.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Result(
    var id: Int,
    var name: String,
    @SerialName("background_image")
    var backgroundImage: String? = null,
    val released: String? = null,
    val rating: Double? = null,
    val genres: List<Genre> = emptyList()
)