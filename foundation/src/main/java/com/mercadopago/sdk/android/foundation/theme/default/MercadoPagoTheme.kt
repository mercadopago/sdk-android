package com.mercadopago.sdk.android.foundation.theme.default

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.airbnb.android.showkase.annotation.ShowkaseColor
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
import com.mercadopago.sdk.android.foundation.typography.MercadoPagoProximaNovaTypography

private const val COLOR_GROUP_ACCENT = "Accent"
private const val COLOR_GROUP_BACKGROUND = "Background"
private const val COLOR_GROUP_TEXT = "Text"
private const val COLOR_GROUP_SECONDARY = "Secondary"
private const val COLOR_GROUP_OUTLINE = "Outline"
private const val COLOR_GROUP_FEEDBACK = "Feedback"

// Accent Colors
@ShowkaseColor(name = "Accent Light", group = COLOR_GROUP_ACCENT)
internal val DefaultLightAccentColor = Color(0xFF3483FA)

@ShowkaseColor(name = "Accent First Variant Light", group = COLOR_GROUP_ACCENT)
internal val DefaultLightAccentFirstVariantColor = Color(0xFF2968C8)

@ShowkaseColor(name = "Accent Second Variant Light", group = COLOR_GROUP_ACCENT)
internal val DefaultLightAccentSecondVariantColor = Color(0xFF1F4E96)

@ShowkaseColor(name = "Accent Yellow Light", group = COLOR_GROUP_ACCENT)
internal val DefaultLightAccentYellowColor = Color(0xFFFFE600)

@ShowkaseColor(name = "Accent Positive Light", group = COLOR_GROUP_ACCENT)
internal val DefaultLightAccentPositiveColor = Color(0xFF00A650)

@ShowkaseColor(name = "Accent Negative Light", group = COLOR_GROUP_ACCENT)
internal val DefaultLightAccentNegativeColor = Color(0xFFF23D4F)

// Background Colors
@ShowkaseColor(name = "Background Primary Light", group = COLOR_GROUP_BACKGROUND)
internal val DefaultLightBackgroundPrimaryColor = Color(0xFFFFFFFF)

@ShowkaseColor(name = "Background Secondary Light", group = COLOR_GROUP_BACKGROUND)
internal val DefaultLightBackgroundSecondaryColor = Color(0xFFF5F5F5)

@ShowkaseColor(name = "Background Tertiary Light", group = COLOR_GROUP_BACKGROUND)
internal val DefaultLightBackgroundTertiaryColor = Color(0xFFEDEDED)

@ShowkaseColor(name = "Background Inverted Light", group = COLOR_GROUP_BACKGROUND)
internal val DefaultLightBackgroundInvertedColor = Color(0xFF1A1A1A)

// Text Colors
@ShowkaseColor(name = "Text Primary Light", group = COLOR_GROUP_TEXT)
internal val DefaultLightTextPrimaryColor = Color(0xFF1A1A1A)

@ShowkaseColor(name = "Text Secondary Light", group = COLOR_GROUP_TEXT)
internal val DefaultLightTextSecondaryColor = Color(0xFF737373)

@ShowkaseColor(name = "Text Accent Light", group = COLOR_GROUP_TEXT)
internal val DefaultLightTextAccentColor = Color(0xFF3483FA)

@ShowkaseColor(name = "Text Disabled Light", group = COLOR_GROUP_TEXT)
internal val DefaultLightTextDisabledColor = Color(0xFFBFBFBF)

@ShowkaseColor(name = "Text Negative Light", group = COLOR_GROUP_TEXT)
internal val DefaultLightTextNegativeColor = Color(0xFFF23D4F)

@ShowkaseColor(name = "Text Inverted Light", group = COLOR_GROUP_TEXT)
internal val DefaultLightTextInvertedColor = Color(0xFFFFFFFF)

// Secondary Colors
@ShowkaseColor(name = "Secondary Light", group = COLOR_GROUP_SECONDARY)
internal val DefaultLightSecondaryColor = Color(0xFFE3EDFB)

@ShowkaseColor(name = "Secondary First Variant Light", group = COLOR_GROUP_SECONDARY)
internal val DefaultLightSecondaryFirstVariantColor = Color(0xFFD9E7FA)

@ShowkaseColor(name = "Secondary Second Variant Light", group = COLOR_GROUP_SECONDARY)
internal val DefaultLightSecondarySecondVariantColor = Color(0xFFC6DCF7)

// Outline Colors
@ShowkaseColor(name = "Outline Primary Light", group = COLOR_GROUP_OUTLINE)
internal val DefaultLightOutlinePrimaryColor = Color(0xFFBFBFBF)

@ShowkaseColor(name = "Outline Secondary Light", group = COLOR_GROUP_OUTLINE)
internal val DefaultLightOutlineSecondaryColor = Color(0xFFE5E5E5)

// Feedback Colors
@ShowkaseColor(name = "Feedback Positive Light", group = COLOR_GROUP_FEEDBACK)
internal val DefaultLightFeedbackPositiveColor = Color(0xFF00A650)

@ShowkaseColor(name = "Feedback Negative Light", group = COLOR_GROUP_FEEDBACK)
internal val DefaultLightFeedbackNegativeColor = Color(0xFFF23D4F)

@ShowkaseColor(name = "Feedback Positive Secondary Light", group = COLOR_GROUP_FEEDBACK)
internal val DefaultLightFeedbackPositiveSecondaryColor = Color(0xFFDCEDE4)

private val DefaultLightRadius = MercadoPagoRadius(
    xxs = 4.dp,
    xs = 6.dp,
    s = 16.dp,
)

private val DefaultLightColors = MercadoPagoColor(
    accent = DefaultLightAccentColor,
    accentFirstVariant = DefaultLightAccentFirstVariantColor,
    accentSecondVariant = DefaultLightAccentSecondVariantColor,
    accentYellow = DefaultLightAccentYellowColor,
    accentPositive = DefaultLightAccentPositiveColor,
    accentNegative = DefaultLightAccentNegativeColor,
    background = BackgroundColor(
        primary = DefaultLightBackgroundPrimaryColor,
        secondary = DefaultLightBackgroundSecondaryColor,
        tertiary = DefaultLightBackgroundTertiaryColor,
        inverted = DefaultLightBackgroundInvertedColor,
    ),
    text = TextColor(
        primary = DefaultLightTextPrimaryColor,
        secondary = DefaultLightTextSecondaryColor,
        accent = DefaultLightTextAccentColor,
        disabled = DefaultLightTextDisabledColor,
        negative = DefaultLightTextNegativeColor,
        inverted = DefaultLightTextInvertedColor,
    ),
    secondary = DefaultLightSecondaryColor,
    secondaryFirstVariant = DefaultLightSecondaryFirstVariantColor,
    secondarySecondVariant = DefaultLightSecondarySecondVariantColor,
    outline = OutlineColor(
        primary = DefaultLightOutlinePrimaryColor,
        secondary = DefaultLightOutlineSecondaryColor,
    ),
    feedback = FeedbackColor(
        positive = DefaultLightFeedbackPositiveColor,
        negative = DefaultLightFeedbackNegativeColor,
        positiveSecondary = DefaultLightFeedbackPositiveSecondaryColor,
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

internal val MercadoPagoDefaultLightTheme = MercadoPagoThemeProvider.Legacy(
    color = DefaultLightColors,
    spacing = DefaultLightSpacing,
    radius = DefaultLightRadius,
    shape = DefaultLightShape,
    outline = MercadoPagoOutline(
        xxs = 1.dp,
        xs = 2.dp,
    ),
    typography = MercadoPagoProximaNovaTypography,
)
