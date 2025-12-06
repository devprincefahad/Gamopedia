package dev.prince.gamopedia

import dev.prince.gamopedia.network.NetworkMonitor
import dev.prince.gamopedia.network.NetworkStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import platform.Foundation.NSOperationQueue
import platform.NetworkExtension.NWPathStatus

class NetworkMonitorIOS : NetworkMonitor {

    private val monitor = NWPathMonitor()
    private val queue = NSOperationQueue()

    private val _networkStatus = MutableStateFlow(NetworkStatus.Available)
    override val networkStatus: Flow<NetworkStatus> = _networkStatus

    init {
        monitor.setUpdateHandler { path ->
            when {
                path.status == NWPathStatus.Satisfied ->
                    _networkStatus.value = NetworkStatus.Available

                else ->
                    _networkStatus.value = NetworkStatus.Unavailable
            }
        }
        monitor.start(queue)
    }
}
