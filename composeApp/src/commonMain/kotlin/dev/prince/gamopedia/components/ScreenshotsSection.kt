package dev.prince.gamopedia.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import dev.prince.gamopedia.model.ScreenshotDto

@Composable
fun ScreenshotsSection(result: Result<List<ScreenshotDto>>?) {

    val screenshots = result?.getOrNull() ?: return

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(screenshots) { screenshot ->
            AsyncImage(
                model = screenshot.image,
                contentDescription = null,
                modifier = Modifier
                    .width(320.dp)
                    .height(190.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )
        }
    }
}
