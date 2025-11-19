package dev.prince.gamopedia.di

import dev.prince.gamopedia.ui.game.GamesViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

actual val platformModule: Module = module {
    viewModelOf(::GamesViewModel)
}