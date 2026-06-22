package com.mercadopago.sdk.android.checkout.presentation.state

import com.mercadopago.sdk.android.checkout.domain.model.MPInstallmentData

/**
 * One-shot events emitted by [com.mercadopago.sdk.android.checkout.presentation.viewmodel.NewCardViewModel].
 *
 * Consumed by `CheckoutController` (when A20 is unblocked) to trigger navigation.
 */
internal sealed interface NewCardViewEvent {
    /**
     * Card data loaded successfully and installments are available — navigate to installment selector.
     *
     * @property installmentData Installment options to display.
     */
    data class NavigateToInstallments(
        val installmentData: MPInstallmentData,
    ) : NewCardViewEvent
}
