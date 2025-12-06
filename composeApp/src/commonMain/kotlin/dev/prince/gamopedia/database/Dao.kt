package dev.prince.gamopedia.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface GamesDao {

    @Query("SELECT * FROM games")
    fun getCachedGames(): Flow<List<GameEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGames(games: List<GameEntity>)

    @Query("DELETE FROM games")
    suspend fun clearGames()

    @Query("SELECT * FROM wishlist")
    fun getWishlist(): Flow<List<WishlistEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToWishlist(item: WishlistEntity)

    @Delete
    suspend fun removeFromWishlist(item: WishlistEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM wishlist WHERE id = :id)")
    fun isWishlisted(id: Int): Flow<Boolean>

    @Query("SELECT * FROM game_details WHERE id = :id")
    fun getGameDetails(id: Int): Flow<GameDetailsEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGameDetails(details: GameDetailsEntity)
}
