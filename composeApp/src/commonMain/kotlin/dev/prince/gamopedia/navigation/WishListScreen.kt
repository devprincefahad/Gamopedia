package dev.prince.gamopedia.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import dev.prince.gamopedia.ui.wishlist.WishListScreenContent

class WishListScreen : Screen{

    @Composable
    override fun Content() {
        WishListScreenContent()
    }

}