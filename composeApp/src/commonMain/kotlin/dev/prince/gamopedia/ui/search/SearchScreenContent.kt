package dev.prince.gamopedia.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import dev.prince.gamopedia.components.GameItem
import dev.prince.gamopedia.navigation.GameDetailsScreen
import dev.prince.gamopedia.util.SearchUiState
import dev.prince.gamopedia.util.backgroundGradient
import dev.prince.gamopedia.viewmodels.GamesViewModel
import dev.prince.gamopedia.viewmodels.SearchViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SearchScreenContent(
    viewModel: SearchViewModel = koinViewModel()
) {

    val navigator = LocalNavigator.current

    val query by viewModel.searchQuery.collectAsState()
    val state by viewModel.searchState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient)
    ) {

        Column(
            modifier = Modifier.fillMaxSize()
                .padding(16.dp)
        ) {

            TextField(
                value = query,
                onValueChange = { viewModel.updateSearchQuery(it) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search games...") },
                singleLine = true
            )

            Spacer(Modifier.height(16.dp))

            when (state) {
                SearchUiState.Idle -> {
                    Text("Type to search games...", color = Color.Gray)
                }

                SearchUiState.Loading -> {
                    Box(Modifier.fillMaxSize(), Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                is SearchUiState.Error -> {
                    val msg = (state as SearchUiState.Error).message
                    Text("Error: $msg")
                }

                is SearchUiState.Success -> {
                    val results = (state as SearchUiState.Success).data
                    LazyColumn {
                        items(results) { game ->
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
}