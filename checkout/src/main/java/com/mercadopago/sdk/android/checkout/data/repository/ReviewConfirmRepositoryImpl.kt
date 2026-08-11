package com.mercadopago.sdk.android.checkout.data.repository

import com.mercadopago.sdk.android.checkout.data.remote.datasource.ReviewConfirmRemoteDataSource
import com.mercadopago.sdk.android.checkout.data.remote.mapper.toViewData
import com.mercadopago.sdk.android.checkout.domain.extensions.map
import com.mercadopago.sdk.android.checkout.domain.extensions.withErrorHandling
import com.mercadopago.sdk.android.checkout.domain.model.ResponseError
import com.mercadopago.sdk.android.checkout.domain.model.ReviewConfirmRequest
import com.mercadopago.sdk.android.checkout.domain.model.ReviewConfirmViewData
import com.mercadopago.sdk.android.checkout.domain.repository.ReviewConfirmRepository
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

internal class ReviewConfirmRepositoryImpl(
    private val dataSource: ReviewConfirmRemoteDataSource,
    private val clientToken: String,
) : ReviewConfirmRepository {
    override suspend fun fetchReviewConfirm(
        request: ReviewConfirmRequest,
    ): Result<ReviewConfirmViewData, ResponseError> =
        withErrorHandling {
            dataSource.post(clientToken = clientToken, request = request)
        }.map { it.toViewData() }
}
