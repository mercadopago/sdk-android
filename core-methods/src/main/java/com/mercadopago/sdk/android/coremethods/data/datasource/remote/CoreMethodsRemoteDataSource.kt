package com.mercadopago.sdk.android.coremethods.data.datasource.remote

import com.mercadopago.sdk.android.coremethods.data.remote.request.CardIssuersRequest
import com.mercadopago.sdk.android.coremethods.data.remote.request.CardTokenBodyRequest
import com.mercadopago.sdk.android.coremethods.data.remote.request.InstallmentsRequest
import com.mercadopago.sdk.android.coremethods.data.remote.request.PaymentMethodsRequest
import com.mercadopago.sdk.android.coremethods.domain.model.CardIssuer
import com.mercadopago.sdk.android.coremethods.domain.model.CardToken
import com.mercadopago.sdk.android.coremethods.domain.model.IdentificationType
import com.mercadopago.sdk.android.coremethods.domain.model.Installment
import com.mercadopago.sdk.android.coremethods.domain.model.PaymentMethod
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

internal interface CoreMethodsRemoteDataSource {
    suspend fun generateCardToken(request: CardTokenBodyRequest): Result<CardToken, ResultError>

    suspend fun getInstallments(request: InstallmentsRequest): Result<Installment, ResultError>

    suspend fun getIdentificationTypes(): Result<List<IdentificationType>, ResultError>

    suspend fun getCardIssuers(request: CardIssuersRequest): Result<List<CardIssuer>, ResultError>

    suspend fun getPaymentMethods(request: PaymentMethodsRequest): Result<List<PaymentMethod>, ResultError>
}
