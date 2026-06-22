package com.mercadopago.sdk.android.checkout.data.remote.datasource

import com.mercadopago.sdk.android.checkout.data.remote.response.PaymentBrickCardResponse
import com.mercadopago.sdk.android.checkout.domain.model.ResponseError
import com.mercadopago.sdk.android.checkout.domain.model.params.FetchPaymentBrickCardParams
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

internal interface PaymentBrickCardRemoteDataSource {
    suspend fun fetch(
        params: FetchPaymentBrickCardParams,
    ): Result<PaymentBrickCardResponse, ResponseError>
}
