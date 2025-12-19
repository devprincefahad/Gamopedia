package dev.prince.gamopedia.repo

import co.touchlab.kermit.Logger
import dev.prince.gamopedia.database.GameDetailsEntity
import dev.prince.gamopedia.database.GameEntity
import dev.prince.gamopedia.database.GamesDao
import dev.prince.gamopedia.model.GameDetailsResponse
import dev.prince.gamopedia.model.GameResponse
import dev.prince.gamopedia.api.ApiService
import dev.prince.gamopedia.database.GenreEntity
import dev.prince.gamopedia.database.ScreenshotEntity
import dev.prince.gamopedia.database.WishlistEntity
import dev.prince.gamopedia.model.GamesByGenreResponse
import dev.prince.gamopedia.model.Genre
import dev.prince.gamopedia.model.ParentPlatform
import dev.prince.gamopedia.model.Platform
import dev.prince.gamopedia.model.ScreenshotDto
import dev.prince.gamopedia.util.toResultModel
import dev.prince.gamopedia.util.toGenreModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
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
                        backgroundImage = cached.backgroundImage,
                        website = cached.website,
                        rating = cached.rating,
                        parentPlatforms = cached.platforms
                            ?.split(", ")
                            ?.map {
                                ParentPlatform(
                                    Platform(
                                        id = -1,
                                        name = it,
                                        slug = it.lowercase()
                                    )
                                )
                            }
                            ?: emptyList()
                    )
                )
            )
        } else {
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
                    backgroundImage = response.backgroundImage,
                    website = response.website,
                    rating = response.rating,
                    platforms = response.parentPlatforms.joinToString(", ") { it.platform.name }
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
                                backgroundImage = cached.backgroundImage,
                                website = cached.website,
                                rating = cached.rating,
                                parentPlatforms = cached.platforms?.split(", ")
                                    ?.map {
                                        ParentPlatform(
                                            Platform(
                                                id = -1,
                                                name = it,
                                                slug = it.lowercase()
                                            )
                                        )
                                    }
                                    ?: emptyList()
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
                        backgroundImage = dto.backgroundImage,
                        released = dto.released,
                        rating = dto.rating,
                        genre = dto.genres.firstOrNull()?.name
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

    override fun observeGenres(): Flow<List<Genre>> =
        dao.observeGenres()
            .map { it.map { entity -> entity.toGenreModel() } }
            .distinctUntilChanged()

    override suspend fun refreshGenres() {
        api.getGenres().fold(
            onSuccess = { response ->
                val entities = response.results.map {
                    GenreEntity(
                        id = it.id,
                        name = it.name,
                        imageBackground = it.backgroundImage
                    )
                }
                dao.clearGenres()
                dao.insertGenres(entities)
            },
            onFailure = {
                Logger.e(it) { "Failed to refresh genres" }
            }
        )
    }

    override fun getGamesByGenre(
        genreId: Int
    ): Flow<Result<GamesByGenreResponse>> = flow {
        try {
            val response = api.getGamesByGenre(genreId)
            emit(Result.success(response))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override fun getGameScreenshots(
        id: Int
    ): Flow<Result<List<ScreenshotDto>>> = flow {

        val cached = dao.observeScreenshots(id).firstOrNull()

        if (!cached.isNullOrEmpty()) {
            emit(
                Result.success(
                    cached.map { ScreenshotDto(it.id, it.image) }
                )
            )
        }

        val apiResult = api.getGameScreenshots(id)

        apiResult.fold(
            onSuccess = { response ->

                val entities = response.results.map {
                    ScreenshotEntity(
                        id = it.id,
                        gameId = id,
                        image = it.image
                    )
                }

                dao.clearScreenshots(id)
                dao.insertScreenshots(entities)

                emit(Result.success(response.results))
            },
            onFailure = { e ->
                if (cached.isNullOrEmpty()) {
                    emit(Result.failure(e))
                }
            }
        )
    }
        .flowOn(Dispatchers.IO)

    override fun observeScreenshots(id: Int): Flow<List<ScreenshotEntity>> =
        dao.observeScreenshots(id)


}
