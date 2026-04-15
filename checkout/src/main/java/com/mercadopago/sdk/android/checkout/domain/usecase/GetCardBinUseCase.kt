package com.mercadopago.sdk.android.checkout.domain.usecase

import com.mercadopago.sdk.android.checkout.data.remote.datasource.CardFormRemoteDataSource
import com.mercadopago.sdk.android.checkout.data.remote.response.CardBinResponse
import com.mercadopago.sdk.android.checkout.domain.exception.ErrorLocalized
import com.mercadopago.sdk.android.checkout.domain.exception.mapToCheckoutError
import com.mercadopago.sdk.android.checkout.domain.extensions.withErrorHandling
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

internal class GetCardBinUseCase(
    private val cardFormRemoteDataSource: CardFormRemoteDataSource,
) {
    suspend operator fun invoke(
        bin: String,
        amount: String,
        processingMode: String,
        locale: String,
        allowCardTypes: String?,
        allowCardBrands: String?,
    ): Result<CardBinResponse, MercadoPagoCheckoutError> =
        withErrorHandling {
            cardFormRemoteDataSource.getCardBin(
                bin = bin,
                amount = amount,
                processingMode = processingMode,
                locale = locale,
                allowCardTypes = allowCardTypes,
                allowCardBrands = allowCardBrands,
            )
        }.mapToCheckoutError(ErrorLocalized.CARD_BIN)
}
