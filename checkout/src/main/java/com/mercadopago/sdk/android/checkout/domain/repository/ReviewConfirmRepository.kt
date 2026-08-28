package com.mercadopago.sdk.android.checkout.domain.repository

import com.mercadopago.sdk.android.checkout.core.model.MPSellerInfo
import com.mercadopago.sdk.android.checkout.domain.model.ResponseError
import com.mercadopago.sdk.android.checkout.domain.model.ReviewConfirmViewData
import com.mercadopago.sdk.android.checkout.domain.model.params.ProcessOrderParams
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

internal interface ReviewConfirmRepository {
    suspend fun fetchReviewConfirm(
        processOrderParams: ProcessOrderParams,
        emailChangeEnabled: Boolean,
        sellerInfo: MPSellerInfo?,
    ): Result<ReviewConfirmViewData, ResponseError>
}
