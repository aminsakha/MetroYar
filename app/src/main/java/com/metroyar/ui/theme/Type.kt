package com.metroyar.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.metroyar.R

// Set of Material typography styles to start with
private val myCustomFontFamily = FontFamily(
    Font(R.font.irsans)
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
//val Typography = Typography(
//    bodyLarge = TextStyle(
//        fontFamily = FontFamily.Default,
//        fontWeight = FontWeight.Normal,
//        fontSize = 16.sp,
//        lineHeight = 24.sp,
//        letterSpacing = 0.5.sp
//    )
/* Other default text styles to override
titleLarge = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Normal,
    fontSize = 22.sp,
    lineHeight = 28.sp,
    letterSpacing = 0.sp
),
labelSmall = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Medium,
    fontSize = 11.sp,
    lineHeight = 16.sp,
    letterSpacing = 0.5.sp
)
*/