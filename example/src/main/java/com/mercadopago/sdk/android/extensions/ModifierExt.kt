package com.mercadopago.sdk.android.extensions

import androidx.compose.foundation.border
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Modifier.addBorder(
    isFocused: Boolean,
    isError: Boolean = false,
): Modifier {
    return border(
        width = if (isFocused) 2.dp else 1.dp,
        color = if (isError) {
            MaterialTheme.colorScheme.error
        } else if (isFocused) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.outline
        },
        shape = MaterialTheme.shapes.small,
    )
}
