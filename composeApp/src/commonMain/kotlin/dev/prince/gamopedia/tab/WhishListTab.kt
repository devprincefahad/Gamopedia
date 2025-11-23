package dev.prince.gamopedia.tab

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import cafe.adriel.voyager.transitions.SlideTransition
import dev.prince.gamopedia.navigation.SearchScreen
import dev.prince.gamopedia.navigation.WishListScreen

object WishListTab : Tab {

    override val options: TabOptions
        @Composable
        get() = remember {
            TabOptions(
                index = 2u,
                title = "Wishlist",
                icon = null
            )
        }

    @Composable
    override fun Content() {
        Navigator(WishListScreen()) {
            SlideTransition(it)
        }
    }

}