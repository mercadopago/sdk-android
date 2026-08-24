package com.mercadopago.sdk.android.checkout.data.repository

import com.mercadopago.sdk.android.checkout.core.model.MPSellerInfo
import com.mercadopago.sdk.android.checkout.data.remote.datasource.ReviewConfirmRemoteDataSource
import com.mercadopago.sdk.android.checkout.data.remote.mapper.toViewData
import com.mercadopago.sdk.android.checkout.data.remote.request.ReviewConfirmRequest
import com.mercadopago.sdk.android.checkout.data.remote.request.SellerInfoRequest
import com.mercadopago.sdk.android.checkout.domain.extensions.map
import com.mercadopago.sdk.android.checkout.domain.extensions.withErrorHandling
import com.mercadopago.sdk.android.checkout.domain.model.ResponseError
import com.mercadopago.sdk.android.checkout.domain.model.ReviewConfirmViewData
import com.mercadopago.sdk.android.checkout.domain.model.params.ProcessOrderParams
import com.mercadopago.sdk.android.checkout.domain.repository.ReviewConfirmRepository
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

internal class ReviewConfirmRepositoryImpl(
    private val dataSource: ReviewConfirmRemoteDataSource,
) : ReviewConfirmRepository {
    override suspend fun fetchReviewConfirm(
        processOrderParams: ProcessOrderParams,
        emailChangeEnabled: Boolean,
        sellerInfo: MPSellerInfo?,
    ): Result<ReviewConfirmViewData, ResponseError> =
        withErrorHandling {
            dataSource.fetch(
                clientToken = processOrderParams.clientToken,
                reviewConfirmRequest = ReviewConfirmRequest(
                    orderId = processOrderParams.orderId,
                    paymentMethodType = processOrderParams.paymentMethodType,
                    paymentMethodId = processOrderParams.paymentMethodId,
                    issuerId = processOrderParams.issuerId,
                    bin = processOrderParams.bin,
                    lastFourDigits = processOrderParams.lastFourDigits,
                    installments = processOrderParams.installments,
                    installmentAmount = processOrderParams.installmentAmount,
                    emailChangeEnabled = emailChangeEnabled,
                    sellerInfo = sellerInfo?.let {
                        SellerInfoRequest(
                            name = it.name,
                            iconUrl = it.logoUrl,
                        )
                    },
                ),
            )
        }.map { it.toViewData() }
}
