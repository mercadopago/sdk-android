package com.mercadopago.sdk.android.checkout.domain.usecase

import com.mercadopago.sdk.android.checkout.domain.exception.ErrorLocalized
import com.mercadopago.sdk.android.checkout.domain.exception.ExceptionFactory.mapError
import com.mercadopago.sdk.android.checkout.domain.extensions.withServiceRetry
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError
import com.mercadopago.sdk.android.checkout.domain.model.PaymentBrickCardOutput
import com.mercadopago.sdk.android.checkout.domain.model.params.FetchPaymentBrickCardParams
import com.mercadopago.sdk.android.checkout.domain.repository.PaymentBrickCardRepository
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

/**
 * Fetches card form configuration for a given order and BIN.
 *
 * Called when the buyer selects "Nueva tarjeta" in the PaymentBrick selector screen.
 * Uses `withServiceRetry` since this is a read-only initialization call.
 */
internal class FetchPaymentBrickCardUseCase(
    private val repository: PaymentBrickCardRepository,
) {
    suspend operator fun invoke(
        params: FetchPaymentBrickCardParams,
    ): Result<PaymentBrickCardOutput, MercadoPagoCheckoutError> =
        withServiceRetry {
            repository.fetch(params = params)
        }.mapError(ErrorLocalized.PAYMENT_BRICK_CARD)
}
