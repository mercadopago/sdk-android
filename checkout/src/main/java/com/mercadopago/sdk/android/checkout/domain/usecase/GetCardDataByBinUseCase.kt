package com.mercadopago.sdk.android.checkout.domain.usecase

import com.mercadopago.android.sdk.checkout.R
import com.mercadopago.sdk.android.checkout.core.model.PaymentMethod
import com.mercadopago.sdk.android.checkout.domain.exception.ErrorCode
import com.mercadopago.sdk.android.checkout.domain.exception.ErrorLocalized
import com.mercadopago.sdk.android.checkout.domain.extensions.flatMap
import com.mercadopago.sdk.android.checkout.domain.extensions.fold
import com.mercadopago.sdk.android.checkout.domain.extensions.hasIssuers
import com.mercadopago.sdk.android.checkout.domain.extensions.toSecurityCode
import com.mercadopago.sdk.android.checkout.domain.model.CardData
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError
import com.mercadopago.sdk.android.checkout.domain.provider.StringProvider
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import java.math.BigDecimal
import com.mercadopago.sdk.android.coremethods.domain.model.PaymentMethod as ApiPaymentMethod

internal class GetCardDataByBinUseCase(
    private val getPaymentMethodsUseCase: GetPaymentMethodsUseCase,
    private val getCardIssuersUseCase: GetCardIssuersUseCase,
    private val getInstallmentsUseCase: GetInstallmentsUseCase,
    private val stringProvider: StringProvider,
) {
    suspend operator fun invoke(
        bin: String,
        amount: BigDecimal?,
        paymentMethods: List<PaymentMethod>?,
    ): Result<CardData, MercadoPagoCheckoutError> =
        getPaymentMethodsUseCase(bin).flatMap { data ->
            data.firstOrNull()?.let { paymentMethod ->
                fetchCardData(bin, amount, paymentMethod)
            } ?: Result.Error(
                createValidationError(
                    stringProvider.getString(R.string.card_form_error_card_number_invalid),
                ),
            )
        }

    @Suppress("ReturnCount")
    private suspend fun fetchCardData(
        bin: String,
        amount: BigDecimal?,
        paymentMethod: ApiPaymentMethod,
    ): Result<CardData, MercadoPagoCheckoutError> {
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

    private fun createValidationError(
        message: String,
    ): MercadoPagoCheckoutError.ServiceError =
        MercadoPagoCheckoutError.ServiceError(
            code = ErrorCode.SERVICE_ERROR,
            messageError = message,
            localized = ErrorLocalized.PAYMENT_METHODS.name,
            throwable = null,
        )
}
