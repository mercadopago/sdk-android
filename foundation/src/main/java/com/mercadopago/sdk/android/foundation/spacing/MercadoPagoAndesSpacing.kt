package com.mercadopago.sdk.android.foundation.spacing

import androidx.compose.ui.unit.Dp

/**
 * Represents the new spacing system configuration for MercadoPago UI components.
 * This class defines the updated spacing values used throughout the application.
 *
 * @property paddings Padding spacing configuration
 * @property gap Gap spacing configuration
 */
data class MercadoPagoAndesSpacing(
    val paddings: AndesSpacingPaddings,
    val gap: AndesSpacingGap,
)

/**
 * Represents the padding spacing configuration.
 *
 * @property none No padding (0)
 * @property pico Pico padding (2)
 * @property xnano Extra nano padding (4)
 * @property nano Nano padding (6)
 * @property xmicro Extra micro padding (8)
 * @property micro Micro padding (12)
 * @property xtiny Extra tiny padding (16)
 * @property tiny Tiny padding (20)
 * @property xsmall Extra small padding (24)
 * @property small Small padding (32)
 * @property medium Medium padding (40)
 * @property large Large padding (48)
 * @property xlarge Extra large padding (56)
 * @property huge Huge padding (64)
 * @property xhuge Extra huge padding (72)
 * @property mega Mega padding (80)
 * @property xmega Extra mega padding (84)
 */
data class AndesSpacingPaddings(
    val none: Dp,
    val pico: Dp,
    val xnano: Dp,
    val nano: Dp,
    val xmicro: Dp,
    val micro: Dp,
    val xtiny: Dp,
    val tiny: Dp,
    val xsmall: Dp,
    val small: Dp,
    val medium: Dp,
    val large: Dp,
    val xlarge: Dp,
    val huge: Dp,
    val xhuge: Dp,
    val mega: Dp,
    val xmega: Dp,
)

/**
 * Represents the gap spacing configuration.
 *
 * @property none No gap (0)
 * @property pico Pico gap (2)
 * @property xnano Extra nano gap (4)
 * @property nano Nano gap (6)
 * @property xmicro Extra micro gap (8)
 * @property micro Micro gap (12)
 * @property xtiny Extra tiny gap (16)
 * @property tiny Tiny gap (20)
 * @property xsmall Extra small gap (24)
 * @property small Small gap (32)
 * @property medium Medium gap (40)
 * @property large Large gap (48)
 * @property xlarge Extra large gap (56)
 * @property huge Huge gap (64)
 * @property xhuge Extra huge gap (72)
 * @property mega Mega gap (80)
 * @property xmega Extra mega gap (84)
 */
data class AndesSpacingGap(
    val none: Dp,
    val pico: Dp,
    val xnano: Dp,
    val nano: Dp,
    val xmicro: Dp,
    val micro: Dp,
    val xtiny: Dp,
    val tiny: Dp,
    val xsmall: Dp,
    val small: Dp,
    val medium: Dp,
    val large: Dp,
    val xlarge: Dp,
    val huge: Dp,
    val xhuge: Dp,
    val mega: Dp,
    val xmega: Dp,
)
