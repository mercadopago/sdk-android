package com.mercadopago.sdk.android.checkout.domain.usecase

import com.mercadopago.sdk.android.checkout.core.model.PaymentMethod
import com.mercadopago.sdk.android.checkout.domain.extensions.flatMap
import com.mercadopago.sdk.android.checkout.domain.extensions.fold
import com.mercadopago.sdk.android.checkout.domain.mapper.extractCardFilters
import com.mercadopago.sdk.android.checkout.domain.mapper.hasIssuers
import com.mercadopago.sdk.android.checkout.domain.mapper.matchesCardFilters
import com.mercadopago.sdk.android.checkout.domain.mapper.toSecurityCode
import com.mercadopago.sdk.android.checkout.domain.model.CardData
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import java.math.BigDecimal
import com.mercadopago.sdk.android.coremethods.domain.model.PaymentMethod as ApiPaymentMethod

internal class GetCardDataByBinUseCase(
    private val getPaymentMethodsUseCase: GetPaymentMethodsUseCase,
    private val getCardIssuersUseCase: GetCardIssuersUseCase,
    private val getInstallmentsUseCase: GetInstallmentsUseCase,
) {
    suspend operator fun invoke(
        bin: String,
        amount: BigDecimal?,
        paymentMethods: List<PaymentMethod>?,
    ): Result<CardData, ResultError> =
        getPaymentMethodsUseCase(bin).flatMap { data ->
            val (cardTypes, cardBrands) = paymentMethods.extractCardFilters()
            data
                .firstOrNull { it.matchesCardFilters(cardTypes, cardBrands) }
                ?.let { fetchCardData(bin, amount, it) }
                ?: Result.Error(ResultError.Validation("No payment method found matching the criteria"))
        }

    @Suppress("ReturnCount")
    private suspend fun fetchCardData(
        bin: String,
        amount: BigDecimal?,
        paymentMethod: ApiPaymentMethod,
    ): Result<CardData, ResultError> {
        val issuers = if (paymentMethod.hasIssuers()) {
            getCardIssuersUseCase(bin, paymentMethod.id.orEmpty()).fold(
                onSuccess = { it },
                onError = { return Result.Error(it) },
            )
        } else {
            null
        }

        val installments = amount?.let {
            getInstallmentsUseCase(bin, amount).fold(
                onSuccess = { it },
                onError = { return Result.Error(it) },
            )
        }

        return Result.Success(
            CardData(
                paymentMethod = paymentMethod,
                securityCode = paymentMethod.toSecurityCode(),
                cardIssuer = issuers?.firstOrNull(),
                installments = installments,
            ),
        )
    }
}
