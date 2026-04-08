package com.mercadopago.sdk.android.foundation.typography

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.airbnb.android.showkase.annotation.ShowkaseTypography
import com.mercadopago.sdk.android.foundation.R

internal val InterFontFamily = FontFamily(
    Font(R.font.inter_regular, FontWeight.W400),
    Font(R.font.inter_semi_bold, FontWeight.W600),
    Font(R.font.inter_bold, FontWeight.W700),
)

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
data class MercadoPagoTypography(
    val heading: HeadingTypography,
    val body: BodyTypography,
    val title: TitleTypography = TitleTypography(
        title = TextStyle.Default,
    ),
)

/**
 * Represents the heading typography configuration.
 *
 * @property default Default heading style configuration
 * @property narrow Narrow heading style configuration
 */
data class HeadingTypography(
    val default: HeadingStyle,
    val narrow: HeadingStyle,
)

/**
 * Represents the heading style configuration with different sizes.
 *
 * @property small Small heading text style
 * @property medium Medium heading text style
 * @property huge Huge heading text style
 */
data class HeadingStyle(
    val small: TextStyle,
    val medium: TextStyle,
    val huge: TextStyle,
)

/**
 * Represents the title typography configuration.
 * @deprecated Use [HeadingTypography] instead. This is kept for backward compatibility.
 *
 * @property title Title text style
 */
@Deprecated("Use HeadingTypography instead", ReplaceWith("HeadingTypography"))
data class TitleTypography(
    val title: TextStyle,
)

/**
 * Represents the body typography configuration.
 *
 * @property default Default body style configuration
 * @property emphasis Emphasis body style configuration
 * @property textlink Textlink body style configuration
 */
data class BodyTypography(
    val default: BodyStyle,
    val emphasis: BodyStyle,
    val textlink: BodyStyle,
)

/**
 * Represents the body style configuration with different sizes.
 *
 * @property small Small body text style
 * @property medium Medium body text style
 * @property large Large body text style
 */
data class BodyStyle(
    val small: TextStyle,
    val medium: TextStyle,
    val large: TextStyle,
)

@ShowkaseTypography(name = "Heading Small Default", group = TYPOGRAPHY_HEADING_GROUP)
internal val HeadingSmallDefault = TextStyle(
    fontFamily = InterFontFamily,
    fontWeight = FontWeight.W700,
    fontSize = 16.sp,
    lineHeight = 22.sp,
    letterSpacing = 0.sp,
)

@ShowkaseTypography(name = "Heading Small Narrow", group = TYPOGRAPHY_HEADING_GROUP)
internal val HeadingSmallNarrow = TextStyle(
    fontFamily = InterFontFamily,
    fontWeight = FontWeight.W400,
    fontSize = 16.sp,
    lineHeight = 22.sp,
    letterSpacing = (-1).sp,
)

@ShowkaseTypography(name = "Heading Medium Default", group = TYPOGRAPHY_HEADING_GROUP)
internal val HeadingMediumDefault = TextStyle(
    fontFamily = InterFontFamily,
    fontWeight = FontWeight.W700,
    fontSize = 20.sp,
    lineHeight = 28.sp,
    letterSpacing = 0.sp,
)

@ShowkaseTypography(name = "Heading Medium Narrow", group = TYPOGRAPHY_HEADING_GROUP)
internal val HeadingMediumNarrow = TextStyle(
    fontFamily = InterFontFamily,
    fontWeight = FontWeight.W400,
    fontSize = 20.sp,
    lineHeight = 28.sp,
    letterSpacing = (-1).sp,
)

@ShowkaseTypography(name = "Heading Huge Default", group = TYPOGRAPHY_HEADING_GROUP)
internal val HeadingHugeDefault = TextStyle(
    fontFamily = InterFontFamily,
    fontWeight = FontWeight.W700,
    fontSize = 24.sp,
    lineHeight = 28.sp,
    letterSpacing = 0.sp,
)

@ShowkaseTypography(name = "Heading Huge Narrow", group = TYPOGRAPHY_HEADING_GROUP)
internal val HeadingHugeNarrow = TextStyle(
    fontFamily = InterFontFamily,
    fontWeight = FontWeight.W400,
    fontSize = 48.sp,
    lineHeight = 56.sp,
    letterSpacing = (-1).sp,
)

