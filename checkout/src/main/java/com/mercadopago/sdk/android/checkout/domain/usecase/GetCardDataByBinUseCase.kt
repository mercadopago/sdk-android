package com.mercadopago.sdk.android.checkout.domain.usecase

import com.mercadopago.sdk.android.checkout.domain.extensions.flatMap
import com.mercadopago.sdk.android.checkout.domain.extensions.fold
import com.mercadopago.sdk.android.checkout.domain.mapper.toSecurityCode
import com.mercadopago.sdk.android.checkout.domain.model.CardData
import com.mercadopago.sdk.android.coremethods.domain.model.CardIssuer
import com.mercadopago.sdk.android.coremethods.domain.model.Installment
import com.mercadopago.sdk.android.coremethods.domain.model.PaymentMethod
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import java.math.BigDecimal

internal class GetCardDataByBinUseCase(
    private val getPaymentMethodsUseCase: GetPaymentMethodsByBinUseCase,
    private val getCardIssuersUseCase: GetCardIssuersByBinUseCase,
    private val getInstallmentsUseCase: GetInstallmentsByBinUseCase,
) {
    suspend operator fun invoke(
        bin: String,
        amount: BigDecimal?,
    ): Result<CardData, ResultError> =
        getPaymentMethodsUseCase(bin)
            .flatMap { paymentMethods ->
                paymentMethods.firstOrNull()?.let { paymentMethod ->
                    fetchCardDataParallel(bin = bin, amount = amount, paymentMethod = paymentMethod)
                } ?: Result.Error(ResultError.Validation("No payment method found"))
            }

    @Suppress("ReturnCount")
    private suspend fun fetchCardDataParallel(
        bin: String,
        amount: BigDecimal?,
        paymentMethod: PaymentMethod,
    ): Result<CardData, ResultError> {
        val issuers = paymentMethod.id?.let {
            getCardIssuersUseCase(bin, it).fold(
                onSuccess = { it },
                onError = { return Result.Error(it) },
            )
        }

        val installments = amount?.let {
            getInstallmentsUseCase(bin, amount).fold(
                onSuccess = { it },
                onError = { return Result.Error(it) },
            )
        }

        return buildCardData(paymentMethod, issuers, installments)
    }

    private fun buildCardData(
        paymentMethod: PaymentMethod,
        issuers: List<CardIssuer>?,
        installments: List<Installment>?,
    ): Result<CardData, ResultError> =
        Result.Success(
            CardData(
                paymentMethod = paymentMethod,
                securityCode = paymentMethod.toSecurityCode(),
                cardIssuer = issuers?.firstOrNull(),
                installments = installments,
            ),
        )
}
