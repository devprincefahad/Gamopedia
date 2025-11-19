package dev.prince.gamopedia

import androidx.compose.ui.window.ComposeUIViewController
import dev.prince.gamopedia.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = { initKoin() }
) {
    App()
}