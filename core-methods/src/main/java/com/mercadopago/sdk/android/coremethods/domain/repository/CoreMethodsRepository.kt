package com.mercadopago.sdk.android.coremethods.domain.repository

import com.mercadopago.sdk.android.coremethods.domain.model.CardIssuer
import com.mercadopago.sdk.android.coremethods.domain.model.CardToken
import com.mercadopago.sdk.android.coremethods.domain.model.IdentificationType
import com.mercadopago.sdk.android.coremethods.domain.model.Installment
import com.mercadopago.sdk.android.coremethods.domain.model.PaymentMethod
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.model.params.GenerateCardTokenParams
import com.mercadopago.sdk.android.coremethods.domain.model.params.GetCardIssuersParams
import com.mercadopago.sdk.android.coremethods.domain.model.params.GetInstallmentParams
import com.mercadopago.sdk.android.coremethods.domain.model.params.GetPaymentMethodsParams
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

internal interface CoreMethodsRepository {
    suspend fun generateCardToken(
        params: GenerateCardTokenParams,
    ): Result<CardToken, ResultError>

    suspend fun getInstallment(
        params: GetInstallmentParams,
    ): Result<List<Installment>, ResultError>

    suspend fun getIdentificationTypes(): Result<List<IdentificationType>, ResultError>

    suspend fun getCardIssuers(
        params: GetCardIssuersParams,
    ): Result<List<CardIssuer>, ResultError>

    suspend fun getPaymentMethods(
        params: GetPaymentMethodsParams,
    ): Result<List<PaymentMethod>, ResultError>
}
