package com.mercadopago.sdk.android.checkout.presentation.cardpayment

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mercadopago.sdk.android.checkout.presentation.state.CardHolderState
import com.mercadopago.sdk.android.checkout.presentation.state.CardNumberState
import com.mercadopago.sdk.android.checkout.presentation.state.CardPaymentDialogState
import com.mercadopago.sdk.android.checkout.presentation.state.CardPaymentScreenState
import com.mercadopago.sdk.android.checkout.presentation.state.ExpirationDateState
import com.mercadopago.sdk.android.checkout.presentation.state.IdentificationTypeState
import com.mercadopago.sdk.android.checkout.presentation.state.SecurityCodeState
import com.mercadopago.sdk.android.checkout.presentation.viewmodel.CardPaymentViewModel
import com.mercadopago.sdk.android.components.MPCardNumberTextField
import com.mercadopago.sdk.android.components.MPExpirationDateTextField
import com.mercadopago.sdk.android.components.MPIdentificationTextField
import com.mercadopago.sdk.android.components.MPSecurityCodeTextField
import com.mercadopago.sdk.android.components.MPSimpleTextField
import com.mercadopago.sdk.android.coremethods.domain.model.IdentificationType
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.cardnumber.CardNumberTextFieldEvent
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.expirationdate.ExpirationDateTextFieldEvent
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.identificationtextfield.IdentificationTextFieldEvent
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.PCIFieldState
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.rememberPCIFieldState
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.securitycode.SecurityCodeTextFieldEvent
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.simpletextfield.SimpleTextFieldEvent
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme

@Composable
internal fun CardPaymentScreen(viewModel: CardPaymentViewModel) {
    val viewState by viewModel.viewState.collectAsState()
    val cardNumberPCIState = rememberPCIFieldState()
    val expirationDatePCIState = rememberPCIFieldState()
    val securityCodePCIState = rememberPCIFieldState()
    val cardHolderPCIState = rememberPCIFieldState()
    val identificationPCIState = rememberPCIFieldState()

    LaunchedEffect(Unit) {
        viewModel.getIdentificationTypes()
    }

    CardPaymentScreenContent(
        viewState = viewState,
        cardNumberPCIState = cardNumberPCIState,
        expirationDatePCIState = expirationDatePCIState,
        securityCodePCIState = securityCodePCIState,
        cardHolderPCIState = cardHolderPCIState,
        identificationPCIState = identificationPCIState,
        onCardNumberEvent = viewModel::onCardNumberEvent,
        onExpirationDateEvent = viewModel::onExpirationDateEvent,
        onSecurityCodeEvent = viewModel::onSecurityCodeEvent,
        onCardHolderEvent = viewModel::onCardHolderEvent,
        onIdentificationEvent = viewModel::onIdentificationEvent,
        onGenerateToken = {
            viewModel.generateToken(
                cardNumberState = cardNumberPCIState,
                expirationDateState = expirationDatePCIState,
                securityCodeState = securityCodePCIState
            )
        },
        onDialogDismiss = { viewModel.onDialogStateChanged(CardPaymentDialogState.Hidden) }
    )
}

