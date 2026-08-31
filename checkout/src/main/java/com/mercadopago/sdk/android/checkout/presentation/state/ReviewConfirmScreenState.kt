package com.mercadopago.sdk.android.checkout.presentation.state

import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError
import com.mercadopago.sdk.android.checkout.presentation.model.ReviewConfirmFooterSummaryUiModel
import com.mercadopago.sdk.android.checkout.presentation.model.ReviewConfirmFooterUiModel
import com.mercadopago.sdk.android.checkout.presentation.model.ReviewConfirmHeaderUiModel
import com.mercadopago.sdk.android.checkout.presentation.model.ReviewConfirmItemUiModel

internal sealed interface ReviewConfirmScreenState {
    data object Loading : ReviewConfirmScreenState

    data class Success(
        val header: ReviewConfirmHeaderUiModel,
        val items: List<ReviewConfirmItemUiModel>,
        val footerSummary: ReviewConfirmFooterSummaryUiModel?,
        val footer: ReviewConfirmFooterUiModel,
        val isLoading: Boolean = false,
    ) : ReviewConfirmScreenState

    data class Error(val error: MercadoPagoCheckoutError) : ReviewConfirmScreenState
}
