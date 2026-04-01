package com.mercadopago.sdk.android.foundation.shape

import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp

/**
 * Represents the new border radius system configuration for MercadoPago UI components.
 * This class defines the updated corner radius values used throughout the application.
 *
 * @property none No radius (0)
 * @property tiny Tiny radius (4)
 * @property xsmall Extra small radius (6)
 * @property small Small radius (8)
 * @property medium Medium radius (12)
 * @property large Large radius (16)
 * @property xlarge Extra large radius (20)
 * @property full Full radius (9999)
 */
data class MercadoPagoRadius(
    val none: Dp,
    val tiny: Dp,
    val xsmall: Dp,
    val small: Dp,
    val medium: Dp,
    val large: Dp,
    val xlarge: Dp,
    val full: Dp,
)

/**
 * Represents the new shape system configuration for MercadoPago UI components.
 * This class provides pre-built shapes based on the radius configuration.
 *
 * @property none No corner shape
 * @property tiny Tiny corner shape
 * @property xsmall Extra small corner shape
 * @property small Small corner shape
 * @property medium Medium corner shape
 * @property large Large corner shape
 * @property xlarge Extra large corner shape
 * @property full Fully rounded shape (circular)
 */
data class MercadoPagoShape(
    val none: Shape,
    val tiny: Shape,
    val xsmall: Shape,
    val small: Shape,
    val medium: Shape,
    val large: Shape,
    val xlarge: Shape,
    val full: Shape,
)
