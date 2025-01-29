package com.mercadopago.sdk.android.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mercadopago.sdk.android.R
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.cardnumber.CardNumberTextField
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.cardnumber.CardNumberTextFieldEvent
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.expirationdate.ExpirationCodeDateFormat
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.expirationdate.ExpirationDateFieldEvent
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.expirationdate.ExpirationDateTextField
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.PCIFieldState
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.rememberPCIFieldState
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.securitycode.SecurityCodeFieldEvent
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.securitycode.SecurityCodeTextField
import com.mercadopago.sdk.android.extensions.addBorder
import com.mercadopago.sdk.android.ui.theme.ExampleTheme

@Composable
fun PaymentExampleScreen() {
    PaymentExampleScreenContent()
}

@Composable
fun PaymentExampleScreenContent(
    modifier: Modifier = Modifier
) {
    Scaffold(modifier = modifier.fillMaxSize()) { paddingValues ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column {
                Spacer(Modifier.height(16.dp))
                CardNumberTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                )
                Spacer(Modifier.height(16.dp))
                Row(modifier = Modifier.padding(horizontal = 16.dp)) {
                    ExpirationDateExample(
                        Modifier.weight(1f)
                    )
                    Spacer(Modifier.width(16.dp))
                    SecurityCodeExample(
                        Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

data class SecurityCodeState(
    var isFocused: Boolean = false,
    var filled: Boolean = false,
    var length: Int = 0,
)

data class ExpirationDateState(
    var isFocused: Boolean = false,
    var filled: Boolean = false,
    var length: Int = 0,
    var error: Boolean = false
)

data class CardNumberTextFieldState(
    var isFocused: Boolean = false,
    var filled: Boolean = false,
    var length: Int = 0,
    val isValid: Boolean = false,
    val lastFourDigits: String = "",
    val cardBin: String? = null,
)

@Composable
@Suppress("LongMethod")
fun SecurityCodeExample(modifier: Modifier = Modifier) {
    var secureCodeState by remember { mutableStateOf(SecurityCodeState()) }
    val state: PCIFieldState = rememberPCIFieldState()
    Column(modifier = modifier) {
        Text(
            text = "Security code",
        )
        Spacer(Modifier.height(4.dp))
        CompositionLocalProvider(
            LocalTextSelectionColors provides TextSelectionColors(
                MaterialTheme.colorScheme.primary,
                MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            SecurityCodeTextField(
                state = state,
                onEvent = { securityCodeFieldEvent ->
                    when (securityCodeFieldEvent) {
                        is SecurityCodeFieldEvent.OnFocusChanged -> {
                            secureCodeState = secureCodeState.copy(
                                isFocused = securityCodeFieldEvent.isFocused
                            )
                        }

                        is SecurityCodeFieldEvent.OnLengthChanged -> {
                            secureCodeState = secureCodeState.copy(
                                length = securityCodeFieldEvent.length
                            )
                        }

                        is SecurityCodeFieldEvent.OnInputFilled -> {
                            secureCodeState = secureCodeState.copy(
                                filled = securityCodeFieldEvent.isFilled
                            )
                        }
                    }
                },
                securityCodeSize = 3,
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                decorationBox = { innerTextField ->
                    // Customize the input with relevant information like borders, icons, colors and more
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .addBorder(
                                isFocused = secureCodeState.isFocused,
                            )
                            .height(OutlinedTextFieldDefaults.MinHeight)
                            .padding(horizontal = 16.dp),
                    ) {
                        Box(modifier = Modifier.weight(1f)) {
                            if (secureCodeState.length == 0) {
                                Text(
                                    text = "123",
                                    color = MaterialTheme.colorScheme.outline,
                                    modifier = Modifier.align(Alignment.CenterStart),
                                )
                            }
                            innerTextField()
                        }
                        Spacer(Modifier.width(4.dp))
                        Image(
                            painter = painterResource(R.drawable.ic_security_code),
                            contentDescription = null,
                            modifier = Modifier.size(34.dp),
                        )
                    }
                }
            )
        }
    }
}

@Composable
@Suppress("LongMethod")
fun ExpirationDateExample(modifier: Modifier = Modifier) {
    val state: PCIFieldState = rememberPCIFieldState()
    var expirationDateState by remember { mutableStateOf(ExpirationDateState()) }
    Column(modifier = modifier) {
        Text(
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
                dateFormat = ExpirationCodeDateFormat.LongFormat,
                onEvent = { expirationDateEvent ->
                    when (expirationDateEvent) {
                        is ExpirationDateFieldEvent.OnInputFilled -> {
                            expirationDateState = expirationDateState.copy(
                                filled = expirationDateEvent.isFilled
                            )
                        }

                        is ExpirationDateFieldEvent.IsValid -> {
                            expirationDateState = expirationDateState.copy(
                                error = !expirationDateEvent.isValid
                            )
                        }

                        is ExpirationDateFieldEvent.OnFocusChanged -> {
                            expirationDateState = expirationDateState.copy(
                                isFocused = expirationDateEvent.isFocused
                            )
                        }

                        is ExpirationDateFieldEvent.OnLengthChanged -> {
                            expirationDateState = expirationDateState.copy(
                                length = expirationDateEvent.length
                            )
                        }
                    }
                },
                decorationBox = { innerTextField ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .addBorder(
                                isFocused = expirationDateState.isFocused,
                                isError = expirationDateState.error
                            )
                            .height(OutlinedTextFieldDefaults.MinHeight)
                            .padding(horizontal = 16.dp),
                    ) {
                        Box {
                            if (expirationDateState.length == 0) {
                                Text(
                                    text = "MM/YYYY",
                                    color = MaterialTheme.colorScheme.outline,
                                    modifier = Modifier.align(Alignment.CenterStart),
                                )
                            }
                            innerTextField()
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun CardNumberTextField(
    modifier: Modifier = Modifier,
) {
    val state = rememberPCIFieldState()
    var fieldState by remember { mutableStateOf(CardNumberTextFieldState()) }
    Column(modifier = modifier) {
        Text(
            text = "Card Number",
        )
        Spacer(Modifier.height(4.dp))
        CardNumberTextField(
            state = state,
            onEvent = { event ->
                fieldState = when (event) {
                    is CardNumberTextFieldEvent.OnFocusChanged -> {
                        fieldState.copy(isFocused = event.isFocused)
                    }
                    is CardNumberTextFieldEvent.OnLengthChanged -> {
                        fieldState.copy(length = event.length)
                    }
                    is CardNumberTextFieldEvent.OnLastFourDigitsFilled -> {
                        fieldState.copy(lastFourDigits = event.lastFourDigits)
                    }
                    is CardNumberTextFieldEvent.IsValid -> {
                        fieldState.copy(isValid = event.isValid)
                    }
                    is CardNumberTextFieldEvent.OnBinChanged -> {
                        fieldState.copy(cardBin = event.cardBin)
                    }
                    else -> fieldState
                }
            },
            decorationBox = { innerTextField ->
                // Customize the input with relevant information like borders, icons, colors and more
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .addBorder(
                            isFocused = fieldState.isFocused,
                        )
                        .height(OutlinedTextFieldDefaults.MinHeight)
                        .padding(horizontal = 16.dp),
                ) {
                    Box {
                        if (fieldState.length == 0) {
                            Text(
                                text = "4444 4444 4444 4444",
                                color = MaterialTheme.colorScheme.outline,
                                modifier = Modifier.align(Alignment.CenterStart),
                            )
                        }
                        innerTextField()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview(name = "PaymentScreen Example", showBackground = true)
@Composable
fun PaymentExampleScreenPreview() {
    ExampleTheme {
        PaymentExampleScreen()
    }
}

@Preview(name = "Empty Security Field", showBackground = true)
@Composable
fun SecurityCodeExamplePreview() {
    ExampleTheme {
        SecurityCodeExample()
    }
}

@Preview(name = "Empty Expiration Date Field", showBackground = true)
@Composable
fun ExpirationDatePreview() {
    ExampleTheme {
        ExpirationDateExample()
    }
}

@Preview(name = "Empty Card Number Field", showBackground = true)
@Composable
fun CardNumberPreview() {
    ExampleTheme {
        CardNumberTextField()
    }
}
