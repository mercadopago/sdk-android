package com.mercadopago.sdk.android.foundation.outline

import androidx.compose.ui.unit.Dp

/**
 * Represents the new border width system configuration for MercadoPago UI components.
 * This class defines the updated border width values used throughout the application.
 *
 * @property none No border (0)
 * @property small Small border (1)
 * @property medium Medium border (2)
 * @property large Large border (3)
 * @property xlarge Extra large border (4)
 */
data class NewBorderWidth(
    val none: Dp,
    val small: Dp,
    val medium: Dp,
    val large: Dp,
    val xlarge: Dp,
)
