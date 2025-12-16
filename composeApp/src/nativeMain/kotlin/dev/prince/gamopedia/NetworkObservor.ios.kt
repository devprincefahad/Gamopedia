package dev.prince.gamopedia

import dev.prince.gamopedia.network.NetworkObserver
import dev.prince.gamopedia.network.NetworkStatus
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import platform.Network.nw_path_get_status
import platform.Network.nw_path_monitor_cancel
import platform.Network.nw_path_monitor_create
import platform.Network.nw_path_monitor_set_queue
import platform.Network.nw_path_monitor_set_update_handler
import platform.Network.nw_path_monitor_start
import platform.Network.nw_path_status_satisfied
import platform.Network.nw_path_status_unsatisfied
import platform.darwin.dispatch_get_main_queue

class IosNetworkObserver : NetworkObserver {

    override fun observe(): Flow<NetworkStatus> = callbackFlow {
        // 1. Create the C-based monitor
        val monitor = nw_path_monitor_create()

        // 2. Define the callback
        // The C-API expects a block, which Kotlin handles as a lambda
        nw_path_monitor_set_update_handler(monitor) { path ->
            val status = nw_path_get_status(path)

            // 3. Map C-enums to our Kotlin Enum
            // nw_path_status_satisfied = 1
            // nw_path_status_unsatisfied = 2
            // nw_path_status_satisfiable = 3
            when (status) {
                nw_path_status_satisfied -> trySend(NetworkStatus.Available)
                nw_path_status_unsatisfied -> trySend(NetworkStatus.Unavailable)
                else -> trySend(NetworkStatus.Idle)
            }
        }

        // 4. Start the monitor on the main queue
        nw_path_monitor_set_queue(monitor, dispatch_get_main_queue())
        nw_path_monitor_start(monitor)

        // 5. Cleanup when Flow is cancelled
        awaitClose {
            nw_path_monitor_cancel(monitor)
        }
    }
}