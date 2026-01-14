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
 * @property headingSmallDefault Heading small default text style
 * @property headingSmallNarrow Heading small narrow text style
 * @property headingMediumDefault Heading medium default text style
 * @property headingMediumNarrow Heading medium narrow text style
 * @property headingHugeDefault Heading huge default text style
 * @property headingHugeNarrow Heading huge narrow text style
 */
data class AndesHeadingTypography(
    val headingSmallDefault: TextStyle,
    val headingSmallNarrow: TextStyle,
    val headingMediumDefault: TextStyle,
    val headingMediumNarrow: TextStyle,
    val headingHugeDefault: TextStyle,
    val headingHugeNarrow: TextStyle,
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
 * @property bodySmallDefault Body small default text style
 * @property bodySmallEmphasis Body small emphasis text style
 * @property bodySmallTextlink Body small textlink text style
 * @property bodyMediumDefault Body medium default text style
 * @property bodyMediumEmphasis Body medium emphasis text style
 * @property bodyMediumTextlink Body medium textlink text style
 * @property bodyLargeDefault Body large default text style
 * @property bodyLargeEmphasis Body large emphasis text style
 * @property bodyLargeTextlink Body large textlink text style
 * @property bodyMediumSemiBold Body medium semibold text style (deprecated, use bodyMediumEmphasis)
 * @property bodyMediumRegular Body medium regular text style (deprecated, use bodyMediumDefault)
 * @property bodySmallSemiBold Body small semibold text style (deprecated, use bodySmallEmphasis)
 * @property bodySmallRegular Body small regular text style (deprecated, use bodySmallDefault)
 * @property bodyExtraSmallSemiBold Body extra small semibold text style (deprecated, use bodySmallEmphasis)
 */
data class AndesBodyTypography(
    val bodySmallDefault: TextStyle,
    val bodySmallEmphasis: TextStyle,
    val bodySmallTextlink: TextStyle,
    val bodyMediumDefault: TextStyle,
    val bodyMediumEmphasis: TextStyle,
    val bodyMediumTextlink: TextStyle,
    val bodyLargeDefault: TextStyle,
    val bodyLargeEmphasis: TextStyle,
    val bodyLargeTextlink: TextStyle,
    @Deprecated("Use bodyMediumEmphasis instead", ReplaceWith("bodyMediumEmphasis"))
    val bodyMediumSemiBold: TextStyle,
    @Deprecated("Use bodyMediumDefault instead", ReplaceWith("bodyMediumDefault"))
    val bodyMediumRegular: TextStyle,
    @Deprecated("Use bodySmallEmphasis instead", ReplaceWith("bodySmallEmphasis"))
    val bodySmallSemiBold: TextStyle,
    @Deprecated("Use bodySmallDefault instead", ReplaceWith("bodySmallDefault"))
    val bodySmallRegular: TextStyle,
    @Deprecated("This style is no longer supported, use bodySmallEmphasis instead")
    val bodyExtraSmallSemiBold: TextStyle,
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
    fontSize = 48.sp,
    lineHeight = 56.sp,
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

@ShowkaseTypography(name = "Body Medium SemiBold", group = TYPOGRAPHY_BODY_GROUP)
internal val AndesBodyMediumSemiBold = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.W600,
    fontSize = 16.sp,
    lineHeight = 24.sp,
    letterSpacing = 0.sp,
)

@ShowkaseTypography(name = "Body Medium Regular", group = TYPOGRAPHY_BODY_GROUP)
internal val AndesBodyMediumRegular = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.W400,
    fontSize = 16.sp,
    lineHeight = 24.sp,
    letterSpacing = 0.sp,
)

@ShowkaseTypography(name = "Body Small SemiBold", group = TYPOGRAPHY_BODY_GROUP)
internal val AndesBodySmallSemiBold = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.W600,
    fontSize = 14.sp,
    lineHeight = 20.sp,
    letterSpacing = 0.sp,
)

@ShowkaseTypography(name = "Body Small Regular", group = TYPOGRAPHY_BODY_GROUP)
internal val AndesBodySmallRegular = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.W400,
    fontSize = 14.sp,
    lineHeight = 20.sp,
    letterSpacing = 0.sp,
)

@ShowkaseTypography(name = "Body Extra Small SemiBold", group = TYPOGRAPHY_BODY_GROUP)
internal val AndesBodyExtraSmallSemiBold = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.W600,
    fontSize = 12.sp,
    lineHeight = 16.sp,
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
        headingSmallDefault = AndesHeadingSmallDefault,
        headingSmallNarrow = AndesHeadingSmallNarrow,
        headingMediumDefault = AndesHeadingMediumDefault,
        headingMediumNarrow = AndesHeadingMediumNarrow,
        headingHugeDefault = AndesHeadingHugeDefault,
        headingHugeNarrow = AndesHeadingHugeNarrow,
    ),
    body = AndesBodyTypography(
        bodySmallDefault = AndesBodySmallDefault,
        bodySmallEmphasis = AndesBodySmallEmphasis,
        bodySmallTextlink = AndesBodySmallTextlink,
        bodyMediumDefault = AndesBodyMediumDefault,
        bodyMediumEmphasis = AndesBodyMediumEmphasis,
        bodyMediumTextlink = AndesBodyMediumTextlink,
        bodyLargeDefault = AndesBodyLargeDefault,
        bodyLargeEmphasis = AndesBodyLargeEmphasis,
        bodyLargeTextlink = AndesBodyLargeTextlink,
        bodyMediumSemiBold = AndesBodyMediumSemiBold,
        bodyMediumRegular = AndesBodyMediumRegular,
        bodySmallSemiBold = AndesBodySmallSemiBold,
        bodySmallRegular = AndesBodySmallRegular,
        bodyExtraSmallSemiBold = AndesBodyExtraSmallSemiBold,
    ),
    title = AndesTitleTypography(
        title = AndesTitle,
    ),
)
