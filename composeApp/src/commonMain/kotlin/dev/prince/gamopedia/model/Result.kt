package dev.prince.gamopedia.model

import kotlinx.serialization.Serializable

@Serializable
data class Result(
    var id: Int,
    var name: String,
    var backgroundImage: String
)