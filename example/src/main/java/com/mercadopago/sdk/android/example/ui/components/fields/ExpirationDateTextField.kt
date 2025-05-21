package com.mercadopago.sdk.android.example.ui.components.fields

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.expirationdate.ExpirationDateFormat
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.expirationdate.ExpirationDateTextField
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.expirationdate.ExpirationDateTextFieldEvent
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.PCIFieldState
import com.mercadopago.sdk.android.example.extensions.addBorder
import com.mercadopago.sdk.android.example.presentation.state.ExpirationDateState
import com.mercadopago.sdk.android.example.ui.components.Label
import com.mercadopago.sdk.android.example.ui.components.PlaceHolder

@Composable
internal fun ExpirationDateTextFieldExample(
    modifier: Modifier = Modifier,
    state: PCIFieldState,
    expirationDateState: ExpirationDateState,
    onExpirationDateEvent: (ExpirationDateTextFieldEvent) -> Unit
) {
    Column(modifier = modifier) {
        Label(
            text = "Expiration Date",
        )
        Spacer(Modifier.height(4.dp))
        CompositionLocalProvider(
            LocalTextSelectionColors provides TextSelectionColors(
                MaterialTheme.colorScheme.primary,
                MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            ExpirationDateTextField(
                modifier = Modifier.fillMaxWidth(),
                state = state,
                dateFormat = ExpirationDateFormat.ShortFormat,
                onEvent = onExpirationDateEvent,
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                ),
                decorationBox = { innerTextField ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .addBorder(
                                isFocused = expirationDateState.isFocused,
                                error = expirationDateState.error.first
                            )
                            .height(OutlinedTextFieldDefaults.MinHeight)
                            .padding(horizontal = 16.dp),
                    ) {
                        Box {
                            if (expirationDateState.length == 0) {
                                PlaceHolder(
                                    text = "MM/YY",
                                    modifier = Modifier.align(Alignment.CenterStart),
                                )
                            }
                            innerTextField()
                        }
                    }
                }
            )
        }

        if (expirationDateState.error.first) {
            Label(
                text = expirationDateState.error.second,
                textColor = MaterialTheme.colorScheme.error
            )
        }
    }
}
