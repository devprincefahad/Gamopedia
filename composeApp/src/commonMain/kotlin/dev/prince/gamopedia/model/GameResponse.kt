package dev.prince.gamopedia.model

import kotlinx.serialization.Serializable

@Serializable
data class GameResponse(
    var results: List<Result>
)