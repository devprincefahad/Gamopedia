package dev.prince.gamopedia.ui.game

import androidx.lifecycle.ViewModel
import dev.prince.gamopedia.network.ApiService
import dev.prince.gamopedia.repo.GamesRepositoryImpl

class GamesViewModel(
    private val apiService: ApiService,
    private val gamesRepository: GamesRepositoryImpl
) : ViewModel() {

    fun getHelloWorld() : String {
        return gamesRepository.helloWorld()
    }

}