package com.mercadopago.sdk.android.checkout.data.remote.datasource

import com.mercadopago.sdk.android.checkout.data.remote.mapper.toInternalResponse
import com.mercadopago.sdk.android.checkout.data.remote.request.ReviewConfirmRequest
import com.mercadopago.sdk.android.checkout.data.remote.service.ReviewConfirmService
import com.mercadopago.sdk.android.checkout.domain.model.ResponseError
import com.mercadopago.sdk.android.checkout.domain.model.ReviewConfirmResponse
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

private const val BEARER_PREFIX = "Bearer "

internal class ReviewConfirmRemoteDataSourceImpl(
    private val service: ReviewConfirmService,
) : ReviewConfirmRemoteDataSource {
    override suspend fun fetch(
        clientToken: String,
        reviewConfirmRequest: ReviewConfirmRequest,
        checkoutType: String,
    ): Result<ReviewConfirmResponse, ResponseError> =
        service.fetch(
            clientToken = "$BEARER_PREFIX$clientToken",
            checkoutType = checkoutType,
            body = reviewConfirmRequest,
        ).toInternalResponse()
}
