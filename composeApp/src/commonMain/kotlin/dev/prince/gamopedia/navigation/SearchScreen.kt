package dev.prince.gamopedia.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import dev.prince.gamopedia.ui.search.SearchScreenContent

class SearchScreen : Screen {

    @Composable
    override fun Content() {
        SearchScreenContent()
    }

}