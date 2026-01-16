package com.mercadopago.sdk.android.checkout.presentation.cardpayment

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mercadopago.sdk.android.checkout.presentation.state.CardHolderState
import com.mercadopago.sdk.android.checkout.presentation.state.CardNumberState
import com.mercadopago.sdk.android.checkout.presentation.state.CardPaymentScreenState
import com.mercadopago.sdk.android.checkout.presentation.state.ExpirationDateState
import com.mercadopago.sdk.android.checkout.presentation.state.FixedFooterState
import com.mercadopago.sdk.android.checkout.presentation.state.IdentificationTypeState
import com.mercadopago.sdk.android.checkout.presentation.state.SecurityCodeState
import com.mercadopago.sdk.android.checkout.presentation.viewmodel.CardPaymentViewModel
import com.mercadopago.sdk.android.components.MPAmountData
import com.mercadopago.sdk.android.components.MPFixedFooter
import com.mercadopago.sdk.android.components.MPFixedFooterButtonData
import com.mercadopago.sdk.android.components.MPHeader
import com.mercadopago.sdk.android.components.inputs.MPCardNumberTextField
import com.mercadopago.sdk.android.components.inputs.MPExpirationDateTextField
import com.mercadopago.sdk.android.components.inputs.MPIdentificationTextField
import com.mercadopago.sdk.android.components.inputs.MPSecurityCodeTextField
import com.mercadopago.sdk.android.components.inputs.MPSimpleTextField
import com.mercadopago.sdk.android.coremethods.domain.model.IdentificationType
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.cardnumber.CardNumberTextFieldEvent
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.expirationdate.ExpirationDateTextFieldEvent
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.identificationtextfield.IdentificationTextFieldEvent
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.PCIFieldState
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.rememberPCIFieldState
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.securitycode.SecurityCodeTextFieldEvent
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.simpletextfield.SimpleTextFieldEvent
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoAndesTheme
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoThemes

@Composable
internal fun CardPaymentScreen(
    viewModel: CardPaymentViewModel,
    onBackClick: () -> Unit = {},
) {
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
        onBackClick = onBackClick,
    )
}

@Suppress("LongParameterList", "LongMethod")
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
    onFooterButtonClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
) {
    MPHeader(
        title = "Preencha os dados do\ncartão",
        onBackClick = onBackClick,
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 120.dp)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
            ) {
                Spacer(Modifier.width(MercadoPagoAndesTheme.spacing.gap.tiny))
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
                    onEvent = onCardNumberEvent,
                )
                if (viewState.cardHolderState.show) {
                    Spacer(Modifier.width(MercadoPagoAndesTheme.spacing.gap.tiny))
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
                        onEvent = onCardHolderEvent,
                    )
                }
                Spacer(Modifier.width(MercadoPagoAndesTheme.spacing.gap.tiny))
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
                        onEvent = onExpirationDateEvent,
                    )
                    Spacer(Modifier.width(MercadoPagoAndesTheme.spacing.gap.micro))
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
                        onEvent = onSecurityCodeEvent,
                    )
                }
                if (viewState.identificationTypeState.show) {
                    Spacer(Modifier.width(MercadoPagoAndesTheme.spacing.gap.tiny))
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
                            onEvent = onIdentificationEvent,
                        )
                    }
                }
            }
            MPFixedFooter(
                title = viewState.fixedFooterState.title,
                amount = MPAmountData(
                    currencySymbol = viewState.fixedFooterState.currencySymbol,
                    integerPart = viewState.fixedFooterState.amountIntegerPart,
                    decimalPart = viewState.fixedFooterState.amountDecimalPart,
                ),
                subtitle = viewState.fixedFooterState.subtitle,
                buttonData = MPFixedFooterButtonData(
                    text = viewState.fixedFooterState.buttonText,
                    enabled = viewState.fixedFooterState.buttonEnabled,
                    onClick = onFooterButtonClick,
                ),
                modifier = Modifier.align(Alignment.BottomCenter),
            )
        }
    }
}

