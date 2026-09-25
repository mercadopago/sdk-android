package com.mercadopago.sdk.android.checkout.domain.usecase

import com.mercadopago.sdk.android.checkout.domain.exception.ErrorLocalized
import com.mercadopago.sdk.android.checkout.domain.exception.ObservedCheckoutErrorFactory.mapResponseObserved
import com.mercadopago.sdk.android.checkout.domain.extensions.withErrorHandling
import com.mercadopago.sdk.android.checkout.domain.model.ObservedCheckoutError
import com.mercadopago.sdk.android.checkout.domain.model.OrderProcessOutput
import com.mercadopago.sdk.android.checkout.domain.model.params.ProcessOrderParams
import com.mercadopago.sdk.android.checkout.domain.repository.OrderRepository
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

internal class ProcessOrderUseCase(
    private val repository: OrderRepository,
) {
    suspend operator fun invoke(
        params: ProcessOrderParams,
    ): Result<OrderProcessOutput, ObservedCheckoutError> =
        withErrorHandling {
            repository.process(params = params)
        }.mapResponseObserved(ErrorLocalized.ORDER_PROCESS)
}
