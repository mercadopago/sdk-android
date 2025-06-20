package com.mercadopago.sdk.android.foundation.shape

import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp

/**
 * Represents the shape system configuration for MercadoPago UI components.
 * This class defines the standard shape values used throughout the application
 * to maintain consistent visual appearance.
 *
 * @property xxs Extra extra small shape
 * @property xs Extra small shape
 * @property s Small shape
 */
data class MercadoPagoShape(
    val xxs: Shape,
    val xs: Shape,
    val s: Shape,
)

/**
 * Represents the radius system configuration for MercadoPago UI components.
 * This class defines the standard corner radius values used throughout the application
 * to maintain consistent visual appearance.
 *
 * @property xxs Extra extra small radius
 * @property xs Extra small radius
 * @property s Small radius
 */
data class MercadoPagoRadius(
    val xxs: Dp,
    val xs: Dp,
    val s: Dp,
)
