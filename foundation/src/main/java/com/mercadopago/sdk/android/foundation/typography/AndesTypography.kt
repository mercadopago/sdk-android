package com.mercadopago.sdk.android.foundation.typography

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.airbnb.android.showkase.annotation.ShowkaseTypography

private const val TYPOGRAPHY_HEADING_GROUP = "Heading"
private const val TYPOGRAPHY_BODY_GROUP = "Body"

/**
 * Represents the new typography system configuration for MercadoPago UI components.
 * This class defines the updated text style values used throughout the application.
 *
 * @property heading Heading typography configuration
 * @property body Body typography configuration
 * @property title Title typography configuration (deprecated, use heading instead)
 */
data class MercadoPagoAndesTypography(
    val heading: AndesHeadingTypography,
    val body: AndesBodyTypography,
    val title: AndesTitleTypography = AndesTitleTypography(
        title = TextStyle.Default,
    ),
)

/**
 * Represents the heading typography configuration.
 *
 * @property default Default heading style configuration
 * @property narrow Narrow heading style configuration
 */
data class AndesHeadingTypography(
    val default: AndesHeadingStyle,
    val narrow: AndesHeadingStyle,
)

/**
 * Represents the heading style configuration with different sizes.
 *
 * @property small Small heading text style
 * @property medium Medium heading text style
 * @property huge Huge heading text style
 */
data class AndesHeadingStyle(
    val small: TextStyle,
    val medium: TextStyle,
    val huge: TextStyle,
)

/**
 * Represents the title typography configuration.
 * @deprecated Use [AndesHeadingTypography] instead. This is kept for backward compatibility.
 *
 * @property title Title text style
 */
@Deprecated("Use AndesHeadingTypography instead", ReplaceWith("AndesHeadingTypography"))
data class AndesTitleTypography(
    val title: TextStyle,
)

/**
 * Represents the body typography configuration.
 *
 * @property default Default body style configuration
 * @property emphasis Emphasis body style configuration
 * @property textlink Textlink body style configuration
 */
data class AndesBodyTypography(
    val default: AndesBodyStyle,
    val emphasis: AndesBodyStyle,
    val textlink: AndesBodyStyle,
)

/**
 * Represents the body style configuration with different sizes.
 *
 * @property small Small body text style
 * @property medium Medium body text style
 * @property large Large body text style
 */
data class AndesBodyStyle(
    val small: TextStyle,
    val medium: TextStyle,
    val large: TextStyle,
)

@ShowkaseTypography(name = "Heading Small Default", group = TYPOGRAPHY_HEADING_GROUP)
internal val AndesHeadingSmallDefault = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.W700,
    fontSize = 16.sp,
    lineHeight = 22.sp,
    letterSpacing = 0.sp,
)

@ShowkaseTypography(name = "Heading Small Narrow", group = TYPOGRAPHY_HEADING_GROUP)
internal val AndesHeadingSmallNarrow = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.W400,
    fontSize = 16.sp,
    lineHeight = 22.sp,
    letterSpacing = (-1).sp,
)

@ShowkaseTypography(name = "Heading Medium Default", group = TYPOGRAPHY_HEADING_GROUP)
internal val AndesHeadingMediumDefault = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.W700,
    fontSize = 20.sp,
    lineHeight = 28.sp,
    letterSpacing = 0.sp,
)

@ShowkaseTypography(name = "Heading Medium Narrow", group = TYPOGRAPHY_HEADING_GROUP)
internal val AndesHeadingMediumNarrow = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.W400,
    fontSize = 20.sp,
    lineHeight = 28.sp,
    letterSpacing = (-1).sp,
)

@ShowkaseTypography(name = "Heading Huge Default", group = TYPOGRAPHY_HEADING_GROUP)
internal val AndesHeadingHugeDefault = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.W700,
    fontSize = 24.sp,
    lineHeight = 28.sp,
    letterSpacing = 0.sp,
)

