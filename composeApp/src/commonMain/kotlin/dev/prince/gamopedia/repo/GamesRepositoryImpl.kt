package dev.prince.gamopedia.repo

import dev.prince.gamopedia.database.GameEntity
import dev.prince.gamopedia.database.GamesDao
import dev.prince.gamopedia.database.WishlistEntity
import dev.prince.gamopedia.model.GameDetailsResponse
import dev.prince.gamopedia.model.GameResponse
import dev.prince.gamopedia.network.ApiService
import dev.prince.gamopedia.util.toResultModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class GamesRepositoryImpl(
    private val api: ApiService,
    private val dao: GamesDao
) : GamesRepository {

    override fun getGames(): Flow<Result<GameResponse>> = flow {
        emit(api.getGames())
    }

    override fun getGameDetails(id: Int): Flow<Result<GameDetailsResponse>> = flow {
        emit(api.getGameDetails(id))
    }

    override fun searchGames(query: String): Flow<Result<GameResponse>> = flow {
        try {
            val response = api.searchGames(query)
            emit(Result.success(response))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override fun observeGames(): Flow<List<dev.prince.gamopedia.model.Result>> =
        dao.getCachedGames()
            .map { list -> list.map { it.toResultModel() } }
            .distinctUntilChanged()

    override suspend fun refreshGames() {
        api.getGames().fold(
            onSuccess = { response ->
                val entities = response.results.map { dto ->
                    GameEntity(
                        id = dto.id,
                        name = dto.name,
                        backgroundImage = dto.backgroundImage
                    )
                }
                dao.clearGames()
                dao.insertGames(entities)
            },
            onFailure = { /* log error */ }
        )
    }

    override fun observeWishlist(): Flow<List<WishlistEntity>> =
        dao.getWishlist()

    override fun isWishlisted(id: Int): Flow<Boolean> =
        dao.isWishlisted(id)

    override suspend fun addToWishlist(item: WishlistEntity) =
        dao.addToWishlist(item)

    override suspend fun removeFromWishlist(item: WishlistEntity) =
        dao.removeFromWishlist(item)

}
