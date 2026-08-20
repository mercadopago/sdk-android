package com.mercadopago.sdk.android.checkout.data.remote.datasource

import com.mercadopago.sdk.android.checkout.data.remote.mapper.toInternalResponse
import com.mercadopago.sdk.android.checkout.data.remote.request.CardBinRequest
import com.mercadopago.sdk.android.checkout.data.remote.response.CardBinResponse
import com.mercadopago.sdk.android.checkout.data.remote.response.CardFormInitResponse
import com.mercadopago.sdk.android.checkout.data.remote.service.CardFormService
import com.mercadopago.sdk.android.checkout.domain.model.ResponseError
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

internal class CardFormRemoteDataSourceImpl(
    private val service: CardFormService,
) : CardFormRemoteDataSource {
    override suspend fun fetchInitialization(
        orderId: String?,
        clientToken: String?,
        checkoutType: String,
        screens: String?,
    ): Result<CardFormInitResponse, ResponseError> =
        service.initialization(
            authorization = clientToken?.let { "Bearer $it" },
            orderId = orderId,
            checkoutType = checkoutType,
            screens = screens,
        ).toInternalResponse()

    override suspend fun getCardBin(
        request: CardBinRequest,
    ): Result<CardBinResponse, ResponseError> =
        with(request) {
            service.getCardBin(
                bin = bin,
                amount = amount,
                checkoutType = checkoutType,
                processingMode = processingMode,
                excludedPaymentTypes = excludedPaymentTypes,
                excludedPaymentMethods = excludedPaymentMethods,
            ).toInternalResponse()
        }
}
