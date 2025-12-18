package dev.prince.gamopedia.model

import kotlinx.serialization.Serializable

@Serializable
data class ScreenshotsResponse(
    val results: List<ScreenshotDto>
)

@Serializable
data class ScreenshotDto(
    val id: Int,
    val image: String
)