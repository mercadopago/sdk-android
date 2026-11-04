package com.mercadopago.sdk.android.checkout.presentation.methodselection

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mercadopago.sdk.android.checkout.domain.model.MethodSelectionOption
import com.mercadopago.sdk.android.checkout.presentation.shared.ButtonState
import com.mercadopago.sdk.android.checkout.presentation.shared.FooterState
import com.mercadopago.sdk.android.checkout.presentation.state.MethodSelectionScreenState
import com.mercadopago.sdk.android.components.MPAmountData
import com.mercadopago.sdk.android.components.MPFixedFooter
import com.mercadopago.sdk.android.components.MPFixedFooterButtonData
import com.mercadopago.sdk.android.components.MPHeader
import com.mercadopago.sdk.android.components.model.MPSubtitle
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme

private const val METHOD_SELECTION_GROUP = "METHOD_SELECTION"

@Composable
internal fun MethodSelectionScreen(
    screenState: MethodSelectionScreenState,
    onOptionTap: (MethodSelectionOption) -> Unit,
    onCtaTap: () -> Unit,
    onBack: () -> Unit,
) {
    BackHandler(onBack = onBack)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
    ) {
        MPHeader(
            modifier = Modifier.weight(1f),
            title = screenState.headerTitle,
            onBackClick = onBack,
        ) {
            Spacer(modifier = Modifier.size(MercadoPagoTheme.spacing.paddings.xsmall))
            Column(
                modifier = Modifier.padding(
                    horizontal = MercadoPagoTheme.spacing.paddings.xtiny,
                ),
                verticalArrangement = Arrangement.spacedBy(MercadoPagoTheme.spacing.paddings.micro),
            ) {
                screenState.options.forEach { option ->
                    MethodSelectionOptionRow(
                        option = option,
                        isArrowLayout = screenState.isArrowLayout,
                        isSelected = option.id == screenState.selectedOptionId,
                        onTap = { tappedOption -> onOptionTap(tappedOption) },
                    )
                }
            }
        }
        if (screenState.footerState.isVisible) {
            MethodSelectionFooter(
                footerState = screenState.footerState,
                onCtaTap = onCtaTap,
            )
        }
    }
}

@Composable
private fun MethodSelectionFooter(
    footerState: FooterState,
    onCtaTap: () -> Unit,
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
                onClick = onCtaTap,
            )
        },
    )
}

private val previewOptions = listOf(
    MethodSelectionOption(
        id = "pagofacil",
        name = "Pago Fácil",
        subtitle = "Vence en 3 días",
        iconUrl = "",
    ),
    MethodSelectionOption(
        id = "rapipago",
        name = "Rapipago",
        subtitle = "Vence en 3 días",
        iconUrl = "",
    ),
)

@Preview(
    name = "MethodSelection Radio Button",
    group = METHOD_SELECTION_GROUP,
    showBackground = true,
)
@Composable
private fun MethodSelectionRadioButtonPreview() {
    MercadoPagoTheme {
        MethodSelectionScreen(
            screenState = MethodSelectionScreenState(
                headerTitle = "¿Dónde quieres pagar?",
                options = previewOptions,
                selectedOptionId = "pagofacil",
                footerState = FooterState(
                    title = "Total",
                    currencySymbol = "$",
                    amountIntegerPart = "1.000",
                    amountDecimalPart = "00",
                    buttonLabel = "Generar código de pago",
                    isVisible = true,
                    buttonState = ButtonState(enabled = true),
                ),
                isArrowLayout = false,
            ),
            onOptionTap = {},
            onCtaTap = {},
            onBack = {},
        )
    }
}

@Preview(name = "MethodSelection Chevron", group = METHOD_SELECTION_GROUP, showBackground = true)
@Composable
private fun MethodSelectionChevronPreview() {
    MercadoPagoTheme {
        MethodSelectionScreen(
            screenState = MethodSelectionScreenState(
                headerTitle = "¿Dónde quieres pagar?",
                options = previewOptions,
                selectedOptionId = null,
                footerState = FooterState(
                    title = "Total",
                    currencySymbol = "$",
                    amountIntegerPart = "1.000",
                    amountDecimalPart = "00",
                    isVisible = true,
                ),
                isArrowLayout = true,
            ),
            onOptionTap = {},
            onCtaTap = {},
            onBack = {},
        )
    }
}
