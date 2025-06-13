package com.mercadopago.sdk.android.coremethods.domain.repository

import com.mercadopago.sdk.android.coremethods.domain.model.CardIssuer
import com.mercadopago.sdk.android.coremethods.domain.model.CardToken
import com.mercadopago.sdk.android.coremethods.domain.model.IdentificationType
import com.mercadopago.sdk.android.coremethods.domain.model.Installment
import com.mercadopago.sdk.android.coremethods.domain.model.MPResultError
import com.mercadopago.sdk.android.coremethods.domain.model.PaymentMethod
import com.mercadopago.sdk.android.coremethods.domain.model.params.GenerateCardTokenParams
import com.mercadopago.sdk.android.coremethods.domain.model.params.GetCardIssuersParams
import com.mercadopago.sdk.android.coremethods.domain.model.params.GetInstallmentParams
import com.mercadopago.sdk.android.coremethods.domain.model.params.GetPaymentMethodsParams
import com.mercadopago.sdk.android.coremethods.domain.utils.MPResult

internal interface CoreMethodsRepository {
    suspend fun generateCardToken(params: GenerateCardTokenParams): MPResult<CardToken, MPResultError>

    suspend fun getInstallment(params: GetInstallmentParams): MPResult<List<Installment>, MPResultError>

    suspend fun getIdentificationTypes(): MPResult<List<IdentificationType>, MPResultError>

    suspend fun getCardIssuers(params: GetCardIssuersParams): MPResult<List<CardIssuer>, MPResultError>

    suspend fun getPaymentMethods(params: GetPaymentMethodsParams): MPResult<List<PaymentMethod>, MPResultError>
}
