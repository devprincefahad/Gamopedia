package dev.prince.gamopedia.repo

import co.touchlab.kermit.Logger
import dev.prince.gamopedia.database.GameDetailsEntity
import dev.prince.gamopedia.database.GameEntity
import dev.prince.gamopedia.database.GamesDao
import dev.prince.gamopedia.database.WishlistEntity
import dev.prince.gamopedia.model.GameDetailsResponse
import dev.prince.gamopedia.model.GameResponse
import dev.prince.gamopedia.network.ApiService
import dev.prince.gamopedia.util.toResultModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class GamesRepositoryImpl(
    private val api: ApiService,
    private val dao: GamesDao
) : GamesRepository {

    override fun getGameDetails(id: Int): Flow<Result<GameDetailsResponse>> = flow {
        // 1. Emit cached DB first
        val cached = dao.getGameDetails(id).firstOrNull()
        if (cached != null) {
            Logger.d { "Cache hit for game $id → Emitting cached details" }
            emit(
                Result.success(
                    GameDetailsResponse(
                        id = cached.id,
                        name = cached.name,
                        description = cached.description,
                        backgroundImage = cached.backgroundImage
                    )
                )
            )
        } else{
            Logger.d { "No cached details found for game $id" }
        }

        // 2. Try API
        val apiResult = api.getGameDetails(id)

        apiResult.fold(
            onSuccess = { response ->
                // 3. Save to DB only if user opened
                val entity = GameDetailsEntity(
                    id = response.id,
                    name = response.name,
                    description = response.description,
                    backgroundImage = response.backgroundImage
                )
                dao.insertGameDetails(entity)

                emit(Result.success(response))
            },
            onFailure = { e ->
                // 4. If DB existed → emit success using DB fallback
                Logger.e(e) { "API failed for game $id" }

                if (cached != null) {
                    emit(
                        Result.success(
                            GameDetailsResponse(
                                id = cached.id,
                                name = cached.name,
                                description = cached.description,
                                backgroundImage = cached.backgroundImage
                            )
                        )
                    )
                } else {
                    emit(Result.failure(e))
                }
            }
        )
    }

    override fun observeGameDetails(id: Int): Flow<GameDetailsEntity?> =
        dao.getGameDetails(id)

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
            onFailure = {
                Logger.e("Failed to refresh games", it)
            }
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
