package dev.prince.gamopedia.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "games")
data class GameEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val backgroundImage: String?
)

@Entity(tableName = "wishlist")
data class WishlistEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val backgroundImage: String?
)

@Entity(tableName = "game_details")
data class GameDetailsEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val description: String,
    val backgroundImage: String?
)

@Entity(tableName = "genres")
data class GenreEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val imageBackground: String?
)

fun GenreEntity.toUiModel() = GenreUiModel(
    id = id,
    name = name,
    imageBackground = imageBackground
)

data class GenreUiModel(
    val id: Int,
    val name: String,
    val imageBackground: String?
)