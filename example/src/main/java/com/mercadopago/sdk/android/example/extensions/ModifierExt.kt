package com.mercadopago.sdk.android.example.extensions

import androidx.compose.foundation.border
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun Modifier.addBorder(
    isFocused: Boolean,
    error: Boolean = false,
): Modifier {
    return border(
        width = if (isFocused) 2.dp else 1.dp,
        color = if (error) {
            MaterialTheme.colorScheme.error
        } else if (isFocused) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.outline
        },
        shape = MaterialTheme.shapes.small,
    )
}
