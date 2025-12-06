package dev.prince.gamopedia.repo

import dev.prince.gamopedia.database.WishlistEntity
import dev.prince.gamopedia.model.GameDetailsResponse
import dev.prince.gamopedia.model.GameResponse
import kotlinx.coroutines.flow.Flow

interface GamesRepository {

    fun getGames(): Flow<Result<GameResponse>>

    fun getGameDetails(id: Int): Flow<Result<GameDetailsResponse>>

    fun searchGames(query: String): Flow<Result<GameResponse>>

    fun observeGames(): Flow<List<dev.prince.gamopedia.model.Result>>

    suspend fun refreshGames()

    fun observeWishlist(): Flow<List<WishlistEntity>>

    fun isWishlisted(id: Int): Flow<Boolean>

    suspend fun addToWishlist(item: WishlistEntity)

    suspend fun removeFromWishlist(item: WishlistEntity)

}