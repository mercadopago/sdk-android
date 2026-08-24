package com.mercadopago.sdk.android.checkout.presentation.reviewconfirm

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import com.mercadopago.sdk.android.checkout.presentation.loading.LoadingScreen
import com.mercadopago.sdk.android.checkout.presentation.model.ReviewConfirmFooterSummaryUiModel
import com.mercadopago.sdk.android.checkout.presentation.model.ReviewConfirmFooterUiModel
import com.mercadopago.sdk.android.checkout.presentation.model.ReviewConfirmHeaderUiModel
import com.mercadopago.sdk.android.checkout.presentation.model.ReviewConfirmItemUiModel
import com.mercadopago.sdk.android.checkout.presentation.state.ITEM_TYPE_PAYER_EMAIL
import com.mercadopago.sdk.android.checkout.presentation.state.ReviewConfirmScreenState
import com.mercadopago.sdk.android.checkout.presentation.viewmodel.ReviewConfirmViewModel
import com.mercadopago.sdk.android.components.MPHeader

@Composable
internal fun ReviewConfirmScreen(
    viewModel: ReviewConfirmViewModel,
    onBackClick: () -> Unit = {},
) {
    val state by viewModel.state.collectAsState()

    when (val currentState = state) {
        is ReviewConfirmScreenState.Loading -> {
            BackHandler(enabled = true) { }
            LoadingScreen()
        }

        is ReviewConfirmScreenState.Success -> {
            BackHandler(enabled = currentState.isLoading) { }
            ReviewConfirmSuccessContent(
                header = currentState.header,
                items = currentState.items,
                footerSummary = currentState.footerSummary,
                footer = currentState.footer,
                isLoading = currentState.isLoading,
                onBackClick = {
                    viewModel.onBackPressed()
                    onBackClick()
                },
                onConfirmClick = viewModel::onConfirmClicked,
                onChangeClick = { itemType ->
                    when (itemType) {
                        ITEM_TYPE_PAYER_EMAIL -> viewModel.onModifyEmailClicked()
                        else -> viewModel.onModifyPaymentMethodClicked(itemType)
                    }
                },
            )
        }

        is ReviewConfirmScreenState.Error -> {
            LoadingScreen()
        }
    }
}

@Composable
private fun ReviewConfirmSuccessContent(
    header: ReviewConfirmHeaderUiModel,
    items: List<ReviewConfirmItemUiModel>,
    footerSummary: ReviewConfirmFooterSummaryUiModel?,
    footer: ReviewConfirmFooterUiModel,
    isLoading: Boolean = false,
    onBackClick: () -> Unit = {},
    onConfirmClick: () -> Unit = {},
    onChangeClick: (String) -> Unit = {},
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
            title = header.title,
            onBackClick = onBackClick,
        ) {
            ReviewConfirmContentList(
                header = header,
                items = items,
                footerSummary = null,
                footerHeightDp = footerHeightDp,
                onChangeClick = onChangeClick,
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .onGloballyPositioned { layoutCoordinates ->
                    footerHeightPx = layoutCoordinates.size.height
                },
        ) {
            footerSummary?.let { summary ->
                ReviewConfirmFooterSummarySection(
                    summary = summary,
                )
            }

            ReviewConfirmFixedFooter(
                footer = footer,
                isLoading = isLoading,
                onConfirmClick = onConfirmClick,
            )
        }

        if (isLoading) {
            ReviewConfirmLoadingOverlay()
        }
    }
}
