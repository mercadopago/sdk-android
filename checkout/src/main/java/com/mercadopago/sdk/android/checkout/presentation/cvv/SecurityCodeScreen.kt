package com.mercadopago.sdk.android.checkout.presentation.cvv

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.tooling.preview.Preview
import com.mercadopago.sdk.android.checkout.presentation.shared.ButtonState
import com.mercadopago.sdk.android.checkout.presentation.shared.FooterState
import com.mercadopago.sdk.android.checkout.presentation.state.SecurityCodeScreenState
import com.mercadopago.sdk.android.checkout.presentation.state.SecurityCodeState
import com.mercadopago.sdk.android.checkout.presentation.viewmodel.SecurityCodeViewModel
import com.mercadopago.sdk.android.components.MPAmountData
import com.mercadopago.sdk.android.components.MPFixedFooter
import com.mercadopago.sdk.android.components.MPFixedFooterButtonData
import com.mercadopago.sdk.android.components.MPHeader
import com.mercadopago.sdk.android.components.MPListItem
import com.mercadopago.sdk.android.components.MPTooltip
import com.mercadopago.sdk.android.components.inputs.MPSecurityCodeTextField
import com.mercadopago.sdk.android.components.model.MPListItemContentInfo
import com.mercadopago.sdk.android.components.model.MPListItemLeading
import com.mercadopago.sdk.android.components.model.MPListSizeType
import com.mercadopago.sdk.android.components.model.MPSubtitle
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.PCIFieldState
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.rememberPCIFieldState
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.securitycode.SecurityCodeTextFieldEvent
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoThemes

/**
 * CVV (security code) screen shown for a saved card before tokenization.
 *
 * Reached only when [com.mercadopago.sdk.android.checkout.domain.usecase.GetSecurityCodeScreenUseCase]
 * returns a non-null config. The raw CVV never leaves the field's `PCIFieldState`; this screen only
 * forwards length/focus/filled signals to [SecurityCodeViewModel] via [SecurityCodeTextFieldEvent].
 */
@Composable
internal fun SecurityCodeScreen(
    viewModel: SecurityCodeViewModel,
) {
    val viewState by viewModel.viewState.collectAsState()

    BackHandler { viewModel.onUserCancelled() }

    val securityCodePCIState = rememberPCIFieldState()

    SecurityCodeScreenContent(
        viewState = viewState,
        securityCodePCIState = securityCodePCIState,
        onSecurityCodeEvent = viewModel::onSecurityCodeEvent,
        onContinueClick = { viewModel.onContinue(securityCodePCIState) },
        onBackClick = viewModel::onUserCancelled,
    )
}

@Composable
private fun SecurityCodeScreenContent(
    viewState: SecurityCodeScreenState,
    securityCodePCIState: PCIFieldState,
    onSecurityCodeEvent: (SecurityCodeTextFieldEvent) -> Unit = {},
    onContinueClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
    ) {
        MPHeader(
            modifier = Modifier.fillMaxSize(),
            title = viewState.title,
            onBackClick = onBackClick,
        ) {
            if (viewState.cardTitle.isNotEmpty()) {
                SecurityCodeCardInfo(viewState)
            }
            SecurityCodeField(
                viewState = viewState,
                securityCodePCIState = securityCodePCIState,
                onSecurityCodeEvent = onSecurityCodeEvent,
            )
        }

        if (viewState.footerState.isVisible) {
            SecurityCodeFooter(
                footerState = viewState.footerState,
                onContinueClick = onContinueClick,
                modifier = Modifier.align(Alignment.BottomCenter),
            )
        }
    }
}

@Composable
private fun SecurityCodeCardInfo(
    viewState: SecurityCodeScreenState,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = MercadoPagoTheme.spacing.paddings.xnano)
            .background(
                color = MercadoPagoTheme.color.surface.primaryIdle,
                shape = RoundedCornerShape(MercadoPagoTheme.radius.small),
            ),
    ) {
        MPListItem(
            contentInfo = MPListItemContentInfo(
                title = viewState.cardTitle,
                description = viewState.cardDescription,
            ),
            leftImage = viewState.cardImageUrl?.let { MPListItemLeading.Thumbnail(it) },
            sizeType = MPListSizeType.Medium,
        )
    }
}

