package com.mercadopago.sdk.android.components.extensions

import androidx.compose.foundation.border
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoAndesTheme

@Composable
internal fun Modifier.addBorder(
    isFocused: Boolean,
    error: Boolean = false,
): Modifier {
    return border(
        width = if (isFocused) {
            MercadoPagoAndesTheme.borderWidth.medium
        } else {
            MercadoPagoAndesTheme.borderWidth.small
        },
        color = if (error) {
            MercadoPagoAndesTheme.color.feedback.negative.borderLoud
        } else if (isFocused) {
            MercadoPagoAndesTheme.color.interactive.border.active
        } else {
            MercadoPagoAndesTheme.color.interactive.border.idle
        },
        shape = MercadoPagoAndesTheme.shape.xsmall,
    )
}
