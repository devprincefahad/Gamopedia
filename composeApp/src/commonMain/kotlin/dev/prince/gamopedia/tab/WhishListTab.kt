package dev.prince.gamopedia.tab

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import cafe.adriel.voyager.transitions.SlideTransition
import dev.prince.gamopedia.navigation.HomeScreen
import dev.prince.gamopedia.navigation.WishListScreen
import kotlin.jvm.Transient

class WishListTab(
    @Transient
    val onNavigator : (isRoot : Boolean) -> Unit
): Tab {

    override val options: TabOptions
        @Composable
        get() {

            val icon = rememberVectorPainter(Icons.Default.Bookmark)

            return remember {
                TabOptions(
                    index = 2u,
                    title = "Wishlist",
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        Navigator(WishListScreen()) {
            LaunchedEffect(it.lastItem){
                onNavigator(it.lastItem is WishListScreen)
            }
            SlideTransition(it)
        }
    }

}