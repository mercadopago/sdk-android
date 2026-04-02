package com.mercadopago.sdk.android.checkout.presentation.installments

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.sharp.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mercadopago.sdk.android.checkout.presentation.event.InstallmentsScreenEvent
import com.mercadopago.sdk.android.checkout.presentation.state.FooterState
import com.mercadopago.sdk.android.checkout.presentation.state.InstallmentState
import com.mercadopago.sdk.android.checkout.presentation.state.InstallmentsScreenState
import com.mercadopago.sdk.android.checkout.presentation.viewmodel.InstallmentsViewModel
import com.mercadopago.sdk.android.components.MPAmountData
import com.mercadopago.sdk.android.components.MPFixedFooter
import com.mercadopago.sdk.android.components.MPHeader
import com.mercadopago.sdk.android.components.MPListItem
import com.mercadopago.sdk.android.components.model.MPListItemContentInfo
import com.mercadopago.sdk.android.components.model.MPListItemTrailing
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoThemes
import java.math.BigDecimal

@Composable
internal fun InstallmentsScreen(
    viewModel: InstallmentsViewModel,
    onBackClick: () -> Unit = {},
    onInstallmentSelected: (Int) -> Unit = {},
) {
    val viewState by viewModel.viewState.collectAsState()
    val viewEvent by viewModel.viewEvent.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getInstallments(
            bin = "44443333",
            amount = BigDecimal.TEN,
        )
    }

    LaunchedEffect(viewEvent) {
        when (val event = viewEvent) {
            is InstallmentsScreenEvent.OnInstallmentsSelected -> {
                onInstallmentSelected(event.installment)
            }
            InstallmentsScreenEvent.Idle -> Unit
        }
    }

    InstallmentsScreenContent(
        viewState = viewState,
        onBackClick = onBackClick,
        onItemClick = { viewModel.onInstallmentSelected(installment = it) },
    )
}

@Composable
private fun InstallmentsScreenContent(
    viewState: InstallmentsScreenState,
    onBackClick: () -> Unit = {},
    onItemClick: (Int) -> Unit = {},
) {
    Box(modifier = Modifier.fillMaxSize()) {
        MPHeader(
            title = viewState.title.orEmpty(),
            onBackClick = onBackClick,
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 120.dp),
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        items(viewState.installmentsState) { item ->
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
                        modifier = Modifier.align(Alignment.BottomCenter),
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Card Payment Screen - Default")
@Composable
private fun InstallmentsScreenPreview() {
    MercadoPagoTheme(
        theme = MercadoPagoThemes.Default,
    ) {
        InstallmentsScreenContent(
            viewState = InstallmentsScreenState(
                title = "Escolha o parcelamento",
                installmentsState = listOf(
                    InstallmentState(
                        text = "1x 300,00",
                        description = "",
                        trailing = "R$ 300",
                        interestFree = false,
                        isSelected = false,
                        number = 1,
                    ),
                    InstallmentState(
                        number = 2,
                        text = "2x 190,00",
                        description = "",
                        trailing = "Sem acréscimo",
                        interestFree = true,
                        isSelected = false,
                    ),
                ),
                footerState = FooterState(
                    title = "Total",
                    currencySymbol = "R$",
                    amountIntegerPart = "100",
                    amountDecimalPart = "30",
                    subtitle = "Santander Credito **** 1234",
                ),
            ),
            onBackClick = { },
            onItemClick = { Log.i("InstallmentsScreen", "onItemClick: $it") },
        )
    }
}
