package dev.prince.gamopedia.ui.genre

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import dev.prince.gamopedia.components.GameItem
import dev.prince.gamopedia.navigation.GameDetailsScreen
import dev.prince.gamopedia.util.GamesUiState
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
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = uiState.message)
            }
        }

        is GamesUiState.Success -> {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                item { Text(text = genreName) }

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
