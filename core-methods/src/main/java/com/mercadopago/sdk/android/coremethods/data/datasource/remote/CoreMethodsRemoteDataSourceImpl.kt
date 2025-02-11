package com.mercadopago.sdk.android.coremethods.data.datasource.remote

import com.mercadopago.sdk.android.coremethods.data.datasource.mappers.toResultError
import com.mercadopago.sdk.android.coremethods.data.remote.mappers.toModel
import com.mercadopago.sdk.android.coremethods.data.remote.request.CardTokenBodyRequest
import com.mercadopago.sdk.android.coremethods.data.remote.request.InstallmentsRequest
import com.mercadopago.sdk.android.coremethods.data.remote.service.CoreMethodsService
import com.mercadopago.sdk.android.coremethods.domain.model.CardToken
import com.mercadopago.sdk.android.coremethods.domain.model.Installment
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

internal class CoreMethodsRemoteDataSourceImpl(
    private val service: CoreMethodsService
) : CoreMethodsRemoteDataSource {

    @Suppress("ReturnCount")
    override suspend fun generateCardToken(
        cardTokenRequest: CardTokenBodyRequest
    ): Result<CardToken, ResultError> {
        val result = service.createToken(cardTokenRequest)
        return when (result.isSuccessful) {
            true -> {
                val body = result.body() ?: return Result.Error(ResultError(message = "empty body"))
                val id =
                    body.id ?: return Result.Error(ResultError(message = "no token identification"))
                Result.Success(CardToken(id))
            }

            false -> {
                Result.Error(
                    error = result.errorBody().toResultError()
                )
            }
        }
    }

    override suspend fun getInstallments(installmentsRequest: InstallmentsRequest): Result<Installment, ResultError> {
        val result = service.getInstallments(
            productId = installmentsRequest.productId,
            bin = installmentsRequest.bin,
            processingMode = installmentsRequest.processingMode,
            amount = installmentsRequest.amount
        )

        return when (result.isSuccessful) {
            true -> {
                val body = result.body() ?: return Result.Error(ResultError(message = "empty body"))
                Result.Success(body.toModel())
            }

            false -> {
                Result.Error(
                    error = result.errorBody().toResultError()
                )
            }
        }
    }
}
