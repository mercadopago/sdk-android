package com.mercadopago.sdk.android.coremethods.data.datasource.remote

import com.mercadopago.sdk.android.coremethods.data.datasource.mappers.mapSuccess
import com.mercadopago.sdk.android.coremethods.data.datasource.mappers.toInternalResponse
import com.mercadopago.sdk.android.coremethods.data.datasource.remote.mapper.toModel
import com.mercadopago.sdk.android.coremethods.data.remote.mappers.toModel
import com.mercadopago.sdk.android.coremethods.data.remote.request.CardIssuersRequest
import com.mercadopago.sdk.android.coremethods.data.remote.request.CardTokenBodyRequest
import com.mercadopago.sdk.android.coremethods.data.remote.request.InstallmentsRequest
import com.mercadopago.sdk.android.coremethods.data.remote.request.PaymentMethodsRequest
import com.mercadopago.sdk.android.coremethods.data.remote.service.CoreMethodsService
import com.mercadopago.sdk.android.coremethods.domain.model.CardIssuer
import com.mercadopago.sdk.android.coremethods.domain.model.CardToken
import com.mercadopago.sdk.android.coremethods.domain.model.IdentificationType
import com.mercadopago.sdk.android.coremethods.domain.model.Installment
import com.mercadopago.sdk.android.coremethods.domain.model.MPError
import com.mercadopago.sdk.android.coremethods.domain.model.PaymentMethod
import com.mercadopago.sdk.android.coremethods.domain.utils.MPResult

internal class CoreMethodsRemoteDataSourceImpl(
    private val service: CoreMethodsService,
) : CoreMethodsRemoteDataSource {

    override suspend fun generateCardToken(
        request: CardTokenBodyRequest
    ): MPResult<CardToken, MPError> {
        return service.createToken(request).toInternalResponse().mapSuccess {
            this.toModel()
        }
    }

    override suspend fun getInstallments(
        request: InstallmentsRequest
    ): MPResult<List<Installment>, MPError> {
        return service.getInstallments(
            bin = request.bin,
            processingMode = request.processingMode,
            amount = request.amount,
        ).toInternalResponse().mapSuccess { this.map { it.toModel() } }
    }

    override suspend fun getIdentificationTypes(): MPResult<List<IdentificationType>, MPError> {
        return service.getIdentificationTypes().toInternalResponse().mapSuccess {
            this.map { it.toModel() }
        }
    }

    override suspend fun getCardIssuers(
        request: CardIssuersRequest
    ): MPResult<List<CardIssuer>, MPError> {
        return service.getCardIssuers(
            bin = request.bin,
            paymentMethodId = request.paymentMethodId,
        ).toInternalResponse().mapSuccess { this.map { it.toModel() } }
    }

    override suspend fun getPaymentMethods(
        request: PaymentMethodsRequest
    ): MPResult<List<PaymentMethod>, MPError> {
        return service.getPaymentMethods(
            bin = request.bin,
        ).toInternalResponse().mapSuccess { this.map { it.toModel() } }
    }
}
