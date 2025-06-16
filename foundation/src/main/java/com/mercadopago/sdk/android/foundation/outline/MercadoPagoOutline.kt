package com.mercadopago.sdk.android.foundation.outline

import androidx.compose.ui.unit.Dp

/**
 * Represents the outline system configuration for MercadoPago UI components.
 * This class defines the standard outline width values used throughout the application
 * to maintain consistent visual appearance.
 *
 * @property xxs Extra extra small outline width
 * @property xs Extra small outline width
 */
data class MercadoPagoOutline(
    val xxs: Dp,
    val xs: Dp,
)
