package dev.prince.gamopedia.ui.details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import dev.prince.gamopedia.database.WishlistEntity
import dev.prince.gamopedia.viewmodels.GamesViewModel
import dev.prince.gamopedia.util.GameDetailsUiState
import dev.prince.gamopedia.viewmodels.WishListViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun GameDetailsContent(
    gamesViewModel: GamesViewModel = koinViewModel(),
    wishListViewModel: WishListViewModel = koinViewModel(),
    gameId: Int
) {

    val state by gamesViewModel.detailsState.collectAsState()

    val isWishlisted by wishListViewModel.isWishlistedFlow(gameId).collectAsState(initial = false)

    LaunchedEffect(gameId) {
        gamesViewModel.fetchGameDetails(gameId)
    }

    when (state) {

        is GameDetailsUiState.Loading -> {
            Box(Modifier.fillMaxSize(), Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is GameDetailsUiState.Error -> {
            val msg = (state as GameDetailsUiState.Error).message
            Box(Modifier.fillMaxSize(), Alignment.Center) {
                Text("Error: $msg")
            }
        }

        is GameDetailsUiState.Success -> {
            val game = (state as GameDetailsUiState.Success).data
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {

                item {

                    Spacer(Modifier.height(16.dp))

                    Box {
                        AsyncImage(
                            model = game.backgroundImage ?: "",
                            contentDescription = game.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp)
                                .clip(RoundedCornerShape(16.dp)),
                            contentScale = ContentScale.Crop
                        )

                        IconButton(
                            onClick = {
                                wishListViewModel.toggleWishlist(
                                    WishlistEntity(
                                        id = game.id,
                                        name = game.name,
                                        backgroundImage = game.backgroundImage ?: ""
                                    )
                                )
                            },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(12.dp)
                                .size(40.dp)
                        ) {
                            Icon(
                                imageVector = if (isWishlisted) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Wishlist",
                                tint = if (isWishlisted) Color.Red else Color.White
                            )
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    Text(
                        text = game.name,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(12.dp))

                    Text(
                        text = game.description,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}