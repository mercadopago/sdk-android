package com.mercadopago.sdk.android.checkout.presentation.reviewconfirm

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import com.mercadopago.sdk.android.checkout.domain.model.FooterSummaryRow
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError
import com.mercadopago.sdk.android.checkout.domain.model.ReviewConfirmFooter
import com.mercadopago.sdk.android.checkout.domain.model.ReviewConfirmFooterSummary
import com.mercadopago.sdk.android.checkout.domain.model.ReviewConfirmHeader
import com.mercadopago.sdk.android.checkout.domain.model.ReviewConfirmItem
import com.mercadopago.sdk.android.checkout.domain.model.ReviewConfirmScreenState
import com.mercadopago.sdk.android.checkout.presentation.loading.LoadingScreen
import com.mercadopago.sdk.android.checkout.presentation.viewmodel.ReviewConfirmViewModel
import com.mercadopago.sdk.android.components.MPAmountData
import com.mercadopago.sdk.android.components.MPFixedFooter
import com.mercadopago.sdk.android.components.MPFixedFooterButtonData
import com.mercadopago.sdk.android.components.MPHeader
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoThemes

@Composable
internal fun ReviewConfirmScreen(
    viewModel: ReviewConfirmViewModel,
    onBackClick: () -> Unit,
    onConfirmClick: () -> Unit,
    onError: (MercadoPagoCheckoutError) -> Unit = {},
    onItemChangeClick: (ReviewConfirmItem) -> Unit = {},
) {
    val state by viewModel.state.collectAsState()

    BackHandler { onBackClick() }

    LaunchedEffect(state) {
        if (state is ReviewConfirmScreenState.Error) {
            onError((state as ReviewConfirmScreenState.Error).error)
        }
    }

    ReviewConfirmScreenContent(
        state = state,
        onBackClick = onBackClick,
        onConfirmClick = onConfirmClick,
        onItemChangeClick = onItemChangeClick,
    )
}

@Composable
internal fun ReviewConfirmScreenContent(
    state: ReviewConfirmScreenState,
    onBackClick: () -> Unit = {},
    onConfirmClick: () -> Unit = {},
    onItemChangeClick: (ReviewConfirmItem) -> Unit = {},
) {
    when (state) {
        is ReviewConfirmScreenState.Loading -> LoadingScreen()
        is ReviewConfirmScreenState.Success -> ReviewConfirmSuccessContent(
            state = state,
            onBackClick = onBackClick,
            onConfirmClick = onConfirmClick,
            onItemChangeClick = onItemChangeClick,
        )
        is ReviewConfirmScreenState.Error -> LoadingScreen()
    }
}

@Composable
private fun ReviewConfirmSuccessContent(
    state: ReviewConfirmScreenState.Success,
    onBackClick: () -> Unit = {},
    onConfirmClick: () -> Unit = {},
    onItemChangeClick: (ReviewConfirmItem) -> Unit = {},
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
            title = state.header.title,
            subtitle = state.header.sellerName.orEmpty(),
            onBackClick = onBackClick,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = MercadoPagoTheme.spacing.paddings.xtiny,
                        vertical = MercadoPagoTheme.spacing.paddings.xmicro,
                    )
                    .padding(bottom = footerHeightDp),
                verticalArrangement = Arrangement.spacedBy(MercadoPagoTheme.spacing.paddings.xnano),
            ) {
                state.items.forEach { item ->
                    ReviewConfirmItemRow(
                        item = item,
                        onChangeClick = { onItemChangeClick(item) },
                    )
                }
                state.footerSummary?.let { summary ->
                    ReviewConfirmSummarySection(summary = summary)
                }
            }
        }

        ReviewConfirmFooterSection(
            footer = state.footer,
            onConfirmClick = onConfirmClick,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .onGloballyPositioned { footerHeightPx = it.size.height },
        )
    }
}

@Composable
private fun ReviewConfirmFooterSection(
    footer: ReviewConfirmFooter,
    onConfirmClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    MPFixedFooter(
        title = footer.description.orEmpty(),
        amount = MPAmountData(
            currencySymbol = "",
            integerPart = footer.totalAmount,
            decimalPart = "",
        ),
        subtitle = footer.interestLabel,
        button = MPFixedFooterButtonData(
            text = footer.buttonLabel,
            onClick = onConfirmClick,
        ),
        modifier = modifier,
    )
}

@Preview(showBackground = true, name = "ReviewConfirm — Loading")
@Composable
private fun ReviewConfirmLoadingPreview() {
    MercadoPagoTheme(theme = MercadoPagoThemes.Default) {
        ReviewConfirmScreenContent(state = ReviewConfirmScreenState.Loading)
    }
}

@Preview(showBackground = true, name = "ReviewConfirm — Success")
@Composable
private fun ReviewConfirmSuccessPreview() {
    MercadoPagoTheme(theme = MercadoPagoThemes.Default) {
        ReviewConfirmScreenContent(
            state = ReviewConfirmScreenState.Success(
                header = ReviewConfirmHeader(
                    title = "Revisa e confirma",
                    sellerName = "Loja Exemplo",
                    sellerIconUrl = null,
                ),
                items = listOf(
                    ReviewConfirmItem(
                        type = "payment_method",
                        label = "Forma de pagamento",
                        value = "Visa •••• 1234",
                        changeLabel = "Mudar",
                    ),
                    ReviewConfirmItem(
                        type = "installments",
                        label = "Parcelas",
                        value = "3x R$ 166,67 sem juros",
                        changeLabel = null,
                    ),
                ),
                footerSummary = ReviewConfirmFooterSummary(
                    products = listOf(FooterSummaryRow(label = "Produtos", amount = "R$ 500,00")),
                    coupon = FooterSummaryRow(label = "Cupom", amount = "- R$ 0,00"),
                    interest = null,
                ),
                footer = ReviewConfirmFooter(
                    buttonLabel = "Confirmar e pagar",
                    totalAmount = "R$ 500,00",
                    description = "Total",
                    interestLabel = null,
                ),
            ),
        )
    }
}
