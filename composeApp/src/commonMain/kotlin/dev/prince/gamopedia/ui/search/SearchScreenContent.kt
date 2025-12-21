package dev.prince.gamopedia.ui.search

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import dev.prince.gamopedia.components.AnimateIn
import dev.prince.gamopedia.components.GameItem
import dev.prince.gamopedia.components.GamingSearchBar
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
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(
                bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding() + 80.dp
            )
        ) {

            item { Spacer(Modifier.height(42.dp)) }

            item {
                AnimateIn {
                    Text(
                        modifier = Modifier.padding(start = 16.dp),
                        text = "Search Games",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                }
            }

            item {
                AnimateIn {
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .shadow(
                                elevation = 16.dp,
                                shape = RoundedCornerShape(14.dp),
                                ambientColor = Color(0xBCCFFF4A),
                                spotColor = Color(0xA4CFFF4A)
                            )
                    ) {
                        GamingSearchBar(
                            query = query,
                            onQueryChange = viewModel::updateSearchQuery
                        )
                    }
                }
            }

            when (state) {
                SearchUiState.Idle -> {
                    item {
                        AnimateIn {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Find your next adventure...",
                                    color = Color.White.copy(alpha = 0.5f),
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }
                }

                SearchUiState.Loading -> {
                    item {
                        Box(
                            modifier = Modifier
                                .fillParentMaxSize()
                                .padding(bottom = 100.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = Color.White)
                        }
                    }
                }

                is SearchUiState.Error -> {
                    val msg = (state as SearchUiState.Error).message
                    item {
                        AnimateIn {
                            Text(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                text = "Error: $msg",
                                color = Color.Red
                            )
                        }
                    }
                }

                is SearchUiState.Success -> {
                    val results = (state as SearchUiState.Success).data

                    items(
                        items = results,
                        key = { it.id }
                    ) { game ->
                        AnimateIn {
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