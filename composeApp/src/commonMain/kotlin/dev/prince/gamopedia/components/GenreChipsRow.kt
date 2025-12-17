package dev.prince.gamopedia.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.prince.gamopedia.database.GenreUiModel

@Composable
fun GenreChipsRow(
    genres: List<GenreUiModel>,
    onGenreClick: (GenreUiModel) -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        items(genres, key = { it.id }) { genre ->
            GenreChip(
                text = genre.name,
                onClick = { onGenreClick(genre) }
            )
        }
    }
}