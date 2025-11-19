package dev.prince.gamopedia.di

import dev.prince.gamopedia.network.ApiService
import dev.prince.gamopedia.network.KtorClient
import org.koin.dsl.module

fun getNetworkModule() = module {

    single { ApiService(httpClient = KtorClient.getInstance()) }

}