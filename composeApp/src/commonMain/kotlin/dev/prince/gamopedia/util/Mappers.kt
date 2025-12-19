package dev.prince.gamopedia.util

import dev.prince.gamopedia.database.GameEntity
import dev.prince.gamopedia.database.GenreEntity
import dev.prince.gamopedia.model.Genre
import dev.prince.gamopedia.model.Result

fun GameEntity.toResultModel(): Result =
    Result(
        id = this.id,
        name = this.name,
        backgroundImage = this.backgroundImage ?: "",
        released = this.released,
        rating = this.rating,
        genres = genre
            ?.let { listOf(Genre(id = 0, name = it)) }
            ?: emptyList()
    )

fun GenreEntity.toGenreModel() = Genre(
    id = id,
    name = name,
    backgroundImage = imageBackground
)
