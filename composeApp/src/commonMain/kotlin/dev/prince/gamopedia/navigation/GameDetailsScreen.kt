package dev.prince.gamopedia.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import dev.prince.gamopedia.ui.details.GameDetails

data class GameDetailsScreen(val id: Int) : Screen {

    @Composable
    override fun Content() {
        GameDetails(gameId = id)
    }

}