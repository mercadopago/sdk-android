package com.mercadopago.sdk.android.example.ui.components.fields

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.cardnumber.CardNumberTextField
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.cardnumber.CardNumberTextFieldEvent
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.PCIFieldState
import com.mercadopago.sdk.android.example.extensions.addBorder
import com.mercadopago.sdk.android.example.presentation.state.CardNumberTextFieldState
import com.mercadopago.sdk.android.example.ui.components.Label
import com.mercadopago.sdk.android.example.ui.components.PlaceHolder

/**
 * This is a example of implementation of the card number secure field
 * The component ensures PCI compliance by handling sensitive card data securely.
 */
@Composable
internal fun CardNumberTextFieldExample(
    modifier: Modifier = Modifier,
    state: PCIFieldState,
    cardNumberState: CardNumberTextFieldState,
    onCardNumberEvent: (CardNumberTextFieldEvent) -> Unit
) {
    Column(modifier = modifier) {
        Label(
            text = "Card Number",
        )
        Spacer(Modifier.height(4.dp))
        CardNumberTextField(
            state = state,
            onEvent = onCardNumberEvent,
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onBackground,
            ),
            decorationBox = { innerTextField ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        // Adding a border by field state created for this example
                        .addBorder(
                            isFocused = cardNumberState.isFocused,
                            error = cardNumberState.error.first
                        )
                        .height(OutlinedTextFieldDefaults.MinHeight)
                        .padding(horizontal = 16.dp),
                ) {
                    // Displaying the issuer image to the user when the bin is changed
                    if (cardNumberState.image != null) {
                        val context = LocalContext.current
                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                .data(cardNumberState.image)
                                .build(),
                            contentDescription = null,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                    }
                    Box {
                        if (cardNumberState.length == 0) {
                            PlaceHolder(
                                text = "4444 4444 4444 4444",
                                modifier = Modifier.align(Alignment.CenterStart),
                            )
                        }
                        // Never forget to call innerTextField again inside decorationBox
                        innerTextField()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
        )

        if (cardNumberState.error.first) {
            Label(
                text = cardNumberState.error.second,
                textColor = MaterialTheme.colorScheme.error
            )
        }
    }
}
