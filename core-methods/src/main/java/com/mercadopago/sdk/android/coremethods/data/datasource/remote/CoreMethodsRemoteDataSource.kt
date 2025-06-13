package com.mercadopago.sdk.android.coremethods.data.datasource.remote

import com.mercadopago.sdk.android.coremethods.data.remote.request.CardIssuersRequest
import com.mercadopago.sdk.android.coremethods.data.remote.request.CardTokenBodyRequest
import com.mercadopago.sdk.android.coremethods.data.remote.request.InstallmentsRequest
import com.mercadopago.sdk.android.coremethods.data.remote.request.PaymentMethodsRequest
import com.mercadopago.sdk.android.coremethods.domain.model.CardIssuer
import com.mercadopago.sdk.android.coremethods.domain.model.CardToken
import com.mercadopago.sdk.android.coremethods.domain.model.IdentificationType
import com.mercadopago.sdk.android.coremethods.domain.model.Installment
import com.mercadopago.sdk.android.coremethods.domain.model.MPResultError
import com.mercadopago.sdk.android.coremethods.domain.model.PaymentMethod
import com.mercadopago.sdk.android.coremethods.domain.utils.MPResult

internal interface CoreMethodsRemoteDataSource {
    suspend fun generateCardToken(request: CardTokenBodyRequest): MPResult<CardToken, MPResultError>

    suspend fun getInstallments(request: InstallmentsRequest): MPResult<List<Installment>, MPResultError>

    suspend fun getIdentificationTypes(): MPResult<List<IdentificationType>, MPResultError>

    suspend fun getCardIssuers(request: CardIssuersRequest): MPResult<List<CardIssuer>, MPResultError>

    suspend fun getPaymentMethods(request: PaymentMethodsRequest): MPResult<List<PaymentMethod>, MPResultError>
}
