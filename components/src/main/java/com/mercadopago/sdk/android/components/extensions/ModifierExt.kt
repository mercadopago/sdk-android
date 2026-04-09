package com.mercadopago.sdk.android.components.extensions

import androidx.compose.foundation.border
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mercadopago.sdk.android.components.inputs.MPInputDefaults

@Composable
internal fun Modifier.addBorder(
    isFocused: Boolean,
    error: Boolean = false,
    enabled: Boolean = true,
    defaults: MPInputDefaults,
): Modifier {
    return border(
        width = if (isFocused) {
            defaults.border.widthFocused
        } else {
            defaults.border.widthIdle
        },
        color = if (!enabled) {
            defaults.colors.borderDisabled
        } else if (error) {
            defaults.colors.borderError
        } else if (isFocused) {
            defaults.colors.borderActive
        } else {
            defaults.colors.borderIdle
        },
        shape = defaults.border.shape,
    )
}
