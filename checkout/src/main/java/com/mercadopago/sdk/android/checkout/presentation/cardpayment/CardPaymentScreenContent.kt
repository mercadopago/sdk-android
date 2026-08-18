package com.mercadopago.sdk.android.checkout.presentation.cardpayment

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mercadopago.sdk.android.checkout.presentation.model.CancelReason
import com.mercadopago.sdk.android.checkout.presentation.shared.ButtonState
import com.mercadopago.sdk.android.checkout.presentation.shared.FooterState
import com.mercadopago.sdk.android.checkout.presentation.state.CardHolderState
import com.mercadopago.sdk.android.checkout.presentation.state.CardNumberState
import com.mercadopago.sdk.android.checkout.presentation.state.CardPaymentScreenState
import com.mercadopago.sdk.android.checkout.presentation.state.ExpirationDateState
import com.mercadopago.sdk.android.checkout.presentation.state.IdentificationTypeState
import com.mercadopago.sdk.android.checkout.presentation.state.SecurityCodeState
import com.mercadopago.sdk.android.checkout.presentation.viewmodel.CardPaymentViewModel
import com.mercadopago.sdk.android.components.MPAmountData
import com.mercadopago.sdk.android.components.MPButton
import com.mercadopago.sdk.android.components.MPFixedFooter
import com.mercadopago.sdk.android.components.MPFixedFooterButtonData
import com.mercadopago.sdk.android.components.MPHeader
import com.mercadopago.sdk.android.components.MPMessage
import com.mercadopago.sdk.android.components.MPMessageType
import com.mercadopago.sdk.android.components.MPTooltip
import com.mercadopago.sdk.android.components.bottomsheet.MPListBottomSheet
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
        viewModel.initialization()
    }

    BackHandler {
        if (!viewState.isLoading) {
            viewModel.onBackPressed(CancelReason.SystemBack)
        }
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
        onBackPressed = { if (!viewState.isLoading) viewModel.onBackPressed(CancelReason.UiButton) },
        onTooltipClick = viewModel::onTooltipClick,
        onMessageClick = viewModel::onMessageClick,
        onFooterButtonClick = {
            focusManager.clearFocus()
            viewModel.onSubmit(
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
    var showIdentificationBottomSheet by remember { mutableStateOf(false) }
    val density = LocalDensity.current
    val isImeVisible = WindowInsets.ime.getBottom(density) > 0
    var overlayButtonHeightPx by remember { mutableIntStateOf(0) }
    val overlayButtonHeightDp = with(density) { overlayButtonHeightPx.toDp() }

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
            modifier = Modifier.fillMaxSize(),
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .imePadding()
                    .padding(bottom = if (isImeVisible) overlayButtonHeightDp else 0.dp),
            ) {
                MPHeader(
                    modifier = Modifier.fillMaxSize(),
                    title = viewState.title,
                    onBackClick = {
                        onBackPressed()
                    },
                ) {
                    CardPaymentFormFields(
                        viewState = viewState,
                        cardNumberPCIState = cardNumberPCIState,
                        expirationDatePCIState = expirationDatePCIState,
                        securityCodePCIState = securityCodePCIState,
                        cardHolderPCIState = cardHolderPCIState,
                        identificationPCIState = identificationPCIState,
                        cardNumberFocusRequester = cardNumberFocusRequester,
                        onCardNumberEvent = onCardNumberEvent,
                        onExpirationDateEvent = onExpirationDateEvent,
                        onSecurityCodeEvent = onSecurityCodeEvent,
                        onCardHolderEvent = onCardHolderEvent,
                        onIdentificationEvent = onIdentificationEvent,
                        onTooltipClick = onTooltipClick,
                        onIdentificationSelectorClick = { showIdentificationBottomSheet = true },
                    )
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

            if (!isImeVisible) {
                CardPaymentFooter(
                    footerState = viewState.footerState,
                    onFooterButtonClick = onFooterButtonClick,
                )
            }
        }

        if (isImeVisible) {
            CardPaymentFooterButtonOverlay(
                modifier = Modifier.align(Alignment.BottomCenter),
                buttonLabel = viewState.footerState.buttonLabel.orEmpty(),
                enabled = viewState.footerState.buttonState?.enabled ?: false,
                isLoading = viewState.footerState.buttonState?.isLoading ?: false,
                onClick = onFooterButtonClick,
                onHeightChanged = { overlayButtonHeightPx = it },
            )
        }

        if (viewState.showTooltip) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                    ) { onTooltipClick() },
            )
        }
    }

    if (viewState.isLoading) {
        com.mercadopago.sdk.android.checkout.presentation.loading.LoadingScreen(
            modifier = Modifier.fillMaxSize(),
        )
    }

    if (showIdentificationBottomSheet) {
        val types = viewState.identificationTypeState.identificationTypes.orEmpty()
        MPListBottomSheet(
            title = viewState.identificationTypeState.label,
            items = types.toBottomSheetItems(),
            selectedLabel = viewState.identificationTypeState.selected?.name,
            onItemSelected = { item ->
                types.findByBottomSheetItem(item)?.let {
                    onIdentificationEvent(IdentificationTextFieldEvent.OnTypeSelected(it))
                }
            },
            onDismiss = { showIdentificationBottomSheet = false },
        )
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
                footerState = FooterState(
                    title = "Total a pagar",
                    currencySymbol = "$",
                    amountIntegerPart = "1.000",
                    amountDecimalPart = "00",
                    subtitle = "em até 12x sem juros",
                    buttonLabel = "Pagar",
                    buttonState = ButtonState(enabled = true),
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
                footerState = FooterState(
                    title = "Total",
                    currencySymbol = "$",
                    amountIntegerPart = "500",
                    amountDecimalPart = "00",
                    buttonLabel = "Continuar",
                    buttonState = ButtonState(enabled = true),
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
                footerState = FooterState(
                    title = "Total a pagar",
                    currencySymbol = "$",
                    amountIntegerPart = "2.500",
                    amountDecimalPart = "50",
                    subtitle = "em até 12x",
                    buttonLabel = "Pagar",
                    buttonState = ButtonState(enabled = false),
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

@Suppress("LongParameterList", "LongMethod")
@Composable
private fun CardPaymentFormFields(
    viewState: CardPaymentScreenState,
    cardNumberPCIState: PCIFieldState,
    expirationDatePCIState: PCIFieldState,
    securityCodePCIState: PCIFieldState,
    cardHolderPCIState: PCIFieldState,
    identificationPCIState: PCIFieldState,
    cardNumberFocusRequester: FocusRequester,
    onCardNumberEvent: (CardNumberTextFieldEvent) -> Unit,
    onExpirationDateEvent: (ExpirationDateTextFieldEvent) -> Unit,
    onSecurityCodeEvent: (SecurityCodeTextFieldEvent) -> Unit,
    onCardHolderEvent: (SimpleTextFieldEvent) -> Unit,
    onIdentificationEvent: (IdentificationTextFieldEvent) -> Unit,
    onTooltipClick: () -> Unit,
    onIdentificationSelectorClick: () -> Unit,
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
                    MPTooltip(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .layout { measurable, constraints ->
                                val placeable = measurable.measure(constraints)
                                layout(placeable.width, 0) {
                                    placeable.placeRelative(0, -placeable.height)
                                }
                            },
                        text = viewState.secureCodeState.messageTooltip,
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
                    onSelectorClick = onIdentificationSelectorClick,
                    onEvent = onIdentificationEvent,
                )
            }
            Spacer(Modifier.size(MercadoPagoTheme.spacing.gap.xsmall))
        }
    }
}

@Suppress("UnusedPrivateMember")
@Composable
private fun CardPaymentFooter(
    footerState: FooterState,
    onFooterButtonClick: () -> Unit,
) {
    Surface(
        shadowElevation = 8.dp,
        tonalElevation = 0.dp,
    ) {
        MPFixedFooter(
            title = footerState.title,
            amount = footerState.toAmountData(),
            subtitle = footerState.subtitle,
            button = MPFixedFooterButtonData(
                text = footerState.buttonLabel.orEmpty(),
                enabled = footerState.buttonState?.enabled ?: false,
                isLoading = footerState.buttonState?.isLoading ?: false,
                onClick = onFooterButtonClick,
            ),
        )
    }
}

@Suppress("UnusedPrivateMember")
@Composable
private fun CardPaymentFooterButtonOverlay(
    buttonLabel: String,
    enabled: Boolean,
    isLoading: Boolean,
    onClick: () -> Unit,
    onHeightChanged: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .imePadding()
            .onGloballyPositioned { onHeightChanged(it.size.height) },
        shadowElevation = 8.dp,
        tonalElevation = 0.dp,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = MercadoPagoTheme.spacing.paddings.xtiny,
                    vertical = MercadoPagoTheme.spacing.paddings.xtiny,
                ),
        ) {
            MPButton(
                text = buttonLabel,
                modifier = Modifier.fillMaxWidth(),
                enabled = enabled,
                isLoading = isLoading,
                onClick = onClick,
            )
        }
    }
}

private fun FooterState.toAmountData(): MPAmountData? =
    if (amountIntegerPart.isNotEmpty()) {
        MPAmountData(
            currencySymbol = currencySymbol,
            integerPart = amountIntegerPart,
            decimalPart = amountDecimalPart,
        )
    } else {
        null
    }
