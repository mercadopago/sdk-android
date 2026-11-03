package com.mercadopago.sdk.android.checkout.statusscreen.data.repository

import com.mercadopago.sdk.android.checkout.core.model.MPSellerInfo
import com.mercadopago.sdk.android.checkout.data.remote.request.SellerInfoRequest
import com.mercadopago.sdk.android.checkout.domain.extensions.flatMap
import com.mercadopago.sdk.android.checkout.domain.extensions.withErrorHandling
import com.mercadopago.sdk.android.checkout.domain.model.ResponseError
import com.mercadopago.sdk.android.checkout.statusscreen.data.remote.datasource.StatusScreenRemoteDataSource
import com.mercadopago.sdk.android.checkout.statusscreen.data.remote.mapper.toViewDataOrNull
import com.mercadopago.sdk.android.checkout.statusscreen.data.remote.request.StatusScreenRequest
import com.mercadopago.sdk.android.checkout.statusscreen.data.remote.request.asLastFourDigitsOrNull
import com.mercadopago.sdk.android.checkout.statusscreen.data.remote.response.StatusScreenResponse
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.StatusScreenViewData
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.invalidStatusScreenResponseError
import com.mercadopago.sdk.android.checkout.statusscreen.domain.repository.StatusScreenRepository
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

internal class StatusScreenRepositoryImpl(
    private val dataSource: StatusScreenRemoteDataSource,
) : StatusScreenRepository {
    override suspend fun getStatusScreen(
        clientToken: String,
        orderId: String,
        lastFourDigits: String?,
        sellerInfo: MPSellerInfo?,
        checkoutType: String?,
    ): Result<StatusScreenViewData, ResponseError> =
        withErrorHandling {
            dataSource.fetch(
                clientToken = clientToken,
                statusScreenRequest = StatusScreenRequest(
                    orderId = orderId,
                    lastFourDigits = lastFourDigits.asLastFourDigitsOrNull(),
                    sellerInfo = sellerInfo?.toSellerInfoRequest(),
                ),
                checkoutType = checkoutType,
            )
        }.flatMap { it.toResultOrInvalidResponseError() }
}

private fun MPSellerInfo.toSellerInfoRequest(): SellerInfoRequest = SellerInfoRequest(name = name, iconUrl = logoUrl)

private fun StatusScreenResponse.toResultOrInvalidResponseError(): Result<StatusScreenViewData, ResponseError> =
    toViewDataOrNull()?.let { Result.Success(it) }
        ?: Result.Error(invalidStatusScreenResponseError())
