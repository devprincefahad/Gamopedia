package dev.prince.gamopedia.ui.game

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.LocalNavigator
import dev.prince.gamopedia.components.GameItem
import dev.prince.gamopedia.navigation.GameDetailsScreen
import dev.prince.gamopedia.util.GamesUiState
import dev.prince.gamopedia.viewmodels.GamesViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun GamesScreenContent(
    viewModel: GamesViewModel = koinViewModel()
) {

    val state by viewModel.uiState.collectAsState()
    val navigator = LocalNavigator.current

    when (state) {
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
                val msg = (state as GamesUiState.Error).message
                Text("Error: $msg")
            }
        }

        is GamesUiState.Success -> {
            LazyColumn {
                val games = (state as GamesUiState.Success).data
                items(games) { game ->
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
