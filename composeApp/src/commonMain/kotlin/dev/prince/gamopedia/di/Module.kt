package dev.prince.gamopedia.di

import dev.prince.gamopedia.network.ApiService
import dev.prince.gamopedia.network.KtorClient
import dev.prince.gamopedia.repo.GamesRepository
import dev.prince.gamopedia.repo.GamesRepositoryImpl
import dev.prince.gamopedia.viewmodels.GamesViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformModule: Module

val sharedModule = module {

    single {
        ApiService(httpClient = KtorClient.getInstance())
    }

    singleOf(::GamesRepositoryImpl).bind<GamesRepository>()

    viewModel {
        GamesViewModel(repository = get())
    }
}