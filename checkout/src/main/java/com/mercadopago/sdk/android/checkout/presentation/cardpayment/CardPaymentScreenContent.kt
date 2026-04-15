package com.mercadopago.sdk.android.checkout.presentation.cardpayment

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalFocusManager
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
import com.mercadopago.sdk.android.components.MPFixedFooter
import com.mercadopago.sdk.android.components.MPFixedFooterButtonData
import com.mercadopago.sdk.android.components.MPHeader
import com.mercadopago.sdk.android.components.MPMessage
import com.mercadopago.sdk.android.components.MPMessageType
import com.mercadopago.sdk.android.components.MPPopover
import com.mercadopago.sdk.android.components.MPProgressIndicator
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
import com.mercadopago.sdk.android.coremethods.ui.utils.MaskVisualTransformation
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoThemes

@Composable
internal fun CardPaymentScreen(
    viewModel: CardPaymentViewModel,
) {
    val viewState by viewModel.viewState.collectAsState()
    val cardNumberPCIState = rememberPCIFieldState()
    val expirationDatePCIState = rememberPCIFieldState()
    val securityCodePCIState = rememberPCIFieldState()
    val cardHolderPCIState = rememberPCIFieldState()
    val identificationPCIState = rememberPCIFieldState()
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        viewModel.getIdentificationTypes()
    }

    BackHandler {
        viewModel.onBackPressed(CardPaymentViewModel.CancelReason.SystemBack)
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
        onBackPressed = { viewModel.onBackPressed(CardPaymentViewModel.CancelReason.UiButton) },
        onTooltipClick = viewModel::onTooltipClick,
        onMessageClick = viewModel::onMessageClick,
        onFooterButtonClick = {
            focusManager.clearFocus()
            viewModel.validateFieldsAndTokenize(
                cardNumberState = cardNumberPCIState,
                expirationDateState = expirationDatePCIState,
                securityCodeState = securityCodePCIState,
            )
        },
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
    onBackPressed: () -> Unit = {},
    onTooltipClick: () -> Unit = {},
    onMessageClick: () -> Unit = {},
) {
    val cardNumberFocusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        cardNumberFocusRequester.requestFocus()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding(),
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
            ) {
                MPHeader(
                    modifier = Modifier.fillMaxSize(),
                    title = viewState.title,
                    onBackClick = {
                        onBackPressed()
                    },
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                    ) {
                        Spacer(Modifier.size(MercadoPagoTheme.spacing.gap.xsmall))
                        MPCardNumberTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(cardNumberFocusRequester),
                            state = cardNumberPCIState,
                            isFocused = viewState.cardNumberState.isFocused,
                            showPlaceHolder = viewState.cardNumberState.showPlaceHolder,
                            error = viewState.cardNumberState.error,
                            enabled = viewState.cardNumberState.enabled,
                            label = viewState.cardNumberState.label,
                            helper = viewState.cardNumberState.helper,
                            placeHolder = viewState.cardNumberState.placeHolder,
                            maxLength = viewState.cardNumberState.maxLength,
                            visualTransformation = MaskVisualTransformation(viewState.cardNumberState.mask),
                            onEvent = onCardNumberEvent,
                        )

                        if (viewState.cardHolderState.show) {
                            Spacer(Modifier.size(MercadoPagoTheme.spacing.gap.xsmall))
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

                        Spacer(Modifier.size(MercadoPagoTheme.spacing.gap.xsmall))
                        MPExpirationDateTextField(
                            state = expirationDatePCIState,
                            isFocused = viewState.expirationDateState.isFocused,
                            showPlaceHolder = viewState.expirationDateState.showPlaceHolder,
                            error = viewState.expirationDateState.error,
                            enabled = viewState.expirationDateState.enabled,
                            label = viewState.expirationDateState.label,
                            helper = viewState.expirationDateState.helper,
                            placeHolder = viewState.expirationDateState.placeHolder,
                            onEvent = onExpirationDateEvent,
                        )

                        if (!viewState.secureCodeState.optional) {
                            Spacer(Modifier.size(MercadoPagoTheme.spacing.gap.xsmall))
                            Box {
                                MPSecurityCodeTextField(
                                    state = securityCodePCIState,
                                    securityCodeSize = viewState.secureCodeState.maxLength,
                                    isFocused = viewState.secureCodeState.isFocused,
                                    showPlaceHolder = viewState.secureCodeState.showPlaceHolder,
                                    error = viewState.secureCodeState.error,
                                    enabled = viewState.secureCodeState.enabled,
                                    label = viewState.secureCodeState.label,
                                    helper = viewState.secureCodeState.helper,
                                    placeHolder = viewState.secureCodeState.placeHolder,
                                    onClickTooltip = onTooltipClick,
                                    onEvent = onSecurityCodeEvent,
                                )
                                if (viewState.showTooltip) {
                                    MPPopover(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .layout { measurable, constraints ->
                                                val placeable = measurable.measure(constraints)
                                                layout(placeable.width, 0) {
                                                    placeable.placeRelative(0, -placeable.height)
                                                }
                                            },
                                        description = viewState.secureCodeState.messageTooltip,
                                        onDismiss = onTooltipClick,
                                    )
                                }
                            }
                        }

                        if (viewState.identificationTypeState.show) {
                            Spacer(Modifier.size(MercadoPagoTheme.spacing.gap.xsmall))
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
                            Spacer(Modifier.size(MercadoPagoTheme.spacing.gap.xsmall))
                        }
                    }
                }
            }

            if (viewState.showMessage) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                ) {
                    MPMessage(
                        text = viewState.messageError.description,
                        type = MPMessageType.Negative,
                    ) {
                        onMessageClick()
                    }
                }
            }

            if (viewState.fixedFooterState.isVisible) {
                Surface(
                    shadowElevation = 8.dp,
                    tonalElevation = 0.dp,
                    shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
                ) {
                    MPFixedFooter(
                        title = viewState.fixedFooterState.title,
                        subtitle = viewState.fixedFooterState.subtitle,
                        button = MPFixedFooterButtonData(
                            text = viewState.fixedFooterState.buttonText,
                            onClick = onFooterButtonClick,
                        ),
                    )
                }
            }
        }

        if (viewState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White.copy(alpha = 0.8f)),
                contentAlignment = Alignment.Center,
            ) {
                MPProgressIndicator()
            }
        }
    }
}

