package com.mercadopago.sdk.android.components.extensions

import androidx.compose.foundation.border
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme

@Composable
internal fun Modifier.addBorder(
    isFocused: Boolean,
    error: Boolean = false,
): Modifier {
    return border(
        width = if (isFocused) {
            MercadoPagoTheme.newBorderWidth.medium
        } else {
            MercadoPagoTheme.newBorderWidth.small
        },
        color = if (error) {
            MercadoPagoTheme.newColor.feedback.negative.borderLoud
        } else if (isFocused) {
            MercadoPagoTheme.newColor.interactive.border.active
        } else {
            MercadoPagoTheme.newColor.interactive.border.idle
        },
        shape = MercadoPagoTheme.newShape.xsmall,
    )
}
