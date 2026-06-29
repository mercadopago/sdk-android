package com.mercadopago.sdk.android.foundation.color

import androidx.compose.ui.graphics.Color

/**
 * Represents the new color system configuration for MercadoPago UI components.
 * This class defines the updated color tokens used throughout the application.
 *
 * @property background Background color configuration
 * @property surface Surface color configuration
 * @property fill Fill color configuration
 * @property border Border color configuration
 * @property icon Icon color configuration
 * @property text Text color configuration
 * @property brand Brand color configuration
 * @property feedback Feedback color configuration
 * @property interactive Interactive color configuration
 * @property transparent Transparent color configuration
 */
data class MercadoPagoColor(
    val background: MercadoPagoBackgroundColor,
    val surface: MercadoPagoSurfaceColor,
    val fill: MercadoPagoFillColor,
    val border: MercadoPagoBorderColor,
    val icon: MercadoPagoIconColor,
    val text: MercadoPagoTextColor,
    val brand: MercadoPagoBrandColor,
    val feedback: MercadoPagoFeedbackColor,
    val interactive: MercadoPagoInteractiveColor,
    val transparent: MercadoPagoTransparentColor,
)

/**
 * Represents the background color configuration.
 *
 * @property primary Primary background color
 * @property secondary Secondary background color
 */
data class MercadoPagoBackgroundColor(
    val primary: Color,
    val secondary: Color,
)

/**
 * Represents the surface color configuration.
 *
 * @property primaryIdle Primary surface color in idle state
 * @property primaryActive Primary surface color in active state
 * @property primaryDisabled Primary surface color in disabled state
 */
data class MercadoPagoSurfaceColor(
    val primaryIdle: Color,
    val primaryActive: Color,
    val primaryDisabled: Color,
)

/**
 * Represents the fill color configuration.
 *
 * @property primary Primary fill color
 * @property secondary Secondary fill color
 * @property inverse Inverse fill color
 * @property disabled Disabled fill color
 * @property accentLoud Loud accent fill color
 * @property accentQuiet Quiet accent fill color
 * @property defaultOnScroll Default fill color on scroll
 */
data class MercadoPagoFillColor(
    val primary: Color,
    val secondary: Color,
    val inverse: Color,
    val disabled: Color,
    val accentLoud: Color,
    val accentQuiet: Color,
    val defaultOnScroll: Color,
)

/**
 * Represents the border color configuration.
 *
 * @property primary Primary border color
 * @property accent Accent border color
 * @property inverse Inverse border color
 * @property disabled Disabled border color
 */
data class MercadoPagoBorderColor(
    val primary: Color,
    val accent: Color,
    val inverse: Color,
    val disabled: Color,
)

/**
 * Represents the icon color configuration.
 *
 * @property primary Primary icon color
 * @property secondary Secondary icon color
 * @property accent Accent icon color
 * @property inverse Inverse icon color
 * @property disabled Disabled icon color
 */
data class MercadoPagoIconColor(
    val primary: Color,
    val secondary: Color,
    val accent: Color,
    val inverse: Color,
    val disabled: Color,
)

/**
 * Represents the text color configuration.
 *
 * @property primary Primary text color
 * @property secondary Secondary text color
 * @property accent Accent text color
 * @property inverse Inverse text color
 * @property disabled Disabled text color
 * @property linkIdle Link text color in idle state
 * @property linkActive Link text color in active state
 */
data class MercadoPagoTextColor(
    val primary: Color,
    val secondary: Color,
    val accent: Color,
    val inverse: Color,
    val disabled: Color,
    val linkIdle: Color,
    val linkActive: Color,
)

/**
 * Represents the brand color configuration.
 *
 * @property fillLoud Loud brand fill color
 * @property fillQuiet Quiet brand fill color
 * @property gradientStart Brand gradient start color
 * @property gradientEnd Brand gradient end color
 */
data class MercadoPagoBrandColor(
    val fillLoud: Color,
    val fillQuiet: Color,
    val gradientStart: Color,
    val gradientEnd: Color,
)

/**
 * Represents the feedback color configuration.
 *
 * @property informative Informative feedback colors
 * @property positive Positive feedback colors
 * @property caution Caution feedback colors
 * @property negative Negative feedback colors
 */
data class MercadoPagoFeedbackColor(
    val informative: MercadoPagoFeedbackTypeColor,
    val positive: MercadoPagoFeedbackTypeColor,
    val caution: MercadoPagoFeedbackTypeColor,
    val negative: MercadoPagoFeedbackTypeColor,
)

/**
 * Represents a single feedback type color configuration.
 *
 * @property fillLoud Loud fill color
 * @property fillQuiet Quiet fill color
 * @property textLoud Loud text color
 * @property borderLoud Loud border color
 * @property iconLoud Loud icon color
 */
data class MercadoPagoFeedbackTypeColor(
    val fillLoud: Color,
    val fillQuiet: Color,
    val textLoud: Color,
    val borderLoud: Color,
    val iconLoud: Color,
)

/**
 * Represents the interactive color configuration.
 *
 * @property fillLoud Loud interactive fill colors
 * @property fillQuiet Quiet interactive fill colors
 * @property fillMute Mute interactive fill colors
 * @property border Interactive border colors
 * @property icon Interactive icon colors
 */
data class MercadoPagoInteractiveColor(
    val fillLoud: MercadoPagoInteractiveFillColor,
    val fillQuiet: MercadoPagoInteractiveFillColor,
    val fillMute: MercadoPagoInteractiveFillColor,
    val border: MercadoPagoInteractiveBorderColor,
    val icon: MercadoPagoInteractiveIconColor,
)

/**
 * Represents interactive fill color states.
 *
 * @property idle Idle state color
 * @property hover Hover state color
 * @property active Active state color
 */
data class MercadoPagoInteractiveFillColor(
    val idle: Color,
    val hover: Color,
    val active: Color,
)

/**
 * Represents interactive border color states.
 *
 * @property idle Idle state color
 * @property active Active state color
 */
data class MercadoPagoInteractiveBorderColor(
    val idle: Color,
    val active: Color,
)

/**
 * Represents interactive icon color states.
 *
 * @property idle Idle state color
 * @property active Active state color
 * @property idleAccent Idle accent state color
 * @property activeAccent Active accent state color
 */
data class MercadoPagoInteractiveIconColor(
    val idle: Color,
    val active: Color,
    val idleAccent: Color,
    val activeAccent: Color,
)

/**
 * Represents transparent color configuration.
 *
 * @property transparent Fully transparent color
 */
data class MercadoPagoTransparentColor(
    val transparent: Color,
)
