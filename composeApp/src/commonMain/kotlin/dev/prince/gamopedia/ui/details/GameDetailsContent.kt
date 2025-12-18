package dev.prince.gamopedia.ui.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType.Companion.Uri
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.util.TableInfo
import coil3.compose.AsyncImage
import dev.prince.gamopedia.components.ExpandableDescription
import dev.prince.gamopedia.components.PlatformChips
import dev.prince.gamopedia.components.ScreenshotsSection
import dev.prince.gamopedia.database.WishlistEntity
import dev.prince.gamopedia.viewmodels.GamesViewModel
import dev.prince.gamopedia.util.GameDetailsUiState
import dev.prince.gamopedia.util.backgroundGradient
import dev.prince.gamopedia.viewmodels.WishListViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun GameDetailsContent(
    gamesViewModel: GamesViewModel = koinViewModel(),
    wishListViewModel: WishListViewModel = koinViewModel(),
    gameId: Int
) {

    val state by gamesViewModel.detailsState.collectAsState()
    val isWishlisted by wishListViewModel
        .isWishlistedFlow(gameId)
        .collectAsState(initial = false)

    val screenshotsState by gamesViewModel.screenshots.collectAsState()

    val uriHandler = LocalUriHandler.current

    LaunchedEffect(gameId) {
        gamesViewModel.fetchGameDetails(gameId)
        gamesViewModel.loadScreenshots(gameId)
    }
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient)
    ) {
        when (state) {

            is GameDetailsUiState.Loading -> {
                Box(Modifier.fillMaxSize(), Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            is GameDetailsUiState.Error -> {
                Box(Modifier.fillMaxSize(), Alignment.Center) {
                    Text("Something went wrong", color = Color.White)
                }
            }

            is GameDetailsUiState.Success -> {
                val game = (state as GameDetailsUiState.Success).data
                val platforms = game.parentPlatforms.map { it.platform.name }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                ) {

                    Box {
                        AsyncImage(
                            model = game.backgroundImage ?: "",
                            contentDescription = game.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(320.dp),
                            contentScale = ContentScale.Crop
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                                .align(Alignment.BottomCenter)
                                .background(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            Color.Black.copy(alpha = 0.85f)
                                        )
                                    )
                                )
                        )

                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(horizontal = 16.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = game.name,
                                color = Color.White,
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Bold,

                                )

                            Text(
                                text = game.rating.toString(),
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }


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
                                .align(Alignment.BottomEnd)
                                .padding(16.dp)
                                .size(44.dp)
                                .background(
                                    Color.White.copy(alpha = 0.15f),
                                    shape = CircleShape
                                )
                        ) {
                            Icon(
                                imageVector = if (isWishlisted)
                                    Icons.Default.Favorite
                                else
                                    Icons.Default.FavoriteBorder,
                                contentDescription = null,
                                tint = if (isWishlisted)
                                    GlowYellow
                                else Color.White
                            )
                        }
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(
                            topStart = 24.dp,
                            topEnd = 24.dp
                        ),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF1E1E1E)
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp)
                        ) {

                            Spacer(Modifier.height(12.dp))

                            Button(
                                onClick = {
                                    uriHandler.openUri(game.website)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFA5FF4A),
                                    contentColor = Color.Black
                                ),
                                shape = RoundedCornerShape(26.dp)
                            ) {
                                Text(
                                    text = "Download",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            Spacer(Modifier.height(12.dp))

                            Text(
                                text = "Gameplay",
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(Modifier.height(12.dp))

                            ScreenshotsSection(screenshotsState)

                            Spacer(Modifier.height(12.dp))

                            Text(
                                text = "About",
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(Modifier.height(12.dp))

                            ExpandableDescription(
                                text = game.description
                            )

                            Spacer(Modifier.height(12.dp))

                            if (platforms.isNotEmpty()) {
                                Spacer(Modifier.height(12.dp))

                                PlatformChips(
                                    platforms = platforms
                                )
                            }

                            Spacer(Modifier.height(16.dp))

                        }
                    }
                }
            }
        }
    }
}

private val GlowYellow = Color(0xFFA5FF4A)