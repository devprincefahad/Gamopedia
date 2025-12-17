package dev.prince.gamopedia.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import dev.prince.gamopedia.components.GameItem
import dev.prince.gamopedia.components.GenreChipsRow
import dev.prince.gamopedia.components.TopGameItem
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
    val topRatedGamesState by viewModel.topRatedGames.collectAsState()

    val navigator = LocalNavigator.current

    val backgroundGradient = Brush.verticalGradient(
        colorStops = arrayOf(
            0.0f to Color(0xFF230F49),
            0.5f to Color(0xFF000000)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient)
    ) {

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            item { Spacer(Modifier.height(32.dp)) }

            // ---------- Categories ----------
            item {
                Text(
                    modifier = Modifier.padding(start = 16.dp),
                    text = "Categories",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }

            item {
                if (genres.isNotEmpty()) {
                    GenreChipsRow(
                        genres = genres,
                        onGenreClick = { genre ->
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

            // ---------- Top Games ----------
            item {
                Text(
                    modifier = Modifier.padding(start = 16.dp),
                    text = "Top Games",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }

            item {
                when (val state = topRatedGamesState) {
                    is GamesUiState.Success -> {
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(state.data) { game ->
                                TopGameItem(
                                    game = game,
                                    onClick = {
                                        navigator?.push(
                                            GameDetailsScreen(game.id)
                                        )
                                    }
                                )
                            }
                        }
                    }

                    else -> Unit
                }
            }

            // ---------- Best of All Time ----------
            item {
                Text(
                    modifier = Modifier.padding(start = 16.dp),
                    text = "Best Of All Time",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }

            when (
                val state =
                    if (gamesByGenreState !is GamesUiState.Loading)
                        gamesByGenreState
                    else
                        gamesState
            ) {
                is GamesUiState.Loading -> {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }

                is GamesUiState.Error -> {
                    item {
                        Text(
                            text = state.message,
                            color = Color.White,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }

                is GamesUiState.Success -> {
                    items(state.data) { game ->
                        GameItem(
                            game = game,
                            onClick = {
                                navigator?.push(
                                    GameDetailsScreen(game.id)
                                )
                            }
                        )
                    }
                }
            }

            item { Spacer(Modifier.height(24.dp)) }
        }
    }
}
