package com.mercadopago.sdk.android.checkout.presentation.paymentbrick

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import com.mercadopago.android.sdk.checkout.R
import com.mercadopago.sdk.android.checkout.presentation.state.PaymentBrickFooterState
import com.mercadopago.sdk.android.checkout.presentation.state.PaymentBrickScreenState
import com.mercadopago.sdk.android.checkout.presentation.state.PaymentOptionState
import com.mercadopago.sdk.android.checkout.presentation.state.PaymentSectionState
import com.mercadopago.sdk.android.checkout.presentation.viewmodel.PaymentBrickViewModel
import com.mercadopago.sdk.android.components.MPAmountData
import com.mercadopago.sdk.android.components.MPFixedFooter
import com.mercadopago.sdk.android.components.MPFixedFooterButtonData
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
    onOptionSelected: (String) -> Unit = {},
) {
    val viewState by viewModel.viewState.collectAsState()

    BackHandler { viewModel.onBackPressed() }

    PaymentBrickScreenContent(
        viewState = viewState,
        onOptionSelected = onOptionSelected,
        onBackPressed = viewModel::onBackPressed,
    )
}

@Composable
internal fun PaymentBrickScreenContent(
    viewState: PaymentBrickScreenState,
    onOptionSelected: (String) -> Unit = {},
    onBackPressed: () -> Unit = {},
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
                MPFixedFooter(
                    title = "Total",
                    amount = MPAmountData(
                        currencySymbol = footer.currencySymbol,
                        integerPart = footer.amountInteger,
                        decimalPart = footer.amountDecimal,
                    ),
                    button = MPFixedFooterButtonData(
                        text = footer.buttonLabel,
                        onClick = {},
                    ),
                )
            }
        }
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
                    leftImage = if (option.thumbnailIcon != null) {
                        MPListItemLeading.Icon(icon = option.thumbnailIcon)
                    } else {
                        MPListItemLeading.Thumbnail(url = option.thumbnailUrl.orEmpty())
                    },
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
                                thumbnailIcon = ImageVector.vectorResource(R.drawable.ic_mercadopago),
                            ),
                            PaymentOptionState(
                                id = "credit_line",
                                title = "Linha de Crédito",
                                thumbnailIcon = ImageVector.vectorResource(R.drawable.ic_mercadopago),
                            ),
                        ),
                    ),
                    PaymentSectionState(
                        title = "Outros meios de pagamento",
                        options = listOf(
                            PaymentOptionState(
                                id = "pix",
                                title = "Pix",
                                thumbnailIcon = ImageVector.vectorResource(R.drawable.ic_pix),
                            ),
                            PaymentOptionState(
                                id = "boleto",
                                title = "Boleto",
                                thumbnailIcon = ImageVector.vectorResource(R.drawable.ic_bill_payment),
                            ),
                            PaymentOptionState(
                                id = "new_card",
                                title = "Novo cartão",
                                description = "Crédito ou pré-pago",
                                thumbnailIcon = ImageVector.vectorResource(R.drawable.ic_add_card),
                            ),
                        ),
                    ),
                ),
                footerState = PaymentBrickFooterState(
                    currencySymbol = "R$",
                    amountInteger = "500",
                    amountDecimal = "00",
                    buttonLabel = "Continuar",
                ),
            ),
        )
    }
}