@Composable
internal fun CardPaymentScreenContent(
    viewState: CardPaymentScreenState,
    cardNumberPCIState: PCIFieldState,
    expirationDatePCIState: PCIFieldState,
    securityCodePCIState: PCIFieldState,
    cardHolderPCIState: PCIFieldState,
    identificationPCIState: PCIFieldState,
    onCardNumberEvent: (CardNumberTextFieldEvent) -> Unit,
    onExpirationDateEvent: (ExpirationDateTextFieldEvent) -> Unit,
    onSecurityCodeEvent: (SecurityCodeTextFieldEvent) -> Unit,
    onCardHolderEvent: (SimpleTextFieldEvent) -> Unit,
    onIdentificationEvent: (IdentificationTextFieldEvent) -> Unit,
    onGenerateToken: () -> Unit,
    onDialogDismiss: () -> Unit
) {
    CardPaymentDialogs(
        dialogState = viewState.dialogState,
        onDismiss = onDialogDismiss
    )

    Column(modifier = Modifier.padding(16.dp)) {
        MPCardNumberTextField(
            modifier = Modifier.fillMaxWidth(),
            state = cardNumberPCIState,
            isFocused = viewState.cardNumberState.isFocused,
            showPlaceHolder = viewState.cardNumberState.showPlaceHolder,
            error = viewState.cardNumberState.error.first,
            enabled = viewState.cardNumberState.enabled,
            label = viewState.cardNumberState.label,
            helper = viewState.cardNumberState.helper,
            placeHolder = viewState.cardNumberState.placeHolder,
            onEvent = onCardNumberEvent
        )

        if (viewState.cardHolderState.show) {
            MPSimpleTextField(
                modifier = Modifier.fillMaxWidth(),
                state = cardHolderPCIState,
                isFocused = viewState.cardHolderState.isFocused,
                showPlaceHolder = viewState.cardHolderState.showPlaceHolder,
                error = viewState.cardHolderState.error,
                enabled = viewState.cardHolderState.enabled,
                label = viewState.cardHolderState.label,
                helper = viewState.cardHolderState.helper,
                placeHolder = viewState.cardHolderState.placeHolder,
                onEvent = onCardHolderEvent
            )
        }

        Row {
            MPExpirationDateTextField(
                modifier = Modifier.weight(1f),
                state = expirationDatePCIState,
                isFocused = viewState.expirationDateState.isFocused,
                showPlaceHolder = viewState.expirationDateState.showPlaceHolder,
                error = viewState.expirationDateState.error.first,
                enabled = viewState.expirationDateState.enabled,
                label = viewState.expirationDateState.label,
                helper = viewState.expirationDateState.helper,
                placeHolder = viewState.expirationDateState.placeHolder,
                onEvent = onExpirationDateEvent
            )

            Spacer(Modifier.width(16.dp))

            MPSecurityCodeTextField(
                modifier = Modifier.weight(1f),
                state = securityCodePCIState,
                securityCodeSize = viewState.secureCodeState.secureCodeLength,
                isFocused = viewState.secureCodeState.isFocused,
                showPlaceHolder = viewState.secureCodeState.showPlaceHolder,
                error = viewState.secureCodeState.error.first,
                enabled = viewState.secureCodeState.enabled,
                label = viewState.secureCodeState.label,
                helper = viewState.secureCodeState.helper,
                placeHolder = viewState.secureCodeState.placeHolder,
                onEvent = onSecurityCodeEvent
            )
        }

        if (viewState.identificationTypeState.show) {
            viewState.identificationTypeState.identificationTypes?.let { types ->
                MPIdentificationTextField(
                    modifier = Modifier.fillMaxWidth(),
                    state = identificationPCIState,
                    identificationTypes = types,
                    selectedIdentificationType = viewState.identificationTypeState.selected,
                    isFocused = viewState.identificationTypeState.isFocused,
                    showPlaceHolder = viewState.identificationTypeState.showPlaceHolder,
                    error = viewState.identificationTypeState.error,
                    enabled = viewState.identificationTypeState.enabled,
                    label = viewState.identificationTypeState.label,
                    helper = viewState.identificationTypeState.helper,
                    placeHolder = viewState.identificationTypeState.placeHolder,
                    onEvent = onIdentificationEvent
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onGenerateToken,
            modifier = Modifier.fillMaxWidth(),
            enabled = !viewState.isLoading
        ) {
            Text(text = if (viewState.isLoading) "Processing..." else "Generate Token")
        }
    }
}

@Composable
private fun CardPaymentDialogs(
    dialogState: CardPaymentDialogState,
    onDismiss: () -> Unit
) {
    when (dialogState) {
        is CardPaymentDialogState.CardToken -> {
            AlertDialog(
                onDismissRequest = onDismiss,
                title = { Text("Token Generated") },
                text = { Text("Token: ${dialogState.token}") },
                confirmButton = {
                    Button(onClick = onDismiss) {
                        Text("OK")
                    }
                }
            )
        }
        is CardPaymentDialogState.Error -> {
            AlertDialog(
                onDismissRequest = onDismiss,
                title = { Text(dialogState.title) },
                text = { Text(dialogState.description) },
                confirmButton = {
                    Button(onClick = onDismiss) {
                        Text("OK")
                    }
                }
            )
        }
        CardPaymentDialogState.Hidden -> Unit
    }
}


@Preview(showBackground = true, name = "Card Payment Screen - Default")
@Composable
private fun CardPaymentScreenContentPreview() {
    MercadoPagoTheme {
        CardPaymentScreenContent(
            viewState = CardPaymentScreenState(
                expirationDateState = ExpirationDateState(
                    label = "Vencimiento",
                    placeHolder = "MM/AA"
                ),
                secureCodeState = SecurityCodeState(
                    label = "CVV",
                    placeHolder = "123",
                    secureCodeLength = 3
                ),
                cardNumberState = CardNumberState(
                    label = "Número de tarjeta",
                    placeHolder = "0000 0000 0000 0000"
                ),
                cardHolderState = CardHolderState(
                    show = true,
                    label = "Nombre del titular",
                    placeHolder = "Maria Elena"
                ),
                identificationTypeState = IdentificationTypeState(
                    show = true,
                    label = "Documento",
                    placeHolder = "Número de documento",
                    identificationTypes = listOf(
                        IdentificationType(
                            id = "CPF",
                            name = "CPF",
                            type = "number",
                            minLength = 11,
                            maxLength = 11
                        ),
                        IdentificationType(
                            id = "CNPJ",
                            name = "CNPJ",
                            type = "number",
                            minLength = 14,
                            maxLength = 14
                        )
                    ),
                    selected = IdentificationType(
                        id = "CPF",
                        name = "CPF",
                        type = "number",
                        minLength = 11,
                        maxLength = 11
                    )
                )
            ),
            cardNumberPCIState = rememberPCIFieldState(),
            expirationDatePCIState = rememberPCIFieldState(),
            securityCodePCIState = rememberPCIFieldState(),
            cardHolderPCIState = rememberPCIFieldState(),
            identificationPCIState = rememberPCIFieldState(),
            onCardNumberEvent = {},
            onExpirationDateEvent = {},
            onSecurityCodeEvent = {},
            onCardHolderEvent = {},
            onIdentificationEvent = {},
            onGenerateToken = {},
            onDialogDismiss = {}
        )
    }
}

@Preview(showBackground = true, name = "Card Payment Screen - Without Card Holder")
@Composable
private fun CardPaymentScreenContentWithoutCardHolderPreview() {
    MercadoPagoTheme {
        CardPaymentScreenContent(
            viewState = CardPaymentScreenState(
                expirationDateState = ExpirationDateState(
                    label = "Vencimiento",
                    placeHolder = "MM/AA"
                ),
                secureCodeState = SecurityCodeState(
                    label = "CVV",
                    placeHolder = "123",
                    secureCodeLength = 3
                ),
                cardNumberState = CardNumberState(
                    label = "Número de tarjeta",
                    placeHolder = "0000 0000 0000 0000"
                ),
                cardHolderState = CardHolderState(show = false),
                identificationTypeState = IdentificationTypeState(show = false)
            ),
            cardNumberPCIState = rememberPCIFieldState(),
            expirationDatePCIState = rememberPCIFieldState(),
            securityCodePCIState = rememberPCIFieldState(),
            cardHolderPCIState = rememberPCIFieldState(),
            identificationPCIState = rememberPCIFieldState(),
            onCardNumberEvent = {},
            onExpirationDateEvent = {},
            onSecurityCodeEvent = {},
            onCardHolderEvent = {},
            onIdentificationEvent = {},
            onGenerateToken = {},
            onDialogDismiss = {}
        )
    }
}

@Preview(showBackground = true, name = "Card Payment Screen - With Error")
@Composable
private fun CardPaymentScreenContentWithErrorPreview() {
    MercadoPagoTheme {
        CardPaymentScreenContent(
            viewState = CardPaymentScreenState(
                expirationDateState = ExpirationDateState(
                    label = "Vencimiento",
                    placeHolder = "MM/AA",
                    error = Pair(true, "Fecha inválida")
                ),
                secureCodeState = SecurityCodeState(
                    label = "CVV",
                    placeHolder = "123",
                    secureCodeLength = 3,
                    error = Pair(true, "CVV inválido")
                ),
                cardNumberState = CardNumberState(
                    label = "Número de tarjeta",
                    placeHolder = "0000 0000 0000 0000",
                    error = Pair(true, "Número de tarjeta inválido")
                ),
                cardHolderState = CardHolderState(
                    show = true,
                    label = "Nombre del titular",
                    placeHolder = "Maria Elena",
                    error = true
                ),
                identificationTypeState = IdentificationTypeState(
                    show = true,
                    label = "Documento",
                    placeHolder = "Número de documento",
                    error = true,
                    identificationTypes = listOf(
                        IdentificationType(
                            id = "CPF",
                            name = "CPF",
                            type = "number",
                            minLength = 11,
                            maxLength = 11
                        )
                    ),
                    selected = IdentificationType(
                        id = "CPF",
                        name = "CPF",
                        type = "number",
                        minLength = 11,
                        maxLength = 11
                    )
                )
            ),
            cardNumberPCIState = rememberPCIFieldState(),
            expirationDatePCIState = rememberPCIFieldState(),
            securityCodePCIState = rememberPCIFieldState(),
            cardHolderPCIState = rememberPCIFieldState(),
            identificationPCIState = rememberPCIFieldState(),
            onCardNumberEvent = {},
            onExpirationDateEvent = {},
            onSecurityCodeEvent = {},
            onCardHolderEvent = {},
            onIdentificationEvent = {},
            onGenerateToken = {},
            onDialogDismiss = {}
        )
    }
}
