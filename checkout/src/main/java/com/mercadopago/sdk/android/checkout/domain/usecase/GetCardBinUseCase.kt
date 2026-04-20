package com.mercadopago.sdk.android.checkout.domain.usecase

import com.mercadopago.sdk.android.checkout.data.remote.datasource.CardFormRemoteDataSource
import com.mercadopago.sdk.android.checkout.data.remote.mapper.toDomain
import com.mercadopago.sdk.android.checkout.domain.exception.ErrorLocalized
import com.mercadopago.sdk.android.checkout.domain.exception.mapToCheckoutError
import com.mercadopago.sdk.android.checkout.domain.extensions.map
import com.mercadopago.sdk.android.checkout.domain.extensions.withErrorHandling
import com.mercadopago.sdk.android.checkout.domain.model.CardBinData
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

internal class GetCardBinUseCase(
    private val cardFormRemoteDataSource: CardFormRemoteDataSource,
) {
    @Suppress("LongParameterList")
    suspend operator fun invoke(
        bin: String,
        amount: String,
        processingMode: String,
        locale: String,
        allowPaymentTypes: String?,
        allowPaymentMethods: String?,
    ): Result<CardBinData, MercadoPagoCheckoutError> =
        withErrorHandling {
            cardFormRemoteDataSource.getCardBin(
                bin = bin,
                amount = amount,
                processingMode = processingMode,
                locale = locale,
                allowPaymentTypes = allowPaymentTypes,
                allowPaymentMethods = allowPaymentMethods,
            )
        }.mapToCheckoutError(ErrorLocalized.CARD_BIN)
            .map { it.toDomain() }
}
