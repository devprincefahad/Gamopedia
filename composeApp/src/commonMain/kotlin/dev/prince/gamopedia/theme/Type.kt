package dev.prince.gamopedia.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import gamopedia.composeapp.generated.resources.Res
import gamopedia.composeapp.generated.resources.google_sans_bold
import gamopedia.composeapp.generated.resources.google_sans_medium
import gamopedia.composeapp.generated.resources.google_sans_regular
import gamopedia.composeapp.generated.resources.google_sans_semi_bold
import org.jetbrains.compose.resources.Font

@Composable
fun getAppFontFamily() = FontFamily(
        Font(resource = Res.font.google_sans_regular, weight = FontWeight.Normal),
        Font(resource = Res.font.google_sans_bold, weight = FontWeight.Bold),
        Font(resource = Res.font.google_sans_semi_bold, weight = FontWeight.SemiBold),
        Font(resource = Res.font.google_sans_medium, weight = FontWeight.Medium)
    )


@Composable
fun googleSansTypography() = Typography().run {
    val fontFamily = getAppFontFamily()
    copy(
        displayLarge = displayLarge.copy(fontFamily = fontFamily),
        displayMedium = displayMedium.copy(fontFamily = fontFamily),
        displaySmall = displaySmall.copy(fontFamily = fontFamily),
        headlineLarge = headlineLarge.copy(fontFamily = fontFamily),
        headlineMedium = headlineMedium.copy(fontFamily = fontFamily),
        headlineSmall = headlineSmall.copy(fontFamily = fontFamily),
        titleLarge = titleLarge.copy(fontFamily = fontFamily),
        titleMedium = titleMedium.copy(fontFamily = fontFamily),
        titleSmall = titleSmall.copy(fontFamily = fontFamily),
        bodyLarge = bodyLarge.copy(fontFamily =  fontFamily),
        bodyMedium = bodyMedium.copy(fontFamily = fontFamily),
        bodySmall = bodySmall.copy(fontFamily = fontFamily),
        labelLarge = labelLarge.copy(fontFamily = fontFamily),
        labelMedium = labelMedium.copy(fontFamily = fontFamily),
        labelSmall = labelSmall.copy(fontFamily = fontFamily)
    )
}