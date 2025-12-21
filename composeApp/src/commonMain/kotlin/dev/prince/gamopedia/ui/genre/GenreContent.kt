package dev.prince.gamopedia.ui.genre

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import dev.prince.gamopedia.components.BackButton
import dev.prince.gamopedia.components.GameItem
import dev.prince.gamopedia.navigation.GameDetailsScreen
import dev.prince.gamopedia.util.GamesUiState
import dev.prince.gamopedia.util.backgroundGradient
import dev.prince.gamopedia.viewmodels.GamesViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun GenreContent(
    genreId: Int,
    genreName: String
) {

    val viewModel = koinViewModel<GamesViewModel>()

    val state by viewModel.gamesByGenre.collectAsState()

    val navigator = LocalNavigator.current

    LaunchedEffect(genreId) {
        viewModel.fetchGamesByGenre(genreId)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient)
    ) {

        when (val uiState = state) {

            is GamesUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is GamesUiState.Error -> {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    BackButton(
                        onClick = { navigator?.pop() },
                        modifier = Modifier.padding(start = 26.dp, top = 46.dp),
                        size = 26.dp
                    )
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = uiState.message)
                    }
                }
            }

            is GamesUiState.Success -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    top = 42.dp,
                                    start = 16.dp,
                                    end = 16.dp,
                                    bottom = 8.dp
                                ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            BackButton(
                                onClick = { navigator?.pop() },
                                size = 26.dp,
                                modifier = Modifier.padding(horizontal = 9.dp)
                            )

                            Spacer(Modifier.width(12.dp))

                            Text(
                                text = genreName,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp
                            )
                        }
                    }

                    items(uiState.data) { game ->
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
        }
    }
}