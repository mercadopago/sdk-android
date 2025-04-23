package com.mercadopago.sdk.android.coremethods.data.repository

import com.mercadopago.sdk.android.coremethods.data.datasource.remote.CoreMethodsRemoteDataSource
import com.mercadopago.sdk.android.coremethods.data.remote.mappers.toRequest
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
import com.mercadopago.sdk.android.coremethods.domain.repository.CoreMethodsRepository
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

internal class CoreMethodsRepositoryImpl(
    private val dataSource: CoreMethodsRemoteDataSource,
) : CoreMethodsRepository {

    override suspend fun generateCardToken(params: GenerateCardTokenParams): Result<CardToken, ResultError> {
        return dataSource.generateCardToken(params.toRequest())
    }

    override suspend fun getInstallment(params: GetInstallmentParams): Result<List<Installment>, ResultError> {
        return dataSource.getInstallments(params.toRequest())
    }

    override suspend fun getIdentificationTypes(): Result<List<IdentificationType>, ResultError> {
        return dataSource.getIdentificationTypes()
    }

    override suspend fun getCardIssuers(params: GetCardIssuersParams): Result<List<CardIssuer>, ResultError> {
        return dataSource.getCardIssuers(params.toRequest())
    }

    override suspend fun getPaymentMethods(params: GetPaymentMethodsParams): Result<List<PaymentMethod>, ResultError> {
        return dataSource.getPaymentMethods(params.toRequest())
    }
}
