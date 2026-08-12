package com.mercadopago.sdk.android.checkout.presentation.reviewconfirm

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.platform.LocalDensity
import com.mercadopago.sdk.android.checkout.domain.model.ReviewConfirmFooter
import com.mercadopago.sdk.android.checkout.domain.model.ReviewConfirmFooterSummary
import com.mercadopago.sdk.android.checkout.domain.model.ReviewConfirmHeader
import com.mercadopago.sdk.android.checkout.domain.model.ReviewConfirmItem
import com.mercadopago.sdk.android.checkout.domain.model.ReviewConfirmScreenState
import com.mercadopago.sdk.android.checkout.presentation.loading.LoadingScreen
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
                onBackClick = onBackClick,
                onConfirmClick = viewModel::onConfirmClicked,
                onChangeClick = {},
            )
        }

        is ReviewConfirmScreenState.Error -> {
            LoadingScreen()
        }
    }
}

@Composable
private fun ReviewConfirmSuccessContent(
    header: ReviewConfirmHeader,
    items: List<ReviewConfirmItem>,
    footerSummary: ReviewConfirmFooterSummary?,
    footer: ReviewConfirmFooter,
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
                footerSummary = footerSummary,
                footerHeightDp = footerHeightDp,
                onChangeClick = onChangeClick,
            )
        }

        ReviewConfirmFixedFooter(
            footer = footer,
            isLoading = isLoading,
            onConfirmClick = onConfirmClick,
            onFooterPositioned = { footerHeightPx = it },
            modifier = Modifier.align(Alignment.BottomCenter),
        )

        if (isLoading) {
            ReviewConfirmLoadingOverlay()
        }
    }
}
