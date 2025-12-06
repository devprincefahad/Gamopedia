package dev.prince.gamopedia.network

import kotlinx.coroutines.flow.Flow

enum class NetworkStatus {
    Available, Unavailable
}

interface NetworkMonitor {
    val networkStatus: Flow<NetworkStatus>
}
