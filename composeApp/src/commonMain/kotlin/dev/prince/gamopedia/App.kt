package dev.prince.gamopedia

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import co.touchlab.kermit.Logger
import dev.prince.gamopedia.tab.HomeTab
import dev.prince.gamopedia.tab.SearchTab
import dev.prince.gamopedia.tab.WishListTab
import dev.prince.gamopedia.viewmodels.GamesViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App() {
    MaterialTheme {
//        Navigator(screen = HomeScreen()) {
//            SlideTransition(navigator = it)
//        }

        val viewModel: GamesViewModel = koinViewModel()
        val isConnected by viewModel.isConnected.collectAsState()
        val snackbarHostState = remember { SnackbarHostState() }

        LaunchedEffect(isConnected) {
            if (!isConnected) {
                snackbarHostState.showSnackbar("You're offline")
            }
            else {
                Logger.d("Online")
            }
        }

        TabNavigator(HomeTab) {

            Scaffold(
                snackbarHost = {
                    SnackbarHost(snackbarHostState)
                },
                bottomBar = {
                    NavigationBar(
                        tonalElevation = 10.dp
                    ) {
                        TabNavigationItem(HomeTab)
                        TabNavigationItem(SearchTab)
                        TabNavigationItem(WishListTab)
                    }
                }
            ) {
                CurrentTab()
            }
        }
    }
}

@Composable
private fun RowScope.TabNavigationItem(tab: Tab) {

    val tabNavigator = LocalTabNavigator.current

    NavigationBarItem(
        selected = tabNavigator.current == tab,
        onClick = { tabNavigator.current = tab },
        label = { Text(tab.options.title) },
        icon = {
            tab.options.icon?.let {
                Icon(painter = it, contentDescription = tab.options.title)
            }
        }
    )

}