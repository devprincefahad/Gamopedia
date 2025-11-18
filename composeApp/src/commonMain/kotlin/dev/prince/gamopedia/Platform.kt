package dev.prince.gamopedia

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform