package dev.prince.gamopedia.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "games")
data class GameEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val backgroundImage: String?,
    val released: String?,
    val rating: Double?,
    val genre: String?
)

@Entity(tableName = "wishlist_table")
data class WishlistEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val backgroundImage: String?,
    val released: String?,
    val rating: Double?,
    val genre: String?
)

@Entity(tableName = "game_details")
data class GameDetailsEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val description: String,
    val backgroundImage: String?,
    val website: String,
    val rating: Double?,
    val platforms: String?
)

@Entity(tableName = "genres")
data class GenreEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val imageBackground: String?
)

@Entity(tableName = "game_screenshots")
data class ScreenshotEntity(
    @PrimaryKey val id: Int,
    val gameId: Int,
    val image: String
)