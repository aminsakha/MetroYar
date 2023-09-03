package com.metroyar.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.metroyar.R

private val myCustomFontFamily = FontFamily(
    Font(R.font.iran_mobile)
)

val Typography = Typography(
    displayLarge = TextStyle(fontFamily = myCustomFontFamily),
    displayMedium = TextStyle(fontFamily = myCustomFontFamily),
    displaySmall = TextStyle(fontFamily = myCustomFontFamily),
    headlineLarge = TextStyle(fontFamily = myCustomFontFamily),
    headlineMedium = TextStyle(fontFamily = myCustomFontFamily),
    headlineSmall = TextStyle(fontFamily = myCustomFontFamily),
    titleLarge = TextStyle(fontFamily = myCustomFontFamily),
    titleMedium = TextStyle(fontFamily = myCustomFontFamily),
    titleSmall = TextStyle(fontFamily = myCustomFontFamily),
    bodyLarge = TextStyle(fontFamily = myCustomFontFamily),
    bodyMedium = TextStyle(fontFamily = myCustomFontFamily),
    bodySmall = TextStyle(fontFamily = myCustomFontFamily),
    labelLarge = TextStyle(fontFamily = myCustomFontFamily),
    labelMedium = TextStyle(fontFamily = myCustomFontFamily),
    labelSmall = TextStyle(fontFamily = myCustomFontFamily)
)