package com.mercadopago.sdk.android.checkout.domain.model

internal sealed interface ReviewConfirmScreenState {
    data object Loading : ReviewConfirmScreenState

    data class Success(
        val header: ReviewConfirmHeader,
        val items: List<ReviewConfirmItem>,
        val footerSummary: ReviewConfirmFooterSummary?,
        val footer: ReviewConfirmFooter,
        val isLoading: Boolean = false,
    ) : ReviewConfirmScreenState

    data class Error(val error: MercadoPagoCheckoutError) : ReviewConfirmScreenState
}