@Suppress("LongMethod")
@Preview(showBackground = true, name = "Card Payment Screen - Default")
@Composable
private fun CardPaymentScreenContentPreview() {
    MercadoPagoTheme(
        theme = MercadoPagoThemes.Default,
    ) {
        CardPaymentScreenContent(
            viewState = CardPaymentScreenState(
                expirationDateState = ExpirationDateState(
                    label = "Vencimiento",
                    placeHolder = "MM/AA",
                ),
                secureCodeState = SecurityCodeState(
                    label = "Código de Segurança",
                    placeHolder = "123",
                    maxLength = 3,
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
                    isVisible = true,
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
            onBackPressed = {},
        )
    }
}

@Suppress("LongMethod")
@Preview(showBackground = true, name = "Card Payment Screen - Without Card Holder")
@Composable
private fun CardPaymentScreenContentWithoutCardHolderPreview() {
    MercadoPagoTheme(
        theme = MercadoPagoThemes.Default,
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
                    maxLength = 3,
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
                    isVisible = true,
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
            onBackPressed = {},
        )
    }
}

@Suppress("LongMethod")
@Preview(showBackground = true, name = "Card Payment Screen - With Error")
@Composable
private fun CardPaymentScreenContentWithErrorPreview() {
    MercadoPagoTheme(
        theme = MercadoPagoThemes.Default,
    ) {
        CardPaymentScreenContent(
            viewState = CardPaymentScreenState(
                expirationDateState = ExpirationDateState(
                    label = "Vencimiento",
                    placeHolder = "MM/AA",
                    error = "Fecha inválida",
                ),
                secureCodeState = SecurityCodeState(
                    label = "CVV",
                    placeHolder = "123",
                    maxLength = 3,
                    error = "CVV inválido",
                ),
                cardNumberState = CardNumberState(
                    label = "Número de tarjeta",
                    placeHolder = "0000 0000 0000 0000",
                    error = "Número de tarjeta inválido",
                ),
                cardHolderState = CardHolderState(
                    show = true,
                    label = "Nombre del titular",
                    placeHolder = "Maria Elena",
                    error = "Erro de card Holder",
                ),
                identificationTypeState = IdentificationTypeState(
                    show = true,
                    label = "Documento",
                    placeHolder = "Número de documento",
                    error = "Erro no identification types",
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
                    isVisible = false,
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
            onBackPressed = {},
        )
    }
}
