package dev.prince.gamopedia.tab

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.key.Key.Companion.R
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import cafe.adriel.voyager.transitions.SlideTransition
import dev.prince.gamopedia.navigation.HomeScreen
import gamopedia.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.Resource
import org.jetbrains.compose.resources.painterResource
import kotlin.jvm.Transient

class HomeTab(
    @Transient
    val onNavigator : (isRoot : Boolean) -> Unit
): Tab {

    override val options: TabOptions
        @Composable
        get() {

            val icon = rememberVectorPainter(Icons.Default.Home)

            return remember {
                TabOptions(
                    index = 0u,
                    title = "Home",
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        Navigator(HomeScreen()) {
            LaunchedEffect(it.lastItem){
                onNavigator(it.lastItem is HomeScreen)
            }
            SlideTransition(it)
        }
    }

}