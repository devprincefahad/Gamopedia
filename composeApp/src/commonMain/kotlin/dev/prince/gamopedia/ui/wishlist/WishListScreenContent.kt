package dev.prince.gamopedia.ui.wishlist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.LocalNavigator
import dev.prince.gamopedia.components.GameItem
import dev.prince.gamopedia.navigation.GameDetailsScreen
import dev.prince.gamopedia.viewmodels.WishListViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun WishListScreenContent(
    viewModel: WishListViewModel = koinViewModel()
) {

    val navigator = LocalNavigator.current

    val wishlist by viewModel.wishlist.collectAsState()

    if (wishlist.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Your wishlist is empty️")
        }
        return
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(wishlist) { item ->

            GameItem(
                game = dev.prince.gamopedia.model.Result(
                    id = item.id,
                    name = item.name,
                    backgroundImage = item.backgroundImage
                ),
                onClick = { navigator?.push(GameDetailsScreen(item.id)) }
            )
        }
    }
}