@Composable
private fun SecurityCodeField(
    viewState: SecurityCodeScreenState,
    securityCodePCIState: PCIFieldState,
    onSecurityCodeEvent: (SecurityCodeTextFieldEvent) -> Unit,
) {
    var showTooltip by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = MercadoPagoTheme.spacing.paddings.xtiny,
                end = MercadoPagoTheme.spacing.paddings.xtiny,
                top = MercadoPagoTheme.spacing.paddings.xmicro,
            ),
    ) {
        MPSecurityCodeTextField(
            state = securityCodePCIState,
            securityCodeSize = viewState.securityCodeState.maxLength,
            isFocused = viewState.securityCodeState.isFocused,
            showPlaceHolder = viewState.securityCodeState.showPlaceHolder,
            error = viewState.fieldError.orEmpty(),
            enabled = viewState.securityCodeState.enabled,
            label = viewState.securityCodeState.label,
            placeHolder = viewState.securityCodeState.placeHolder,
            onClickTooltip = { showTooltip = !showTooltip },
            onEvent = onSecurityCodeEvent,
        )
        if (showTooltip) {
            MPTooltip(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .layout { measurable, constraints ->
                        val placeable = measurable.measure(constraints)
                        layout(placeable.width, 0) {
                            placeable.placeRelative(0, -placeable.height)
                        }
                    },
                text = viewState.securityCodeState.helper,
            )
        }
    }
}

@Composable
private fun SecurityCodeFooter(
    footerState: FooterState,
    onContinueClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    MPFixedFooter(
        title = footerState.title,
        amount = MPAmountData(
            currencySymbol = footerState.currencySymbol,
            integerPart = footerState.amountIntegerPart,
            decimalPart = footerState.amountDecimalPart,
        ),
        subtitle = footerState.subtitle?.let { MPSubtitle(text = it) },
        button = footerState.buttonLabel?.let { label ->
            MPFixedFooterButtonData(
                text = label,
                enabled = footerState.buttonState?.enabled ?: false,
                isLoading = footerState.buttonState?.isLoading ?: false,
                onClick = onContinueClick,
            )
        },
        modifier = modifier,
    )
}

@Preview(showBackground = true, name = "Security Code Screen")
@Composable
private fun SecurityCodeScreenPreview() {
    MercadoPagoTheme(theme = MercadoPagoThemes.Default) {
        SecurityCodeScreenContent(
            securityCodePCIState = rememberPCIFieldState(),
            viewState = SecurityCodeScreenState(
                title = "Digite o código de segurança",
                cardTitle = "Banco •••• 1234",
                cardDescription = "Visa Crédito",
                cardImageUrl = "https://http.cat/200",
                securityCodeState = SecurityCodeState(
                    label = "Código de segurança",
                    placeHolder = "123",
                    maxLength = 3,
                    messageTooltip = "O CVV está no verso do seu cartão.",
                ),
                footerState = FooterState(
                    title = "Total",
                    currencySymbol = "R$",
                    amountIntegerPart = "300",
                    amountDecimalPart = "00",
                    subtitle = "Visa **** 1234",
                    buttonLabel = "Continuar",
                    buttonState = ButtonState(enabled = true),
                    isVisible = true,
                ),
            ),
        )
    }
}

@Preview(showBackground = true, name = "Security Code Screen - Error")
@Composable
private fun SecurityCodeScreenErrorPreview() {
    MercadoPagoTheme(theme = MercadoPagoThemes.Default) {
        SecurityCodeScreenContent(
            securityCodePCIState = rememberPCIFieldState(),
            viewState = SecurityCodeScreenState(
                title = "Digite o código de segurança",
                cardTitle = "Banco •••• 1234",
                cardDescription = "Visa Crédito",
                cardImageUrl = "https://http.cat/200",
                securityCodeState = SecurityCodeState(
                    label = "Código de segurança",
                    placeHolder = "123",
                    maxLength = 3,
                ),
                footerState = FooterState(
                    title = "Total",
                    currencySymbol = "R$",
                    amountIntegerPart = "300",
                    amountDecimalPart = "00",
                    subtitle = "Visa **** 1234",
                    buttonLabel = "Continuar",
                    buttonState = ButtonState(enabled = false),
                    isVisible = true,
                ),
            ),
        )
    }
}
