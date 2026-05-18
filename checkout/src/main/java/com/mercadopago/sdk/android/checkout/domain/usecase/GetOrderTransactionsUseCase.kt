package com.mercadopago.sdk.android.checkout.domain.usecase

import com.mercadopago.sdk.android.checkout.domain.exception.ErrorLocalized
import com.mercadopago.sdk.android.checkout.domain.exception.ExceptionFactory.mapError
import com.mercadopago.sdk.android.checkout.domain.extensions.withServiceRetry
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError
import com.mercadopago.sdk.android.checkout.domain.repository.OrderRepository
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

internal class GetOrderTransactionsUseCase(
    private val repository: OrderRepository,
) {
    suspend operator fun invoke(
        orderId: String,
    ): Result<Unit, MercadoPagoCheckoutError> =
        withServiceRetry {
            repository.transactions(orderId = orderId)
        }.mapError(ErrorLocalized.ORDER_TRANSACTIONS)
}
