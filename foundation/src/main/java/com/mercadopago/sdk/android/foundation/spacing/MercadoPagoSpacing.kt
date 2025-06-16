package com.mercadopago.sdk.android.foundation.spacing

import androidx.compose.ui.unit.Dp

/**
 * Represents the spacing system configuration for MercadoPago UI components.
 * This class defines the standard spacing values used throughout the application
 * to maintain consistent layout and visual hierarchy.
 *
 * @property xxs Extra extra small spacing
 * @property xs Extra small spacing
 * @property s Small spacing
 * @property m Medium spacing
 * @property l Large spacing
 * @property xl Extra large spacing
 * @property xxl Extra extra large spacing
 */
data class MercadoPagoSpacing(
    val xxs: Dp,
    val xs: Dp,
    val s: Dp,
    val m: Dp,
    val l: Dp,
    val xl: Dp,
    val xxl: Dp
)
