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
data class MercadoPagoAndesColor(
    val background: MercadoPagoAndesBackgroundColor,
    val surface: MercadoPagoAndesSurfaceColor,
    val fill: MercadoPagoAndesFillColor,
    val border: MercadoPagoAndesBorderColor,
    val icon: MercadoPagoAndesIconColor,
    val text: MercadoPagoAndesTextColor,
    val brand: MercadoPagoAndesBrandColor,
    val feedback: MercadoPagoAndesFeedbackColor,
    val interactive: MercadoPagoAndesInteractiveColor,
    val transparent: MercadoPagoAndesTransparentColor,
)

/**
 * Represents the background color configuration.
 *
 * @property primary Primary background color
 * @property secondary Secondary background color
 */
data class MercadoPagoAndesBackgroundColor(
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
data class MercadoPagoAndesSurfaceColor(
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
data class MercadoPagoAndesFillColor(
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
data class MercadoPagoAndesBorderColor(
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
data class MercadoPagoAndesIconColor(
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
data class MercadoPagoAndesTextColor(
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
data class MercadoPagoAndesBrandColor(
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
data class MercadoPagoAndesFeedbackColor(
    val informative: MercadoPagoAndesFeedbackTypeColor,
    val positive: MercadoPagoAndesFeedbackTypeColor,
    val caution: MercadoPagoAndesFeedbackTypeColor,
    val negative: MercadoPagoAndesFeedbackTypeColor,
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
data class MercadoPagoAndesFeedbackTypeColor(
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
data class MercadoPagoAndesInteractiveColor(
    val fillLoud: MercadoPagoAndesInteractiveFillColor,
    val fillQuiet: MercadoPagoAndesInteractiveFillColor,
    val fillMute: MercadoPagoAndesInteractiveFillColor,
    val border: MercadoPagoAndesInteractiveBorderColor,
    val icon: MercadoPagoAndesInteractiveIconColor,
)

/**
 * Represents interactive fill color states.
 *
 * @property idle Idle state color
 * @property hover Hover state color
 * @property active Active state color
 */
data class MercadoPagoAndesInteractiveFillColor(
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
data class MercadoPagoAndesInteractiveBorderColor(
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
data class MercadoPagoAndesInteractiveIconColor(
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
data class MercadoPagoAndesTransparentColor(
    val transparent: Color,
)
