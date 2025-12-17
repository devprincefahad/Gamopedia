package dev.prince.gamopedia.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import dev.prince.gamopedia.ui.genre.GenreContent

data class GenreGamesScreen(
    val genreId: Int,
    val genreName: String
) : Screen {

    @Composable
    override fun Content() {
        GenreContent(genreId = genreId, genreName = genreName)
    }

}