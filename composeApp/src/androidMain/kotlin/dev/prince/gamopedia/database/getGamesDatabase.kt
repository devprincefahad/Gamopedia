package dev.prince.gamopedia.database

import android.content.Context
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver

fun getGamesDatabase(context: Context): GamesDatabase {
    val dbFile = context.getDatabasePath("games.db")
    return Room.databaseBuilder<GamesDatabase>(
        context = context.applicationContext,
        name = dbFile.absolutePath
    )
        .setDriver(BundledSQLiteDriver())
        .build()
}
