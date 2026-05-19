package com.mercadopago.sdk.android.checkout.domain.usecase

import com.mercadopago.sdk.android.checkout.data.remote.response.OrderProcessResponse
import com.mercadopago.sdk.android.checkout.domain.exception.ErrorLocalized
import com.mercadopago.sdk.android.checkout.domain.exception.ExceptionFactory.mapError
import com.mercadopago.sdk.android.checkout.domain.extensions.withServiceRetry
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError
import com.mercadopago.sdk.android.checkout.domain.repository.OrderRepository
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

internal class ProcessOrderUseCase(
    private val repository: OrderRepository,
) {
    suspend operator fun invoke(
        orderId: String,
    ): Result<OrderProcessResponse, MercadoPagoCheckoutError> =
        withServiceRetry {
            repository.process(orderId = orderId)
        }.mapError(ErrorLocalized.ORDER_PROCESS)
}
