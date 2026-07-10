package com.mercadopago.sdk.android.checkout.presentation.installments

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.sharp.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
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
import com.mercadopago.sdk.android.components.MPProgressIndicator
import com.mercadopago.sdk.android.components.model.MPListItemContentInfo
import com.mercadopago.sdk.android.components.model.MPListItemTrailing
import com.mercadopago.sdk.android.components.model.MPListItemType
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoThemes

@Composable
internal fun InstallmentsScreen(
    viewModel: InstallmentsViewModel,
    isLoading: Boolean = false,
    onBackClick: () -> Unit = {},
) {
    val viewState by viewModel.viewState.collectAsState()

    BackHandler(enabled = isLoading) { }

    InstallmentsScreenContent(
        viewState = viewState,
        isLoading = isLoading,
        onBackClick = onBackClick,
        onItemClick = viewModel::onInstallmentSelected,
        onPayClick = viewModel::onPayClicked,
    )
}

@Suppress("LongMethod")
@Composable
private fun InstallmentsScreenContent(
    viewState: InstallmentsScreenState,
    isLoading: Boolean = false,
    onBackClick: () -> Unit = {},
    onItemClick: (Int) -> Unit = {},
    onPayClick: () -> Unit = {},
) {
    val density = LocalDensity.current
    var footerHeightPx by remember { mutableIntStateOf(0) }
    val footerHeightDp = with(density) { footerHeightPx.toDp() }

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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 4.dp)
                    .padding(bottom = footerHeightDp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                viewState.items.forEach { item ->
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
        if (viewState.footerState.isVisible) {
            InstallmentsFooter(
                footerState = viewState.footerState,
                onPayClick = onPayClick,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .onGloballyPositioned { footerHeightPx = it.size.height },
            )
        }

        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
                contentAlignment = Alignment.Center,
            ) {
                MPProgressIndicator()
            }
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
            description = item.description.takeIf { it.isNotEmpty() },
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clearAndSetSemantics {
                contentDescription = item.accessibilityLabel
                role = Role.Button
            },
        trailing = MPListItemTrailing(
            text = item.trailing,
            type = MPListItemTrailing.Type.Icon(
                icon = Icons.AutoMirrored.Sharp.KeyboardArrowRight,
            ),
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
            description = item.description.takeIf { it.isNotEmpty() },
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clearAndSetSemantics {
                contentDescription = item.accessibilityLabel
                role = Role.Button
            },
        type = MPListItemType.RadioButton(selected = item.isSelected),
        trailing = if (item.trailing.isNotEmpty()) {
            MPListItemTrailing(text = item.trailing)
        } else {
            null
        },
        onClick = { onItemClick(item.number) },
    )
}

@Composable
private fun InstallmentsFooter(
    footerState: FooterState,
    onPayClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    MPFixedFooter(
        title = footerState.title,
        amount = MPAmountData(
            currencySymbol = footerState.currencySymbol,
            integerPart = footerState.amountIntegerPart,
            decimalPart = footerState.amountDecimalPart,
        ),
        subtitle = footerState.subtitle,
        button = footerState.buttonLabel?.let { label ->
            MPFixedFooterButtonData(
                text = label,
                isLoading = footerState.isButtonLoading,
                onClick = onPayClick,
            )
        },
        modifier = modifier,
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
                items = listOf(
                    InstallmentState(
                        text = "1x R$ 300,00",
                        trailing = "",
                        description = "CFT: 0,00%  TEA: 0,00%",
                        isSelected = false,
                        number = 1,
                        accessibilityLabel = "1x R$ 300,00",
                    ),
                    InstallmentState(
                        text = "2x R$ 190,00",
                        trailing = "R$ 380,00",
                        description = "CFT: 369,00%  TEA: 265,00%",
                        isSelected = false,
                        number = 2,
                        accessibilityLabel = "2x R$ 190,00",
                    ),
                ),
                footerState = FooterState(
                    title = "Total",
                    currencySymbol = "R$",
                    amountIntegerPart = "300",
                    amountDecimalPart = "00",
                    subtitle = "Visa **** 1234",
                    isVisible = true,
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
                items = listOf(
                    InstallmentState(
                        text = "1x R$ 300,00",
                        trailing = "",
                        description = "CFT: 0,00%  TEA: 0,00%",
                        isSelected = true,
                        number = 1,
                        accessibilityLabel = "1x R$ 300,00",
                    ),
                    InstallmentState(
                        text = "2x R$ 190,00",
                        trailing = "R$ 380,00",
                        description = "CFT: 369,00%  TEA: 265,00%",
                        isSelected = false,
                        number = 2,
                        accessibilityLabel = "2x R$ 190,00",
                    ),
                ),
                footerState = FooterState(
                    title = "Total",
                    currencySymbol = "R$",
                    amountIntegerPart = "300",
                    amountDecimalPart = "00",
                    subtitle = "Mastercard **** 5678",
                    buttonLabel = "Pagar",
                    isVisible = true,
                ),
            ),
        )
    }
}
