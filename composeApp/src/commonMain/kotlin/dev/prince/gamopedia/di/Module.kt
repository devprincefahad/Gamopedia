package dev.prince.gamopedia.di

import dev.prince.gamopedia.database.GamesDatabase
import dev.prince.gamopedia.api.ApiService
import dev.prince.gamopedia.api.KtorClient
import dev.prince.gamopedia.repo.GamesRepository
import dev.prince.gamopedia.repo.GamesRepositoryImpl
import dev.prince.gamopedia.viewmodels.GamesViewModel
import dev.prince.gamopedia.viewmodels.SearchViewModel
import dev.prince.gamopedia.viewmodels.WishListViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformModule: Module

val sharedModule = module {

    single {
        ApiService(httpClient = KtorClient.getInstance())
    }

    single { get<GamesDatabase>().gamesDao() }

    singleOf(::GamesRepositoryImpl).bind<GamesRepository>()

    //viewModelOf(::GamesViewModel)

    viewModel {
        GamesViewModel(
            repository = get(),
            networkObserver = get()
        )
    }

    viewModel {
        WishListViewModel(repository = get())
    }

}