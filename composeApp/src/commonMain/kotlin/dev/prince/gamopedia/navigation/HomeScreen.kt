package dev.prince.gamopedia.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import dev.prince.gamopedia.ui.game.GamesScreen

class HomeScreen : Screen {

    @Composable
    override fun Content() {
        GamesScreen()
    }

}