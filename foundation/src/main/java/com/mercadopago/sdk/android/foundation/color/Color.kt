package com.mercadopago.sdk.android.foundation.color

import androidx.compose.ui.graphics.Color

/**
 * Represents the color system configuration for MercadoPago UI components.
 * This class defines the standard color values used throughout the application
 * to maintain consistent visual appearance.
 *
 * @property accent Primary accent color
 * @property secondary Secondary color
 * @property accentFirstVariant First variant of accent color
 * @property accentSecondVariant Second variant of accent color
 * @property accentYellow Yellow accent color
 * @property accentPositive Positive accent color
 * @property accentNegative Negative accent color
 * @property secondaryFirstVariant First variant of secondary color
 * @property secondarySecondVariant Second variant of secondary color
 * @property text Text color configuration
 * @property background Background color configuration
 * @property outline Outline color configuration
 * @property feedback Feedback color configuration
 */
data class MercadoPagoColor(
    val accent: Color,
    val secondary: Color,
    val accentFirstVariant: Color,
    val accentSecondVariant: Color,
    val accentYellow: Color,
    val accentPositive: Color,
    val accentNegative: Color,
    val secondaryFirstVariant: Color,
    val secondarySecondVariant: Color,
    val text: TextColor,
    val background: BackgroundColor,
    val outline: OutlineColor,
    val feedback: FeedbackColor,
)

/**
 * Represents the background color configuration for MercadoPago UI components.
 * This class defines the standard background color values used throughout the application.
 *
 * @property primary Primary background color
 * @property secondary Secondary background color
 * @property tertiary Tertiary background color
 * @property inverted Inverted background color
 */
data class BackgroundColor(
    val primary: Color,
    val secondary: Color,
    val tertiary: Color,
    val inverted: Color,
)

/**
 * Represents the text color configuration for MercadoPago UI components.
 * This class defines the standard text color values used throughout the application.
 *
 * @property primary Primary text color
 * @property secondary Secondary text color
 * @property accent Accent text color
 * @property disabled Disabled text color
 * @property negative Negative text color
 * @property inverted Inverted text color
 */
data class TextColor(
    val primary: Color,
    val secondary: Color,
    val accent: Color,
    val disabled: Color,
    val negative: Color,
    val inverted: Color,
)

/**
 * Represents the outline color configuration for MercadoPago UI components.
 * This class defines the standard outline color values used throughout the application.
 *
 * @property primary Primary outline color
 * @property secondary Secondary outline color
 */
data class OutlineColor(
    val primary: Color,
    val secondary: Color,
)

/**
 * Represents the feedback color configuration for MercadoPago UI components.
 * This class defines the standard feedback color values used throughout the application.
 *
 * @property positive Positive feedback color
 * @property negative Negative feedback color
 * @property positiveSecondary Secondary positive feedback color
 */
data class FeedbackColor(
    val positive: Color,
    val negative: Color,
    val positiveSecondary: Color,
)