@ShowkaseTypography(name = "Body Small Default", group = TYPOGRAPHY_BODY_GROUP)
internal val BodySmallDefault = TextStyle(
    fontFamily = InterFontFamily,
    fontWeight = FontWeight.W400,
    fontSize = 12.sp,
    lineHeight = 16.sp,
    letterSpacing = 0.sp,
)

@ShowkaseTypography(name = "Body Small Emphasis", group = TYPOGRAPHY_BODY_GROUP)
internal val BodySmallEmphasis = TextStyle(
    fontFamily = InterFontFamily,
    fontWeight = FontWeight.W600,
    fontSize = 12.sp,
    lineHeight = 16.sp,
    letterSpacing = 0.sp,
)

@ShowkaseTypography(name = "Body Small Textlink", group = TYPOGRAPHY_BODY_GROUP)
internal val BodySmallTextlink = TextStyle(
    fontFamily = InterFontFamily,
    fontWeight = FontWeight.W600,
    fontSize = 12.sp,
    lineHeight = 16.sp,
    letterSpacing = 0.sp,
)

@ShowkaseTypography(name = "Body Medium Default", group = TYPOGRAPHY_BODY_GROUP)
internal val BodyMediumDefault = TextStyle(
    fontFamily = InterFontFamily,
    fontWeight = FontWeight.W400,
    fontSize = 14.sp,
    lineHeight = 20.sp,
    letterSpacing = 0.sp,
)

@ShowkaseTypography(name = "Body Medium Emphasis", group = TYPOGRAPHY_BODY_GROUP)
internal val BodyMediumEmphasis = TextStyle(
    fontFamily = InterFontFamily,
    fontWeight = FontWeight.W600,
    fontSize = 14.sp,
    lineHeight = 20.sp,
    letterSpacing = 0.sp,
)

@ShowkaseTypography(name = "Body Medium Textlink", group = TYPOGRAPHY_BODY_GROUP)
internal val BodyMediumTextlink = TextStyle(
    fontFamily = InterFontFamily,
    fontWeight = FontWeight.W600,
    fontSize = 14.sp,
    lineHeight = 20.sp,
    letterSpacing = 0.sp,
)

@ShowkaseTypography(name = "Body Large Default", group = TYPOGRAPHY_BODY_GROUP)
internal val BodyLargeDefault = TextStyle(
    fontFamily = InterFontFamily,
    fontWeight = FontWeight.W400,
    fontSize = 16.sp,
    lineHeight = 24.sp,
    letterSpacing = 0.sp,
)

@ShowkaseTypography(name = "Body Large Emphasis", group = TYPOGRAPHY_BODY_GROUP)
internal val BodyLargeEmphasis = TextStyle(
    fontFamily = InterFontFamily,
    fontWeight = FontWeight.W600,
    fontSize = 16.sp,
    lineHeight = 24.sp,
    letterSpacing = 0.sp,
)

@ShowkaseTypography(name = "Body Large Textlink", group = TYPOGRAPHY_BODY_GROUP)
internal val BodyLargeTextlink = TextStyle(
    fontFamily = InterFontFamily,
    fontWeight = FontWeight.W600,
    fontSize = 16.sp,
    lineHeight = 24.sp,
    letterSpacing = 0.sp,
)

internal val Title = TextStyle(
    fontFamily = InterFontFamily,
    fontWeight = FontWeight.W600,
    fontSize = 20.sp,
    lineHeight = 28.sp,
    letterSpacing = 0.sp,
)

internal val DefaultTypography = MercadoPagoTypography(
    heading = HeadingTypography(
        default = HeadingStyle(
            small = HeadingSmallDefault,
            medium = HeadingMediumDefault,
            huge = HeadingHugeDefault,
        ),
        narrow = HeadingStyle(
            small = HeadingSmallNarrow,
            medium = HeadingMediumNarrow,
            huge = HeadingHugeNarrow,
        ),
    ),
    body = BodyTypography(
        default = BodyStyle(
            small = BodySmallDefault,
            medium = BodyMediumDefault,
            large = BodyLargeDefault,
        ),
        emphasis = BodyStyle(
            small = BodySmallEmphasis,
            medium = BodyMediumEmphasis,
            large = BodyLargeEmphasis,
        ),
        textlink = BodyStyle(
            small = BodySmallTextlink,
            medium = BodyMediumTextlink,
            large = BodyLargeTextlink,
        ),
    ),
    title = TitleTypography(
        title = Title,
    ),
)
