package com.mercadopago.sdk.android.components.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme

/**
 * Enum representing the available icon color options.
 * Each color type resolves to a specific color from the Mercado Pago theme.
 */
enum class MPIconColor {
    /** Primary text color. */
    Primary,

    /** Secondary text color. */
    Secondary,

    /** Disabled text color. */
    Disabled,

    /** Inverse text color (typically white). */
    Inverse,

    /** Positive feedback color. */
    Positive,

    /** Negative feedback color. */
    Negative,

    /** Caution feedback color. */
    Caution,

    /** Informative feedback color. */
    Informative,
    ;

    /**
     * Resolves this color type to an actual Color from the theme.
     */
    @Composable
    fun toColor(): Color =
        when (this) {
            Primary -> MercadoPagoTheme.color.text.primary
            Secondary -> MercadoPagoTheme.color.text.secondary
            Disabled -> MercadoPagoTheme.color.text.disabled
            Inverse -> MercadoPagoTheme.color.text.inverse
            Positive -> MercadoPagoTheme.color.feedback.positive.iconLoud
            Negative -> MercadoPagoTheme.color.feedback.negative.iconLoud
            Caution -> MercadoPagoTheme.color.feedback.caution.iconLoud
            Informative -> MercadoPagoTheme.color.feedback.informative.iconLoud
        }
}
