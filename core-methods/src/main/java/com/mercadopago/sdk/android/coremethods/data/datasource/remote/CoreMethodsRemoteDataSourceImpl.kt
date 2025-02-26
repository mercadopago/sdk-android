package com.mercadopago.sdk.android.coremethods.data.datasource.remote

import com.mercadopago.sdk.android.coremethods.data.datasource.mappers.toResultError
import com.mercadopago.sdk.android.coremethods.data.remote.request.CardTokenBodyRequest
import com.mercadopago.sdk.android.coremethods.data.remote.request.InstallmentsRequest
import com.mercadopago.sdk.android.coremethods.data.remote.service.CoreMethodsService
import com.mercadopago.sdk.android.coremethods.domain.model.CardToken
import com.mercadopago.sdk.android.coremethods.domain.model.IdentificationTypes
import com.mercadopago.sdk.android.coremethods.domain.model.Installment
import com.mercadopago.sdk.android.coremethods.domain.model.PayerCost
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

internal class CoreMethodsRemoteDataSourceImpl(
    private val service: CoreMethodsService,
) : CoreMethodsRemoteDataSource {
    @Suppress("ReturnCount")
    override suspend fun generateCardToken(request: CardTokenBodyRequest): Result<CardToken, ResultError> {
        val result = service.createToken(request)
        return when (result.isSuccessful) {
            true -> {
                val body = result.body() ?: return Result.Error(ResultError(message = "empty body"))
                val id =
                    body.id ?: return Result.Error(ResultError(message = "no token identification"))
                Result.Success(CardToken(id))
            }

            false -> {
                Result.Error(
                    error = result.errorBody().toResultError(),
                )
            }
        }
    }

    override suspend fun getInstallments(request: InstallmentsRequest): Result<Installment, ResultError> {
        return Result.Success(
            Installment(
                payerCost = listOf(
                    PayerCost(
                        instalments = 1,
                        installmentAmount = 1000,
                        totalAmount = 1000.00f,
                    ),
                    PayerCost(
                        instalments = 2,
                        installmentAmount = 500,
                        totalAmount = 1000.00f,
                    ),
                    PayerCost(
                        instalments = 3,
                        installmentAmount = 370,
                        totalAmount = 1080.00f,
                    ),
                    PayerCost(
                        instalments = 4,
                        installmentAmount = 250,
                        totalAmount = 1200.00f,
                    ),
                    PayerCost(
                        instalments = 5,
                        installmentAmount = 150,
                        totalAmount = 1700.00f,
                    ),
                    PayerCost(
                        instalments = 6,
                        installmentAmount = 120,
                        totalAmount = 1800.00f,
                    ),
                    PayerCost(
                        instalments = 7,
                        installmentAmount = 95,
                        totalAmount = 1900.00f,
                    ),
                    PayerCost(
                        instalments = 8,
                        installmentAmount = 67,
                        totalAmount = 2200.00f,
                    ),
                ),
            ),
        )
//            val result = service.getInstallments(
//            productId = installmentsRequest.productId,
//            bin = installmentsRequest.bin,
//            processingMode = installmentsRequest.processingMode,
//            amount = installmentsRequest.amount
//        )
//        return when (result.isSuccessful) {
//            true -> {
//                val body = result.body() ?: return Result.Error(ResultError(message = "empty body"))
//                Result.Success(body.toModel())
//            }
//
//            false -> {
//                Result.Error(
//                    error = result.errorBody().toResultError()
//                )
//            }
//        }
    }

    override suspend fun getIdentificationTypes(): Result<List<IdentificationTypes>, ResultError> {
        TODO("Not yet implemented")
    }
}
