package com.mercadopago.sdk.android.coremethods.ui.components.samples

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.PCIFieldState
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.rememberPCIFieldState
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.securitycode.SecurityCode

@Sampled
@Composable
internal fun SecurityCodeFieldBasicSample() {
    // A basic use of this Component
    // Create a PCIFieldState to use in input
    val state: PCIFieldState = rememberPCIFieldState()
    SecurityCode(
        state = state,
        onEvent = { securityCodeFieldEvent ->
            // Handle the security code events
        },
        securityCodeSize = 3, // Pass the securityCode length number
    )
}

@Sampled
@Composable
internal fun SecurityCodeFieldDecorationBoxSample() {
    // Adding a decoration in the component
    // Create a PCIFieldState to use in input
    val state: PCIFieldState = rememberPCIFieldState()
    SecurityCode(
        state = state,
        onEvent = { securityCodeFieldEvent ->
            // Handle the security code events
        },
        securityCodeSize = 3, // Pass the securityCode length number,
        decorationBox = { innerTextField ->
            // Adding the inner text inside a box with a border
            Box(
                modifier = Modifier.border(
                    width = 2.dp,
                    color = Color.Black,
                    shape = RoundedCornerShape(10.dp)
                )
            ) {
                // Don`t forget to call the innerTextField()
                innerTextField()
            }
        }
    )
}
