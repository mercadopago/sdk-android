package com.mercadopago.sdk.android.foundation.typography

import androidx.compose.ui.text.TextStyle

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
