package com.mercadopago.sdk.android.checkout.domain.repository

import com.mercadopago.sdk.android.checkout.domain.model.PaymentBrickInitializationOutput
import com.mercadopago.sdk.android.checkout.domain.model.ResponseError
import com.mercadopago.sdk.android.checkout.domain.model.params.FetchPaymentBrickInitializationParams
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

internal interface PaymentBrickInitializationRepository {
    suspend fun fetch(
        params: FetchPaymentBrickInitializationParams,
    ): Result<PaymentBrickInitializationOutput, ResponseError>
}
