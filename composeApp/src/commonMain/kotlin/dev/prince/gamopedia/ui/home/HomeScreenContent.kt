package dev.prince.gamopedia.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import dev.prince.gamopedia.components.AnimateIn
import dev.prince.gamopedia.components.GameItem
import dev.prince.gamopedia.components.GenreChipsRow
import dev.prince.gamopedia.components.TopGameItem
import dev.prince.gamopedia.navigation.GameDetailsScreen
import dev.prince.gamopedia.navigation.GenreGamesScreen
import dev.prince.gamopedia.util.GamesUiState
import dev.prince.gamopedia.util.backgroundGradient
import dev.prince.gamopedia.viewmodels.GamesViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreenContent(
    viewModel: GamesViewModel = koinViewModel()
) {

    val gamesState by viewModel.uiState.collectAsState()
    val genres by viewModel.genres.collectAsState()
    val topRatedGamesState by viewModel.topRatedGames.collectAsState()
    val gamesByGenreState by viewModel.gamesByGenre.collectAsState()

    val isConnected by viewModel.status.collectAsState()

    val navigator = LocalNavigator.current

    val isOfflineAndEmpty = (gamesState is GamesUiState.Loading) && !isConnected

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(
                bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding() + 80.dp
            )
        ) {

            if (!isOfflineAndEmpty) {

                item { Spacer(Modifier.height(34.dp)) }

                item {
                    AnimateIn {
                        Text(
                            modifier = Modifier.padding(start = 16.dp),
                            text = "Categories",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }
                }

                item {
                    if (genres.isNotEmpty()) {
                        AnimateIn {
                            GenreChipsRow(
                                genres = genres,
                                onGenreClick = { genre ->
                                    navigator?.push(GenreGamesScreen(genre.id, genre.name))
                                }
                            )
                        }
                    }
                }

                item {
                    AnimateIn {
                        Text(
                            modifier = Modifier.padding(start = 16.dp, top = 8.dp),
                            text = "Top Games",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }
                }

                item {
                    if (topRatedGamesState is GamesUiState.Success) {
                        val games = (topRatedGamesState as GamesUiState.Success).data
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(games, key = { it.id }) { game ->
                                AnimateIn {
                                    TopGameItem(
                                        game = game,
                                        onClick = { navigator?.push(GameDetailsScreen(game.id)) }
                                    )
                                }
                            }
                        }
                    }
                }

                item {
                    AnimateIn {
                        Text(
                            modifier = Modifier.padding(start = 16.dp, top = 8.dp),
                            text = "Best Of All Time",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }
                }
            }

            val state = if (gamesByGenreState !is GamesUiState.Loading) gamesByGenreState else gamesState

            when (state) {
                is GamesUiState.Loading -> {
                    item {
                        if (!isConnected) {
                            Box(
                                modifier = Modifier.fillParentMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = "No Internet Connection 🔌",
                                        color = Color.White.copy(alpha = 0.6f),
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Spacer(Modifier.height(8.dp))
                                    Text(
                                        text = "Check your connection to explore",
                                        color = Color.White.copy(alpha = 0.4f),
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(color = Color.White)
                            }
                        }
                    }
                }

                is GamesUiState.Error -> {
                    val msg = state.message
                    item {
                        AnimateIn {
                            Text(
                                text = msg,
                                color = Color.Red,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }

                is GamesUiState.Success -> {
                    val games = state.data
                    items(games, key = { it.id }) { game ->
                        AnimateIn {
                            GameItem(
                                game = game,
                                onClick = { navigator?.push(GameDetailsScreen(game.id)) }
                            )
                        }
                    }
                }
            }

            item { Spacer(Modifier.height(24.dp)) }
        }
    }
}