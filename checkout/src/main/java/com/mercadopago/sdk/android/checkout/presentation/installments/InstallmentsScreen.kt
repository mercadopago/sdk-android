package com.mercadopago.sdk.android.checkout.presentation.installments

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.sharp.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mercadopago.sdk.android.checkout.presentation.state.FooterState
import com.mercadopago.sdk.android.checkout.presentation.state.InstallmentState
import com.mercadopago.sdk.android.checkout.presentation.state.InstallmentsDisplayType
import com.mercadopago.sdk.android.checkout.presentation.state.InstallmentsScreenState
import com.mercadopago.sdk.android.checkout.presentation.viewmodel.InstallmentsViewModel
import com.mercadopago.sdk.android.components.MPAmountData
import com.mercadopago.sdk.android.components.MPFixedFooter
import com.mercadopago.sdk.android.components.MPFixedFooterButtonData
import com.mercadopago.sdk.android.components.MPHeader
import com.mercadopago.sdk.android.components.MPListItem
import com.mercadopago.sdk.android.components.model.MPListItemContentInfo
import com.mercadopago.sdk.android.components.model.MPListItemTrailing
import com.mercadopago.sdk.android.components.model.MPListItemType
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoThemes

@Composable
internal fun InstallmentsScreen(
    viewModel: InstallmentsViewModel,
    onBackClick: () -> Unit = {},
) {
    val viewState by viewModel.viewState.collectAsState()

    InstallmentsScreenContent(
        viewState = viewState,
        onBackClick = onBackClick,
    )
}

@Composable
private fun InstallmentsScreenContent(
    viewState: InstallmentsScreenState,
    onBackClick: () -> Unit = {},
    onItemClick: (Int) -> Unit = {},
    onPayClick: () -> Unit = {},
) {
    val density = LocalDensity.current
    var footerHeightPx by remember { mutableStateOf(0) }
    val footerHeightDp = with(density) { footerHeightPx.toDp() }

    Box(modifier = Modifier.fillMaxSize()) {
        MPHeader(
            modifier = Modifier.fillMaxSize(),
            title = viewState.title.orEmpty(),
            onBackClick = onBackClick,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 4.dp)
                    .padding(bottom = footerHeightDp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                viewState.installmentsState.forEach { item ->
                    when (viewState.displayType) {
                        InstallmentsDisplayType.Chevron -> ChevronInstallmentItem(
                            item = item,
                            onItemClick = onItemClick,
                        )
                        InstallmentsDisplayType.RadioButton -> RadioButtonInstallmentItem(
                            item = item,
                            onItemClick = onItemClick,
                        )
                    }
                }
            }
        }
        viewState.footerState?.let { footer ->
            MPFixedFooter(
                title = footer.title,
                amount = MPAmountData(
                    currencySymbol = footer.currencySymbol,
                    integerPart = footer.amountIntegerPart,
                    decimalPart = footer.amountDecimalPart,
                ),
                subtitle = footer.subtitle,
                button = footer.buttonLabel?.let { label ->
                    MPFixedFooterButtonData(
                        text = label,
                        onClick = onPayClick,
                    )
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .onGloballyPositioned { footerHeightPx = it.size.height },
            )
        }
    }
}

@Composable
private fun ChevronInstallmentItem(
    item: InstallmentState,
    onItemClick: (Int) -> Unit,
) {
    MPListItem(
        contentInfo = MPListItemContentInfo(
            title = item.text,
            description = item.description,
        ),
        modifier = Modifier.fillMaxWidth(),
        trailing = MPListItemTrailing(
            text = item.trailing,
            type = MPListItemTrailing.Type.Icon(
                icon = Icons.AutoMirrored.Sharp.KeyboardArrowRight,
            ),
            textColor = if (item.interestFree) {
                MercadoPagoTheme.color.feedback.positive.textLoud
            } else {
                null
            },
        ),
        onClick = { onItemClick(item.number) },
    )
}

@Composable
private fun RadioButtonInstallmentItem(
    item: InstallmentState,
    onItemClick: (Int) -> Unit,
) {
    MPListItem(
        contentInfo = MPListItemContentInfo(
            title = item.text,
            description = item.description,
        ),
        modifier = Modifier.fillMaxWidth(),
        type = MPListItemType.RadioButton(selected = item.isSelected),
        trailing = if (item.trailing.isNotEmpty()) {
            MPListItemTrailing(
                text = item.trailing,
                textColor = if (item.interestFree) {
                    MercadoPagoTheme.color.feedback.positive.textLoud
                } else {
                    null
                },
            )
        } else {
            null
        },
        onClick = { onItemClick(item.number) },
    )
}

@Preview(showBackground = true, name = "Installments Screen - Chevron")
@Composable
private fun InstallmentsScreenChevronPreview() {
    MercadoPagoTheme(theme = MercadoPagoThemes.Default) {
        InstallmentsScreenContent(
            viewState = InstallmentsScreenState(
                title = "Escolha o parcelamento",
                displayType = InstallmentsDisplayType.Chevron,
                installmentsState = listOf(
                    InstallmentState(
                        text = "1x R$ 300,00",
                        description = "",
                        trailing = "",
                        interestFree = false,
                        isSelected = false,
                        number = 1,
                    ),
                    InstallmentState(
                        text = "2x R$ 190,00",
                        description = "",
                        trailing = "Sem acréscimo",
                        interestFree = true,
                        isSelected = false,
                        number = 2,
                    ),
                ),
                footerState = FooterState(
                    title = "Total",
                    currencySymbol = "R$",
                    amountIntegerPart = "300",
                    amountDecimalPart = "00",
                    subtitle = "Visa **** 1234",
                ),
            ),
        )
    }
}

@Preview(showBackground = true, name = "Installments Screen - RadioButton")
@Composable
private fun InstallmentsScreenRadioButtonPreview() {
    MercadoPagoTheme(theme = MercadoPagoThemes.Default) {
        InstallmentsScreenContent(
            viewState = InstallmentsScreenState(
                title = "Escolha o parcelamento",
                displayType = InstallmentsDisplayType.RadioButton,
                installmentsState = listOf(
                    InstallmentState(
                        text = "1x R$ 300,00",
                        description = "",
                        trailing = "",
                        interestFree = false,
                        isSelected = true,
                        number = 1,
                    ),
                    InstallmentState(
                        text = "2x R$ 190,00",
                        description = "",
                        trailing = "Sem acréscimo",
                        interestFree = true,
                        isSelected = false,
                        number = 2,
                    ),
                ),
                footerState = FooterState(
                    title = "Total",
                    currencySymbol = "R$",
                    amountIntegerPart = "300",
                    amountDecimalPart = "00",
                    subtitle = "Mastercard **** 5678",
                    buttonLabel = "Pagar",
                ),
            ),
        )
    }
}
