package dev.prince.gamopedia.ui.wishlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import dev.prince.gamopedia.navigation.GameDetailsScreen
import dev.prince.gamopedia.util.backgroundGradient
import dev.prince.gamopedia.viewmodels.WishListViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun WishListScreenContent(
    viewModel: WishListViewModel = koinViewModel()
) {
    val navigator = LocalNavigator.current
    val wishlist by viewModel.wishlist.collectAsState()

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
                        text = "Your Wishlist",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                }
            }

            if (wishlist.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier.fillParentMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        AnimateIn {
                            Text(
                                text = "Your wishlist is empty 🕸️",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.White
                            )
                        }
                    }
                }
            } else {
                items(
                    items = wishlist,
                    key = { it.id }
                ) { item ->
                    AnimateIn {
                        GameItem(
                            game = dev.prince.gamopedia.model.Result(
                                id = item.id,
                                name = item.name,
                                backgroundImage = item.backgroundImage,
                                released = item.released,
                                rating = item.rating,
                                genres = item.genre?.let { genreName ->
                                    listOf(
                                        dev.prince.gamopedia.model.Genre(
                                            id = -1,
                                            name = genreName
                                        )
                                    )
                                } ?: emptyList()
                            ),
                            onClick = { navigator?.push(GameDetailsScreen(item.id)) }
                        )
                    }
                }
            }
        }
    }
}