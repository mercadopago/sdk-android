package com.mercadopago.sdk.android.foundation.theme.default

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mercadopago.sdk.android.foundation.color.BackgroundColor
import com.mercadopago.sdk.android.foundation.color.FeedbackColor
import com.mercadopago.sdk.android.foundation.color.MercadoPagoColor
import com.mercadopago.sdk.android.foundation.color.OutlineColor
import com.mercadopago.sdk.android.foundation.color.TextColor
import com.mercadopago.sdk.android.foundation.outline.MercadoPagoOutline
import com.mercadopago.sdk.android.foundation.shape.MercadoPagoRadius
import com.mercadopago.sdk.android.foundation.shape.MercadoPagoShape
import com.mercadopago.sdk.android.foundation.spacing.MercadoPagoSpacing
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoThemeProvider

private val DefaultLightRadius = MercadoPagoRadius(
    xxs = 4.dp,
    xs = 6.dp,
    s = 16.dp,
)

private val DefaultLightColors = MercadoPagoColor(
    accent = Color(0xFF3483FA),
    accentFirstVariant = Color(0xFF2968C8),
    accentSecondVariant = Color(0xFF1F4E96),
    accentYellow = Color(0xFFFFE600),
    accentPositive = Color(0xFF00A650),
    accentNegative = Color(0xFFF23D4F),
    background = BackgroundColor(
        primary = Color(0xFFFFFFFF),
        secondary = Color(0xFFF5F5F5),
        tertiary = Color(0xFFEDEDED),
        inverted = Color(0xFF1A1A1A),
    ),
    text = TextColor(
        primary = Color(0xFF1A1A1A),
        secondary = Color(0xFF737373),
        accent = Color(0xFF3483FA),
        disabled = Color(0xFFBFBFBF),
        negative = Color(0xFFF23D4F),
        inverted = Color(0xFFFFFFFF),
    ),
    secondary = Color(0xFFE3EDFB),
    secondaryFirstVariant = Color(0xFFD9E7FA),
    secondarySecondVariant = Color(0xFFC6DCF7),
    outline = OutlineColor(
        primary = Color(0xFFBFBFBF),
        secondary = Color(0xFFE5E5E5),
    ),
    feedback = FeedbackColor(
        positive = Color(0xFF00A650),
        negative = Color(0xFFF23D4F),
        positiveSecondary = Color(0xFFDCEDE4),
    ),
)

private val DefaultLightSpacing = MercadoPagoSpacing(
    xxs = 4.dp,
    xs = 8.dp,
    s = 12.dp,
    m = 16.dp,
    l = 20.dp,
    xl = 24.dp,
    xxl = 32.dp,
)

private val DefaultLightShape = MercadoPagoShape(
    xxs = RoundedCornerShape(DefaultLightRadius.xxs),
    xs = RoundedCornerShape(DefaultLightRadius.xs),
    s = RoundedCornerShape(DefaultLightRadius.s),
)

internal val MercadoPagoDefaultLightTheme = MercadoPagoThemeProvider(
    color = DefaultLightColors,
    spacing = DefaultLightSpacing,
    radius = DefaultLightRadius,
    shape = DefaultLightShape,
    outline = MercadoPagoOutline(
        xxs = 1.dp,
        xs = 2.dp,
    ),
)
