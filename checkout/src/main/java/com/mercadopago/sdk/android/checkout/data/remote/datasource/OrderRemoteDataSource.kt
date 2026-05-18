package com.mercadopago.sdk.android.checkout.data.remote.datasource

import com.mercadopago.sdk.android.checkout.domain.model.ResponseError
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

internal interface OrderRemoteDataSource {
    suspend fun process(
        orderId: String,
    ): Result<Unit, ResponseError>

    suspend fun transactions(
        orderId: String,
    ): Result<Unit, ResponseError>
}
