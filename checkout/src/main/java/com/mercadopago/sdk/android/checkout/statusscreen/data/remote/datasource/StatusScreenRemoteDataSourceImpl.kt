package com.mercadopago.sdk.android.checkout.statusscreen.data.remote.datasource

import com.mercadopago.sdk.android.checkout.data.remote.mapper.toInternalResponse
import com.mercadopago.sdk.android.checkout.domain.model.ResponseError
import com.mercadopago.sdk.android.checkout.statusscreen.data.remote.request.StatusScreenRequest
import com.mercadopago.sdk.android.checkout.statusscreen.data.remote.response.StatusScreenResponse
import com.mercadopago.sdk.android.checkout.statusscreen.data.remote.service.StatusScreenService
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

private const val BEARER_PREFIX = "Bearer "

internal class StatusScreenRemoteDataSourceImpl(
    private val service: StatusScreenService,
) : StatusScreenRemoteDataSource {
    override suspend fun fetch(
        clientToken: String,
        statusScreenRequest: StatusScreenRequest,
        checkoutType: String?,
    ): Result<StatusScreenResponse, ResponseError> =
        service.fetch(
            clientToken = "$BEARER_PREFIX$clientToken",
            body = statusScreenRequest,
            checkoutType = checkoutType,
        ).toInternalResponse()
}
