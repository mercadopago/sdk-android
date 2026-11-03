package com.mercadopago.sdk.android.checkout.statusscreen.data.remote.datasource

import com.mercadopago.sdk.android.checkout.domain.model.ResponseError
import com.mercadopago.sdk.android.checkout.statusscreen.data.remote.request.StatusScreenRequest
import com.mercadopago.sdk.android.checkout.statusscreen.data.remote.response.StatusScreenResponse
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

internal interface StatusScreenRemoteDataSource {
    suspend fun fetch(
        clientToken: String,
        statusScreenRequest: StatusScreenRequest,
        checkoutType: String? = null,
    ): Result<StatusScreenResponse, ResponseError>
}
