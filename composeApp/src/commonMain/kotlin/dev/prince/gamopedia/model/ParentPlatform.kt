package dev.prince.gamopedia.model

import kotlinx.serialization.Serializable

@Serializable
data class ParentPlatform(
    val platform: Platform
)

@Serializable
data class Platform(
    val id: Int,
    val name: String,
    val slug: String
)