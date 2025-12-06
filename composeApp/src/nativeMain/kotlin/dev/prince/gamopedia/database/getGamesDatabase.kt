package dev.prince.gamopedia.database

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import platform.Foundation.NSHomeDirectory

fun getGamesDatabase(): GamesDatabase {
    val dbFile = NSHomeDirectory() + "/games.db"
    return Room.databaseBuilder<GamesDatabase>(
        name = dbFile,
        factory = { GamesDatabase::class.instantiateImpl() }
    )
        .setDriver(BundledSQLiteDriver())
        .build()
}