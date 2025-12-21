package dev.prince.gamopedia.repo

import dev.prince.gamopedia.database.GameDetailsEntity
import dev.prince.gamopedia.database.GameEntity
import dev.prince.gamopedia.database.ScreenshotEntity
import dev.prince.gamopedia.database.WishlistEntity
import dev.prince.gamopedia.model.GameDetailsResponse
import dev.prince.gamopedia.model.GameResponse
import dev.prince.gamopedia.model.GamesByGenreResponse
import dev.prince.gamopedia.model.Genre
import dev.prince.gamopedia.model.ScreenshotDto
import kotlinx.coroutines.flow.Flow

interface GamesRepository {

//    fun getGameDetails(id: Int): Flow<Result<GameDetailsResponse>>

    fun observeGameDetails(id: Int): Flow<GameDetailsEntity?>

    suspend fun refreshGameDetails(id: Int): Result<Boolean>

    fun searchGames(query: String): Flow<Result<GameResponse>>

    fun observeGames(): Flow<List<dev.prince.gamopedia.model.Result>>

    suspend fun refreshGames()

    fun observeWishlist(): Flow<List<WishlistEntity>>

    fun isWishlisted(id: Int): Flow<Boolean>

    suspend fun addToWishlist(game: GameEntity)

    suspend fun removeFromWishlist(id: Int)

    fun observeGenres(): Flow<List<Genre>>

    suspend fun refreshGenres()

    fun getGamesByGenre(genreId: Int): Flow<Result<GamesByGenreResponse>>

    fun getGameScreenshots(id: Int): Flow<Result<List<ScreenshotDto>>>

    fun observeScreenshots(id: Int): Flow<List<ScreenshotEntity>>

}