package com.mercadopago.sdk.android.checkout.domain.usecase

import com.mercadopago.sdk.android.checkout.domain.exception.ErrorLocalized
import com.mercadopago.sdk.android.checkout.domain.exception.ExceptionFactory.mapError
import com.mercadopago.sdk.android.checkout.domain.extensions.withServiceRetry
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError
import com.mercadopago.sdk.android.checkout.domain.model.PaymentBrickInitializationOutput
import com.mercadopago.sdk.android.checkout.domain.model.params.FetchPaymentBrickInitializationParams
import com.mercadopago.sdk.android.checkout.domain.repository.PaymentBrickInitializationRepository
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

internal class FetchPaymentBrickInitializationUseCase(
    private val repository: PaymentBrickInitializationRepository,
) {
    suspend operator fun invoke(
        params: FetchPaymentBrickInitializationParams,
    ): Result<PaymentBrickInitializationOutput, MercadoPagoCheckoutError> =
        withServiceRetry {
            repository.fetch(params = params)
        }.mapError(ErrorLocalized.PAYMENT_INITIALIZATION)
}
