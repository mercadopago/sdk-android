package com.mercadopago.sdk.android.coremethods.ui.components.samples

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.cardnumber.CardNumberTextField
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.PCIFieldState
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.rememberPCIFieldState

@Sampled
@Composable
internal fun CardNumberWithIssuerIconSample() {
    val state: PCIFieldState = rememberPCIFieldState().apply {
        input = "1234567890123456123"
    }
    val iconUrl = ""
    CardNumberTextField(
        state = state,
        onEvent = { _ ->
            // call viewmodel passing the events to handle the calls
        },
        decorationBox = { innerTextField ->
            // Adding the inner text inside a box with a border
            Box(
                modifier = Modifier.border(
                    width = 2.dp,
                    color = Color.Blue,
                    shape = RoundedCornerShape(10.dp),
                )
            ) {
                Row {
                    if (iconUrl.isNotEmpty()) {
                        // Display the issuer icon when you have an icon
                    }

                    // Don`t forget to call the innerTextField()
                    innerTextField()
                }
            }
        }
    )
}
