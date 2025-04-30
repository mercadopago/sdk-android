package com.mercadopago.sdk.android.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF5AE4DB),
    onPrimary = Color(0xFF171E2E),
    onSurfaceVariant = Color(0xFFB5BBC8),
    background = Color(0XFF171E2E),
    secondary = Color(0xFFD0BCFF),
    tertiary = Color(0xFFD0BCFF),
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF3483FA),
    onPrimary = Color(0xFFFFFFFF),
    background = Color(0xFFFFFFFF),
    onBackground = Color(0xFF1A1A1A),
    onSurfaceVariant = Color(0xFF737373),
    secondary = Color(0xFFD0BCFF),
    tertiary = Color(0xFFD0BCFF),
    outline = Color(0xFFBFBFBF),
)

private val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
)

@Composable
internal fun ExampleTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
