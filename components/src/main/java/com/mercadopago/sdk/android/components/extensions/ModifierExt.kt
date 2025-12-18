package com.mercadopago.sdk.android.components.extensions

import androidx.compose.foundation.border
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme

@Composable
internal fun Modifier.addBorder(
    isFocused: Boolean,
    error: Boolean = false,
): Modifier {
    return border(
        width = if (isFocused) 2.dp else 1.dp,
        color = if (error) {
            MercadoPagoTheme.color.accentNegative
        } else if (isFocused) {
            MercadoPagoTheme.color.secondary
        } else {
            MercadoPagoTheme.color.secondarySecondVariant
        },
        shape = MercadoPagoTheme.shape.xs,
    )
}
