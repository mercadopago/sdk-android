package com.mercadopago.sdk.android.foundation.typography

import androidx.compose.ui.text.TextStyle

/**
 * Represents the new typography system configuration for MercadoPago UI components.
 * This class defines the updated text style values used throughout the application.
 *
 * @property title Title typography configuration
 * @property body Body typography configuration
 */
data class MercadoPagoAndesTypography(
    val title: AndesTitleTypography,
    val body: AndesBodyTypography,
)

/**
 * Represents the title typography configuration.
 *
 * @property title Title text style
 */
data class AndesTitleTypography(
    val title: TextStyle,
)

/**
 * Represents the body typography configuration.
 *
 * @property bodyMediumSemiBold Body medium semibold text style
 * @property bodyMediumRegular Body medium regular text style
 * @property bodySmallSemiBold Body small semibold text style
 * @property bodySmallRegular Body small regular text style
 * @property bodyExtraSmallSemiBold Body extra small semibold text style
 */
data class AndesBodyTypography(
    val bodyMediumSemiBold: TextStyle,
    val bodyMediumRegular: TextStyle,
    val bodySmallSemiBold: TextStyle,
    val bodySmallRegular: TextStyle,
    val bodyExtraSmallSemiBold: TextStyle,
)
