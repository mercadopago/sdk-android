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
data class NewMercadoPagoColor(
    val background: NewBackgroundColor,
    val surface: NewSurfaceColor,
    val fill: NewFillColor,
    val border: NewBorderColor,
    val icon: NewIconColor,
    val text: NewTextColor,
    val brand: NewBrandColor,
    val feedback: NewFeedbackColor,
    val interactive: NewInteractiveColor,
    val transparent: NewTransparentColor,
)

/**
 * Represents the background color configuration.
 *
 * @property primary Primary background color
 * @property secondary Secondary background color
 */
data class NewBackgroundColor(
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
data class NewSurfaceColor(
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
data class NewFillColor(
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
data class NewBorderColor(
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
data class NewIconColor(
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
data class NewTextColor(
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
data class NewBrandColor(
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
data class NewFeedbackColor(
    val informative: NewFeedbackTypeColor,
    val positive: NewFeedbackTypeColor,
    val caution: NewFeedbackTypeColor,
    val negative: NewFeedbackTypeColor,
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
data class NewFeedbackTypeColor(
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
data class NewInteractiveColor(
    val fillLoud: NewInteractiveFillColor,
    val fillQuiet: NewInteractiveFillColor,
    val fillMute: NewInteractiveFillColor,
    val border: NewInteractiveBorderColor,
    val icon: NewInteractiveIconColor,
)

/**
 * Represents interactive fill color states.
 *
 * @property idle Idle state color
 * @property hover Hover state color
 * @property active Active state color
 */
data class NewInteractiveFillColor(
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
data class NewInteractiveBorderColor(
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
data class NewInteractiveIconColor(
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
data class NewTransparentColor(
    val transparent: Color,
)
