package dev.prince.gamopedia.repo

import dev.prince.gamopedia.database.GameDetailsEntity
import dev.prince.gamopedia.database.GenreUiModel
import dev.prince.gamopedia.database.WishlistEntity
import dev.prince.gamopedia.model.GameDetailsResponse
import dev.prince.gamopedia.model.GameResponse
import dev.prince.gamopedia.model.GamesByGenreResponse
import kotlinx.coroutines.flow.Flow

interface GamesRepository {

    fun getGameDetails(id: Int): Flow<Result<GameDetailsResponse>>

    fun observeGameDetails(id: Int): Flow<GameDetailsEntity?>

    fun searchGames(query: String): Flow<Result<GameResponse>>

    fun observeGames(): Flow<List<dev.prince.gamopedia.model.Result>>

    suspend fun refreshGames()

    fun observeWishlist(): Flow<List<WishlistEntity>>

    fun isWishlisted(id: Int): Flow<Boolean>

    suspend fun addToWishlist(item: WishlistEntity)

    suspend fun removeFromWishlist(item: WishlistEntity)

    fun observeGenres(): Flow<List<GenreUiModel>>

    suspend fun refreshGenres()

    fun getGamesByGenre(genreId: Int): Flow<Result<GamesByGenreResponse>>

}