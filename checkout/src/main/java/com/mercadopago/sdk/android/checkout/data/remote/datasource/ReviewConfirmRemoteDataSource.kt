package com.mercadopago.sdk.android.checkout.data.remote.datasource

import com.mercadopago.sdk.android.checkout.data.remote.request.ReviewConfirmRequest
import com.mercadopago.sdk.android.checkout.domain.model.ResponseError
import com.mercadopago.sdk.android.checkout.domain.model.ReviewConfirmResponse
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

internal interface ReviewConfirmRemoteDataSource {
    suspend fun fetch(
        clientToken: String,
        reviewConfirmRequest: ReviewConfirmRequest,
    ): Result<ReviewConfirmResponse, ResponseError>
}
