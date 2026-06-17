package com.mercadopago.sdk.android.checkout.presentation.paymentbrick

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.sharp.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mercadopago.sdk.android.checkout.presentation.loading.LoadingScreen
import com.mercadopago.sdk.android.checkout.presentation.state.PaymentBrickFooterState
import com.mercadopago.sdk.android.checkout.presentation.state.PaymentBrickScreenState
import com.mercadopago.sdk.android.checkout.presentation.state.PaymentOptionState
import com.mercadopago.sdk.android.checkout.presentation.state.PaymentSectionState
import com.mercadopago.sdk.android.checkout.presentation.viewmodel.PaymentBrickViewModel
import com.mercadopago.sdk.android.components.MPHeader
import com.mercadopago.sdk.android.components.MPListItem
import com.mercadopago.sdk.android.components.MPText
import com.mercadopago.sdk.android.components.model.MPListItemContentInfo
import com.mercadopago.sdk.android.components.model.MPListItemLeading
import com.mercadopago.sdk.android.components.model.MPListItemTrailing
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme

private const val PAYMENT_BRICK_GROUP = "PAYMENT_BRICK"

@Composable
internal fun PaymentBrickScreen(
    viewModel: PaymentBrickViewModel,
) {
    val viewState by viewModel.viewState.collectAsState()

    BackHandler { viewModel.onBackPressed() }

    PaymentBrickScreenContent(
        viewState = viewState,
        onOptionSelected = viewModel::onOptionSelected,
        onBackPressed = viewModel::onBackPressed,
    )
}

@Composable
internal fun PaymentBrickScreenContent(
    viewState: PaymentBrickScreenState,
    onOptionSelected: (String) -> Unit = {},
    onBackPressed: () -> Unit = {},
) {
    when {
        viewState.isLoading -> LoadingScreen()
        viewState.isError -> PaymentBrickErrorContent(onBackPressed = onBackPressed)
        else -> PaymentBrickMainContent(
            viewState = viewState,
            onOptionSelected = onOptionSelected,
            onBackPressed = onBackPressed,
        )
    }
}

@Composable
private fun PaymentBrickMainContent(
    viewState: PaymentBrickScreenState,
    onOptionSelected: (String) -> Unit,
    onBackPressed: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
            ) {
                MPHeader(
                    modifier = Modifier.fillMaxSize(),
                    title = viewState.title,
                    onBackClick = onBackPressed,
                ) {
                    Spacer(modifier = Modifier.size(MercadoPagoTheme.spacing.paddings.xsmall))
                    PaymentSectionsList(
                        sections = viewState.sections,
                        onOptionClick = onOptionSelected,
                    )
                }
            }
            viewState.footerState?.let { footer ->
                PaymentBrickFooter(footer = footer)
            }
        }
    }
}

@Composable
private fun PaymentBrickErrorContent(
    onBackPressed: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
    ) {
        MPHeader(
            modifier = Modifier.fillMaxWidth(),
            title = "",
            onBackClick = onBackPressed,
            content = {},
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            MPText(
                text = "Ocurrió un error. Por favor, intentá de nuevo.",
                style = MercadoPagoTheme.typography.body.default.medium,
                color = MercadoPagoTheme.color.text.primary,
                modifier = Modifier.padding(horizontal = MercadoPagoTheme.spacing.paddings.xtiny),
            )
        }
    }
}

@Composable
private fun PaymentBrickFooter(
    footer: PaymentBrickFooterState,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = MercadoPagoTheme.spacing.paddings.xtiny,
                vertical = MercadoPagoTheme.spacing.paddings.xsmall,
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        MPText(
            text = footer.totalLabel,
            style = MercadoPagoTheme.typography.body.default.medium,
            color = MercadoPagoTheme.color.text.secondary,
        )
        MPText(
            text = footer.totalAmount,
            style = MercadoPagoTheme.typography.body.emphasis.medium,
            color = MercadoPagoTheme.color.text.primary,
        )
    }
}

@Composable
private fun PaymentSectionsList(
    sections: List<PaymentSectionState>,
    onOptionClick: (String) -> Unit,
) {
    Column {
        sections.forEach { section ->
            SectionTitle(section.title)
            section.options.forEach { option ->
                MPListItem(
                    contentInfo = MPListItemContentInfo(
                        title = option.title,
                        description = option.description,
                    ),
                    leftImage = MPListItemLeading.Thumbnail(url = option.thumbnailUrl.orEmpty()),
                    trailing = MPListItemTrailing(
                        type = MPListItemTrailing.Type.Icon(Icons.AutoMirrored.Sharp.KeyboardArrowRight),
                    ),
                    onClick = { onOptionClick(option.id) },
                )
            }
        }
    }
}

@Composable
private fun SectionTitle(
    title: String,
) {
    MPText(
        text = title,
        style = MercadoPagoTheme.typography.heading.default.medium,
        color = MercadoPagoTheme.color.text.primary,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = MercadoPagoTheme.spacing.paddings.xtiny,
                vertical = MercadoPagoTheme.spacing.paddings.xmicro,
            ),
    )
}

@Preview(name = "PaymentBrick Screen", group = PAYMENT_BRICK_GROUP, showBackground = true)
@Composable
private fun PaymentBrickScreenContentPreview() {
    MercadoPagoTheme {
        PaymentBrickScreenContent(
            viewState = PaymentBrickScreenState(
                title = "Escolha como pagar",
                sections = listOf(
                    PaymentSectionState(
                        title = "Mercado Pago",
                        options = listOf(
                            PaymentOptionState(
                                id = "mp_balance",
                                title = "Saldo em conta ou cartões salvos",
                                thumbnailUrl = "",
                            ),
                            PaymentOptionState(
                                id = "credit_line",
                                title = "Linha de Crédito",
                                thumbnailUrl = "",
                            ),
                        ),
                    ),
                    PaymentSectionState(
                        title = "Outros meios de pagamento",
                        options = listOf(
                            PaymentOptionState(
                                id = "pix",
                                title = "Pix",
                                thumbnailUrl = "",
                            ),
                            PaymentOptionState(
                                id = "boleto",
                                title = "Boleto",
                                thumbnailUrl = "",
                            ),
                            PaymentOptionState(
                                id = "new_card",
                                title = "Novo cartão",
                                description = "Crédito ou pré-pago",
                                thumbnailUrl = "",
                            ),
                        ),
                    ),
                ),
                footerState = PaymentBrickFooterState(
                    totalLabel = "Total",
                    totalAmount = "$ 500,00",
                ),
            ),
        )
    }
}

@Preview(name = "PaymentBrick Loading", group = PAYMENT_BRICK_GROUP, showBackground = true)
@Composable
private fun PaymentBrickLoadingPreview() {
    MercadoPagoTheme {
        PaymentBrickScreenContent(viewState = PaymentBrickScreenState(isLoading = true))
    }
}

@Preview(name = "PaymentBrick Error", group = PAYMENT_BRICK_GROUP, showBackground = true)
@Composable
private fun PaymentBrickErrorPreview() {
    MercadoPagoTheme {
        PaymentBrickScreenContent(viewState = PaymentBrickScreenState(isError = true))
    }
}