@ShowkaseTypography(name = "Heading Huge Narrow", group = TYPOGRAPHY_HEADING_GROUP)
internal val AndesHeadingHugeNarrow = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.W400,
    fontSize = 48.sp,
    lineHeight = 56.sp,
    letterSpacing = (-1).sp,
)

@ShowkaseTypography(name = "Body Small Default", group = TYPOGRAPHY_BODY_GROUP)
internal val AndesBodySmallDefault = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.W400,
    fontSize = 14.sp,
    lineHeight = 20.sp,
    letterSpacing = 0.sp,
)

@ShowkaseTypography(name = "Body Small Emphasis", group = TYPOGRAPHY_BODY_GROUP)
internal val AndesBodySmallEmphasis = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.W600,
    fontSize = 14.sp,
    lineHeight = 20.sp,
    letterSpacing = 0.sp,
)

@ShowkaseTypography(name = "Body Small Textlink", group = TYPOGRAPHY_BODY_GROUP)
internal val AndesBodySmallTextlink = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.W600,
    fontSize = 14.sp,
    lineHeight = 20.sp,
    letterSpacing = 0.sp,
)

@ShowkaseTypography(name = "Body Medium Default", group = TYPOGRAPHY_BODY_GROUP)
internal val AndesBodyMediumDefault = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.W400,
    fontSize = 16.sp,
    lineHeight = 24.sp,
    letterSpacing = 0.sp,
)

@ShowkaseTypography(name = "Body Medium Emphasis", group = TYPOGRAPHY_BODY_GROUP)
internal val AndesBodyMediumEmphasis = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.W600,
    fontSize = 16.sp,
    lineHeight = 24.sp,
    letterSpacing = 0.sp,
)

@ShowkaseTypography(name = "Body Medium Textlink", group = TYPOGRAPHY_BODY_GROUP)
internal val AndesBodyMediumTextlink = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.W600,
    fontSize = 16.sp,
    lineHeight = 24.sp,
    letterSpacing = 0.sp,
)

@ShowkaseTypography(name = "Body Large Default", group = TYPOGRAPHY_BODY_GROUP)
internal val AndesBodyLargeDefault = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.W400,
    fontSize = 18.sp,
    lineHeight = 28.sp,
    letterSpacing = 0.sp,
)

@ShowkaseTypography(name = "Body Large Emphasis", group = TYPOGRAPHY_BODY_GROUP)
internal val AndesBodyLargeEmphasis = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.W600,
    fontSize = 18.sp,
    lineHeight = 28.sp,
    letterSpacing = 0.sp,
)

@ShowkaseTypography(name = "Body Large Textlink", group = TYPOGRAPHY_BODY_GROUP)
internal val AndesBodyLargeTextlink = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.W600,
    fontSize = 18.sp,
    lineHeight = 28.sp,
    letterSpacing = 0.sp,
)

internal val AndesTitle = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.W600,
    fontSize = 20.sp,
    lineHeight = 28.sp,
    letterSpacing = 0.sp,
)

internal val AndesDefaultTypography = MercadoPagoAndesTypography(
    heading = AndesHeadingTypography(
        default = AndesHeadingStyle(
            small = AndesHeadingSmallDefault,
            medium = AndesHeadingMediumDefault,
            huge = AndesHeadingHugeDefault,
        ),
        narrow = AndesHeadingStyle(
            small = AndesHeadingSmallNarrow,
            medium = AndesHeadingMediumNarrow,
            huge = AndesHeadingHugeNarrow,
        ),
    ),
    body = AndesBodyTypography(
        default = AndesBodyStyle(
            small = AndesBodySmallDefault,
            medium = AndesBodyMediumDefault,
            large = AndesBodyLargeDefault,
        ),
        emphasis = AndesBodyStyle(
            small = AndesBodySmallEmphasis,
            medium = AndesBodyMediumEmphasis,
            large = AndesBodyLargeEmphasis,
        ),
        textlink = AndesBodyStyle(
            small = AndesBodySmallTextlink,
            medium = AndesBodyMediumTextlink,
            large = AndesBodyLargeTextlink,
        ),
    ),
    title = AndesTitleTypography(
        title = AndesTitle,
    ),
)
