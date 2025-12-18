package dev.prince.gamopedia.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import dev.prince.gamopedia.model.Result

@Composable
fun GameItem(
    game: Result,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val genreNames = game.genres.joinToString(", ") { it.name }

    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF2A2A2A)
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box {

            Row(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                AsyncImage(
                    model = game.backgroundImage,
                    contentDescription = game.name,
                    modifier = Modifier
                        .size(96.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 56.dp)
                ) {

                    Text(
                        text = game.name,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        fontSize = 16.sp,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    if (genreNames.isNotBlank()) {
                        Text(
                            text = genreNames,
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 12.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            game.rating?.let {
                RatingChip(
                    rating = it,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 10.dp, end = 10.dp)
                )
            }
        }
    }
}


@Composable
fun RatingChip(
    rating: Double,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFCFFF4A),
                        Color(0xFF9EEA3A)
                    )
                )
            )
            .padding(horizontal = 14.dp, vertical = 2.dp)
    ) {
        Text(
            text = "${(rating * 10).toInt() / 10f}",
            color = Color.Black,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}
