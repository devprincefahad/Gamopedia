package dev.prince.gamopedia.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PlatformChips(
    platforms: List<String>,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState),
        horizontalArrangement =
            Arrangement.spacedBy(8.dp)
    ) {
        platforms.forEach { platform ->
            PlatformChip(name = platform)
        }
    }
}

@Composable
fun PlatformChip(
    name: String
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(
                Color(0xFF1E1E1E)
            )
            .border(
                width = 1.dp,
                color = Color(0x99A5FF4A),
                shape = RoundedCornerShape(50)
            )
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = name,
            color = Color(0xFFA5FF4A),
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}
