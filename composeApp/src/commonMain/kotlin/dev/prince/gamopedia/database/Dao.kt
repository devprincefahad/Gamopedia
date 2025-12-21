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

    @Query("SELECT * FROM wishlist_table")
    fun getWishlist(): Flow<List<WishlistEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToWishlist(game: WishlistEntity)

    @Query("DELETE FROM wishlist_table WHERE id = :id")
    suspend fun removeFromWishlist(id: Int)

    @Query("SELECT EXISTS(SELECT 1 FROM wishlist_table WHERE id = :id)")
    fun isWishlisted(id: Int): Flow<Boolean>

    @Query("SELECT * FROM game_details WHERE id = :id")
    fun getGameDetails(id: Int): Flow<GameDetailsEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGameDetails(details: GameDetailsEntity)

    @Query("SELECT * FROM genres")
    fun observeGenres(): Flow<List<GenreEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGenres(genres: List<GenreEntity>)

    @Query("DELETE FROM genres")
    suspend fun clearGenres()

    @Query("SELECT * FROM game_screenshots WHERE gameId = :gameId")
    fun observeScreenshots(gameId: Int): Flow<List<ScreenshotEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScreenshots(screenshots: List<ScreenshotEntity>)

    @Query("DELETE FROM game_screenshots WHERE gameId = :gameId")
    suspend fun clearScreenshots(gameId: Int)
}
