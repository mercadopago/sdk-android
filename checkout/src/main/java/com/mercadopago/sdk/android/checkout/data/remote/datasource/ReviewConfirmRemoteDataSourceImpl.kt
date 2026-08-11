package com.mercadopago.sdk.android.checkout.data.remote.datasource

import com.mercadopago.sdk.android.checkout.data.remote.mapper.toInternalResponse
import com.mercadopago.sdk.android.checkout.data.remote.service.ReviewConfirmService
import com.mercadopago.sdk.android.checkout.domain.model.ResponseError
import com.mercadopago.sdk.android.checkout.domain.model.ReviewConfirmRequest
import com.mercadopago.sdk.android.checkout.domain.model.ReviewConfirmResponse
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

private const val BEARER_PREFIX = "Bearer "

internal class ReviewConfirmRemoteDataSourceImpl(
    private val service: ReviewConfirmService,
) : ReviewConfirmRemoteDataSource {
    override suspend fun post(
        clientToken: String,
        request: ReviewConfirmRequest,
    ): Result<ReviewConfirmResponse, ResponseError> =
        service.post(
            clientToken = "$BEARER_PREFIX$clientToken",
            body = request,
        ).toInternalResponse()
}
