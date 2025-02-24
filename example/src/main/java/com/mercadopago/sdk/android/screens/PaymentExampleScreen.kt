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
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.mercadopago.sdk.android.presentation.PaymentScreenViewModel
import com.mercadopago.sdk.android.presentation.data.Installment
import com.mercadopago.sdk.android.presentation.state.CardNumberTextFieldState
import com.mercadopago.sdk.android.presentation.state.ExpirationDateState
import com.mercadopago.sdk.android.presentation.state.PaymentScreenViewState
import com.mercadopago.sdk.android.presentation.state.SecurityCodeState
import com.mercadopago.sdk.android.ui.components.InstallmentListDropDownField
import com.mercadopago.sdk.android.ui.theme.ExampleTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun PaymentExampleScreen(
    viewModel: PaymentScreenViewModel = koinViewModel(),
) {
    val viewState by viewModel.viewState.collectAsState()
    val cardNumberState = rememberPCIFieldState()
    val expirationDateState = rememberPCIFieldState()
    val securityCodeState = rememberPCIFieldState()

    PaymentExampleScreenContent(
        viewState = viewState,
        cardNumberState = cardNumberState,
        expirationDateState = expirationDateState,
        securityCodeState = securityCodeState,
        onExpirationDateEvent = viewModel::onExpirationDateEvent,
        onSecurityCodeEvent = viewModel::onSecurityCodeEvent,
        onCardNumberEvent = viewModel::onCardNumberEvent,
        onSelectedInstallment = viewModel::onInstallmentSelected
    )
}

@Composable
@Suppress("LongParameterList", "LongMethod")
fun PaymentExampleScreenContent(
    modifier: Modifier = Modifier,
    viewState: PaymentScreenViewState,
    cardNumberState: PCIFieldState,
    expirationDateState: PCIFieldState,
    securityCodeState: PCIFieldState,
    onExpirationDateEvent: (ExpirationDateFieldEvent) -> Unit,
    onSecurityCodeEvent: (SecurityCodeFieldEvent) -> Unit,
    onCardNumberEvent: (CardNumberTextFieldEvent) -> Unit,
    onSelectedInstallment: (Installment) -> Unit,
) {
    Scaffold(modifier = modifier.fillMaxSize()) { paddingValues ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column {
                Spacer(Modifier.height(16.dp))
                CardNumberTextFieldExample(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    state = cardNumberState,
                    cardNumberState = viewState.cardNumberState,
                    onCardNumberEvent = onCardNumberEvent
                )
                Spacer(Modifier.height(16.dp))
                Row(modifier = Modifier.padding(horizontal = 16.dp)) {
                    ExpirationDateExample(
                        Modifier.weight(1f),
                        state = expirationDateState,
                        expirationDateState = viewState.expirationDateState,
                        onExpirationDateEvent = onExpirationDateEvent
                    )
                    Spacer(Modifier.width(16.dp))
                    SecurityCodeExample(
                        Modifier.weight(1f),
                        state = securityCodeState,
                        securityCodeState = viewState.secureCodeState,
                        onSecurityCodeEvent = onSecurityCodeEvent
                    )
                }
                InstallmentListDropDownField(
                    state = viewState.installmentsState,
                    onSelectedInstallment = onSelectedInstallment
                )
                Spacer(Modifier.size(16.dp))
                Button(
                    shape = MaterialTheme.shapes.small,
                    onClick = {
                        PaymentScreenViewModel().generateToken(
                            cardNumberState = cardNumberState,
                            expirationDateState = expirationDateState,
                            securityCodeState = securityCodeState
                        )
                    },
                    modifier = Modifier
                        .align(Alignment.End)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                ) {
                    Text(
                        text = "Pay",
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
            }
        }
    }
}

@Composable
fun SecurityCodeExample(
    modifier: Modifier = Modifier,
    state: PCIFieldState,
    securityCodeState: SecurityCodeState,
    onSecurityCodeEvent: (SecurityCodeFieldEvent) -> Unit
) {
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
                onEvent = onSecurityCodeEvent,
                securityCodeSize = 3,
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                decorationBox = { innerTextField ->
                    // Customize the input with relevant information like borders, icons, colors and more
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .addBorder(
                                isFocused = securityCodeState.isFocused,
                            )
                            .height(OutlinedTextFieldDefaults.MinHeight)
                            .padding(horizontal = 16.dp),
                    ) {
                        Box(modifier = Modifier.weight(1f)) {
                            if (securityCodeState.length == 0) {
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
fun ExpirationDateExample(
    modifier: Modifier = Modifier,
    state: PCIFieldState,
    expirationDateState: ExpirationDateState,
    onExpirationDateEvent: (ExpirationDateFieldEvent) -> Unit
) {
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
                onEvent = onExpirationDateEvent,
                decorationBox = { innerTextField ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .addBorder(
                                isFocused = expirationDateState.isFocused,
                                isError = expirationDateState.valid
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
fun CardNumberTextFieldExample(
    modifier: Modifier = Modifier,
    state: PCIFieldState,
    cardNumberState: CardNumberTextFieldState,
    onCardNumberEvent: (CardNumberTextFieldEvent) -> Unit
) {
    Column(modifier = modifier) {
        Text(
            text = "Card Number",
        )
        Spacer(Modifier.height(4.dp))
        CardNumberTextField(
            state = state,
            onEvent = onCardNumberEvent,
            decorationBox = { innerTextField ->
                // Customize the input with relevant information like borders, icons, colors and more
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .addBorder(
                            isFocused = cardNumberState.isFocused,
                        )
                        .height(OutlinedTextFieldDefaults.MinHeight)
                        .padding(horizontal = 16.dp),
                ) {
                    Box {
                        if (cardNumberState.length == 0) {
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

@Preview(name = "Payment Screen Example", showBackground = true)
@Composable
fun PaymentExampleScreenPreview() {
    ExampleTheme {
        PaymentExampleScreen()
    }
}

// @Preview(name = "Empty Security Field", showBackground = true)
// @Composable
// fun SecurityCodeExamplePreview() {
//    ExampleTheme {
//        SecurityCodeExample()
//    }
// }
//
// @Preview(name = "Empty Expiration Date Field", showBackground = true)
// @Composable
// fun ExpirationDatePreview() {
//    ExampleTheme {
//        ExpirationDateExample()
//    }
// }
//
// @Preview(name = "Empty Card Number Field", showBackground = true)
// @Composable
// fun CardNumberPreview() {
//    ExampleTheme {
//        CardNumberTextField()
//    }
// }
