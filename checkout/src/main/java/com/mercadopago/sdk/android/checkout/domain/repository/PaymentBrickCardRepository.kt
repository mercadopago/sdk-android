package com.mercadopago.sdk.android.checkout.domain.repository

import com.mercadopago.sdk.android.checkout.domain.model.PaymentBrickCardOutput
import com.mercadopago.sdk.android.checkout.domain.model.ResponseError
import com.mercadopago.sdk.android.checkout.domain.model.params.FetchPaymentBrickCardParams
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

internal interface PaymentBrickCardRepository {
    suspend fun fetch(
        params: FetchPaymentBrickCardParams,
    ): Result<PaymentBrickCardOutput, ResponseError>
}
