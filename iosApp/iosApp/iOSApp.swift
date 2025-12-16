import SwiftUI
import ComposeApp

@main
struct iOSApp: App {
    init() {
        KoinInit_iosKt.doInitKoin()
    }
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
