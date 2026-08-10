package com.mercadopago.sdk.android.checkout.domain.repository

import com.mercadopago.sdk.android.checkout.domain.model.ResponseError
import com.mercadopago.sdk.android.checkout.domain.model.ReviewConfirmRequest
import com.mercadopago.sdk.android.checkout.domain.model.ReviewConfirmViewData
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

internal interface ReviewConfirmRepository {
    suspend fun fetchReviewConfirm(
        request: ReviewConfirmRequest,
    ): Result<ReviewConfirmViewData, ResponseError>
}
