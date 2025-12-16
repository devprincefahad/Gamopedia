package dev.prince.gamopedia.network

import kotlinx.coroutines.flow.Flow

enum class NetworkStatus {
    Idle,
    Available,
    Unavailable,
    Losing,
    Lost
}

interface NetworkObserver {
    fun observe(): Flow<NetworkStatus>
}