package com.mercadopago.sdk.android.checkout.domain.usecase

import com.mercadopago.sdk.android.checkout.domain.model.MethodSelectionScreenData
import com.mercadopago.sdk.android.checkout.domain.model.PaymentMethodOutput

internal class FetchMethodSelectionScreenUseCase {
    operator fun invoke(
        ticketMethod: PaymentMethodOutput,
    ): MethodSelectionScreenData? = ticketMethod.screen
}
