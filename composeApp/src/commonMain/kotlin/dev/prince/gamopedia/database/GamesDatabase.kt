package dev.prince.gamopedia.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor

@Database(
    entities = [GameEntity::class, WishlistEntity::class, GameDetailsEntity::class, GenreEntity::class, ScreenshotEntity::class],
    version = 1,
    exportSchema = false
)
@ConstructedBy(GamesDatabaseConstructor::class)
abstract class GamesDatabase : RoomDatabase() {
    abstract fun gamesDao(): GamesDao
}
// The Room compiler generates the `actual` implementations.
@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object GamesDatabaseConstructor :
    RoomDatabaseConstructor<GamesDatabase> {
    override fun initialize(): GamesDatabase
}