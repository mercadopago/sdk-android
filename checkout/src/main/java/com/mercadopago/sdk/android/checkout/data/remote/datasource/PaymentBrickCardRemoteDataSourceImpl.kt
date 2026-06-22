package com.mercadopago.sdk.android.checkout.data.remote.datasource

import com.mercadopago.sdk.android.checkout.data.remote.mapper.toInternalResponse
import com.mercadopago.sdk.android.checkout.data.remote.response.PaymentBrickCardResponse
import com.mercadopago.sdk.android.checkout.data.remote.service.PaymentBrickCardService
import com.mercadopago.sdk.android.checkout.domain.model.ResponseError
import com.mercadopago.sdk.android.checkout.domain.model.params.FetchPaymentBrickCardParams
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

internal class PaymentBrickCardRemoteDataSourceImpl(
    private val service: PaymentBrickCardService,
) : PaymentBrickCardRemoteDataSource {
    override suspend fun fetch(
        params: FetchPaymentBrickCardParams,
    ): Result<PaymentBrickCardResponse, ResponseError> =
        service.fetch(
            orderId = params.orderId,
            bin = params.bin,
        ).toInternalResponse()
}
