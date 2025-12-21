package dev.prince.gamopedia.util

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color


val backgroundGradient = Brush.verticalGradient(
    colorStops = arrayOf(
        0.0f to Color(0xFF230F49),
        0.85f to Color(0xFF000000),
        //1.0f to Color(0x66A5FF4A)
    )
)

val glowishColor = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFCFFF4A),
            Color(0xFF9EEA3A)
        )
    )

val GlowYellow = Color(0xFFA5FF4A)