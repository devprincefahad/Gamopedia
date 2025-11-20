package dev.prince.gamopedia.repo

import dev.prince.gamopedia.model.GameResponse
import dev.prince.gamopedia.network.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface GamesRepository {

    fun getGames(): Flow<Result<GameResponse>>

}

class GamesRepositoryImpl(
    private val api: ApiService
) : GamesRepository {

    override fun getGames(): Flow<Result<GameResponse>> = flow {
        emit(api.getGames())
    }
}
