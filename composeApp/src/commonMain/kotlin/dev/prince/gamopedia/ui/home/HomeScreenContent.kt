package dev.prince.gamopedia.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import dev.prince.gamopedia.components.GameItem
import dev.prince.gamopedia.components.GenreItem
import dev.prince.gamopedia.navigation.GameDetailsScreen
import dev.prince.gamopedia.navigation.GenreGamesScreen
import dev.prince.gamopedia.util.GamesUiState
import dev.prince.gamopedia.viewmodels.GamesViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreenContent(
    viewModel: GamesViewModel = koinViewModel()
) {

    val gamesState by viewModel.uiState.collectAsState()
    val genres by viewModel.genres.collectAsState()
    val gamesByGenreState by viewModel.gamesByGenre.collectAsState()

    val navigator = LocalNavigator.current

    Column(modifier = Modifier.fillMaxSize()) {

        /* -------------------- GENRES ROW -------------------- */
        if (genres.isNotEmpty()) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(genres) { genre ->
                    GenreItem(
                        genre = genre,
                        onClick = {
                            navigator?.push(
                                GenreGamesScreen(
                                    genreId = genre.id,
                                    genreName = genre.name
                                )
                            )
                        }
                    )
                }
            }
        }
        /* -------------------- GAMES LIST -------------------- */
        when (val state =
            if (gamesByGenreState !is GamesUiState.Loading)
                gamesByGenreState
            else
                gamesState
        ) {

            is GamesUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is GamesUiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = state.message)
                }
            }

            is GamesUiState.Success -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.data) { game ->
                        GameItem(
                            game = game,
                            onClick = {
                                navigator?.push(GameDetailsScreen(game.id))
                            }
                        )
                    }
                }
            }
        }
    }
}