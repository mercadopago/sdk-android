package com.mercadopago.sdk.android.checkout.data.remote.datasource

import com.mercadopago.sdk.android.checkout.data.remote.response.PaymentBrickInitializationResponse
import com.mercadopago.sdk.android.checkout.domain.model.ResponseError
import com.mercadopago.sdk.android.checkout.domain.model.params.FetchPaymentBrickInitializationParams
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

internal interface PaymentBrickInitializationRemoteDataSource {
    suspend fun fetch(
        params: FetchPaymentBrickInitializationParams,
    ): Result<PaymentBrickInitializationResponse, ResponseError>
}
