package dev.prince.gamopedia

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
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
        val isConnected by viewModel.status.collectAsState()
        val snackbarHostState = remember { SnackbarHostState() }

        var isVisible by remember { mutableStateOf(true) }
        val homeTab = remember {
            HomeTab(
                onNavigator = { isVisible = it }
            )
        }
        val wishListTab = remember {
            WishListTab(
                onNavigator = { isVisible = it }
            )
        }
        LaunchedEffect(isConnected) {
            if (!isConnected) {
                snackbarHostState.showSnackbar("You're offline")
            } else {
                Logger.d("Online")
            }
        }

        TabNavigator(homeTab) {

            Scaffold(
                snackbarHost = {
                    SnackbarHost(snackbarHostState)
                },
                bottomBar = {
                    AnimatedVisibility(
                        visible = isVisible,
                        enter = slideInVertically { height -> height },
                        exit = slideOutVertically { height -> height }
                    ) {
                        NavigationBar(
                            containerColor = Color(0xFF2A2A2A),
                            tonalElevation = 10.dp
                        ) {
                            TabNavigationItem(homeTab)
                            TabNavigationItem(SearchTab)
                            TabNavigationItem(wishListTab)
                        }
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
    val NeonLime = Color(0xFFD6FF4D)

    NavigationBarItem(
        selected = tabNavigator.current == tab,
        onClick = { tabNavigator.current = tab },
        label = { Text(tab.options.title, color = NeonLime) },
        icon = {
            tab.options.icon?.let {
                Icon(painter = it, contentDescription = tab.options.title)
            }
        },
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = NeonLime,
            unselectedIconColor = NeonLime,
            selectedTextColor = NeonLime,
            unselectedTextColor = NeonLime,
            indicatorColor = Color.Transparent
        )
    )

}