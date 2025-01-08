package com.mercadopago.sdk.android.coremethods.ui.components.samples

import androidx.compose.runtime.Composable
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.PCITextField
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.rememberPCIFieldState

@Sampled
@Composable
internal fun PCITextFieldBasicSample() {
    // Create a state for the field
    val state = rememberPCIFieldState()
    PCITextField(
        value = state.input,
        onValueChange = { value ->
            // Update the value of the field change
            state.input = value
        },
        onFocusChanged = {
            // Handle focus change sending events to consumers
        },
    )
}
