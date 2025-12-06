package dev.prince.gamopedia.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [GameEntity::class, WishlistEntity::class, GameDetailsEntity::class],
    version = 1,
    exportSchema = false
)
abstract class GamesDatabase : RoomDatabase() {
    abstract fun gamesDao(): GamesDao
}
