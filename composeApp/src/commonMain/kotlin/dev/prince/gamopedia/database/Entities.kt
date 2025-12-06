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