@Suppress("LongMethod")
@Preview(showBackground = true, name = "Card Payment Screen - Default")
@Composable
private fun CardPaymentScreenContentPreview() {
    MercadoPagoTheme(
        theme = MercadoPagoThemes.Andes,
    ) {
        CardPaymentScreenContent(
            viewState = CardPaymentScreenState(
                expirationDateState = ExpirationDateState(
                    label = "Vencimiento",
                    placeHolder = "MM/AA",
                ),
                secureCodeState = SecurityCodeState(
                    label = "CVV",
                    placeHolder = "123",
                    secureCodeLength = 3,
                ),
                cardNumberState = CardNumberState(
                    label = "Número de tarjeta",
                    placeHolder = "0000 0000 0000 0000",
                ),
                cardHolderState = CardHolderState(
                    show = true,
                    label = "Nombre del titular",
                    placeHolder = "Maria Elena",
                    helper = "Como descrito atrás do cartão",
                ),
                identificationTypeState = IdentificationTypeState(
                    show = true,
                    label = "Documento",
                    placeHolder = "Número de documento",
                    identificationTypes = listOf(),
                    selected = IdentificationType(
                        id = "CPF",
                        name = "CPF",
                        type = "number",
                        minLength = 11,
                        maxLength = 11,
                    ),
                ),
                fixedFooterState = FixedFooterState(
                    title = "Total a pagar",
                    currencySymbol = "$",
                    amountIntegerPart = "1.000",
                    amountDecimalPart = "00",
                    subtitle = "em até 12x sem juros",
                    buttonText = "Pagar",
                    buttonEnabled = true,
                ),
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
            onFooterButtonClick = {},
            onBackClick = {},
        )
    }
}

@Suppress("LongMethod")
@Preview(showBackground = true, name = "Card Payment Screen - Without Card Holder")
@Composable
private fun CardPaymentScreenContentWithoutCardHolderPreview() {
    MercadoPagoTheme(
        theme = MercadoPagoThemes.Andes,
    ) {
        CardPaymentScreenContent(
            viewState = CardPaymentScreenState(
                expirationDateState = ExpirationDateState(
                    label = "Vencimiento",
                    placeHolder = "MM/AA",
                ),
                secureCodeState = SecurityCodeState(
                    label = "CVV",
                    placeHolder = "123",
                    secureCodeLength = 3,
                ),
                cardNumberState = CardNumberState(
                    label = "Número de tarjeta",
                    placeHolder = "0000 0000 0000 0000",
                ),
                cardHolderState = CardHolderState(show = false),
                identificationTypeState = IdentificationTypeState(show = false),
                fixedFooterState = FixedFooterState(
                    title = "Total",
                    currencySymbol = "$",
                    amountIntegerPart = "500",
                    amountDecimalPart = "00",
                    buttonText = "Continuar",
                    buttonEnabled = true,
                ),
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
            onFooterButtonClick = {},
            onBackClick = {},
        )
    }
}

@Suppress("LongMethod")
@Preview(showBackground = true, name = "Card Payment Screen - With Error")
@Composable
private fun CardPaymentScreenContentWithErrorPreview() {
    MercadoPagoTheme(
        theme = MercadoPagoThemes.Andes,
    ) {
        CardPaymentScreenContent(
            viewState = CardPaymentScreenState(
                expirationDateState = ExpirationDateState(
                    label = "Vencimiento",
                    placeHolder = "MM/AA",
                    error = Pair(true, "Fecha inválida"),
                ),
                secureCodeState = SecurityCodeState(
                    label = "CVV",
                    placeHolder = "123",
                    secureCodeLength = 3,
                    error = Pair(true, "CVV inválido"),
                ),
                cardNumberState = CardNumberState(
                    label = "Número de tarjeta",
                    placeHolder = "0000 0000 0000 0000",
                    error = Pair(true, "Número de tarjeta inválido"),
                ),
                cardHolderState = CardHolderState(
                    show = true,
                    label = "Nombre del titular",
                    placeHolder = "Maria Elena",
                    error = true,
                ),
                identificationTypeState = IdentificationTypeState(
                    show = true,
                    label = "Documento",
                    placeHolder = "Número de documento",
                    error = true,
                    identificationTypes = listOf(),
                    selected = IdentificationType(
                        id = "CPF",
                        name = "CPF",
                        type = "number",
                        minLength = 11,
                        maxLength = 11,
                    ),
                ),
                fixedFooterState = FixedFooterState(
                    title = "Total a pagar",
                    currencySymbol = "$",
                    amountIntegerPart = "2.500",
                    amountDecimalPart = "50",
                    subtitle = "em até 12x",
                    buttonText = "Pagar",
                    buttonEnabled = false,
                ),
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
            onFooterButtonClick = {},
            onBackClick = {},
        )
    }
}
