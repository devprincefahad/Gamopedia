package dev.prince.gamopedia.tab

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import cafe.adriel.voyager.transitions.SlideTransition
import dev.prince.gamopedia.navigation.HomeScreen
import dev.prince.gamopedia.navigation.SearchScreen
import dev.prince.gamopedia.navigation.WishListScreen
import kotlin.jvm.Transient

class SearchTab(
    @Transient
    val onNavigator: (isRoot: Boolean) -> Unit
) : Tab {

    override val options: TabOptions
        @Composable
        get() {

            val icon = rememberVectorPainter(Icons.Default.Search)

            return remember {
                TabOptions(
                    index = 1u,
                    title = "Search",
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        Navigator(SearchScreen()) {
            LaunchedEffect(it.lastItem){
                onNavigator(it.lastItem is SearchScreen)
            }
            SlideTransition(it)
        }
    }

}