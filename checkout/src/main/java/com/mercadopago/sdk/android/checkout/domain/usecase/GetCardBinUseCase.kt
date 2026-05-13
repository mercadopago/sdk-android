package com.mercadopago.sdk.android.checkout.domain.usecase

import com.mercadopago.sdk.android.checkout.domain.exception.ErrorLocalized
import com.mercadopago.sdk.android.checkout.domain.exception.ExceptionFactory.mapError
import com.mercadopago.sdk.android.checkout.domain.extensions.withServiceRetry
import com.mercadopago.sdk.android.checkout.domain.model.CardBinData
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError
import com.mercadopago.sdk.android.checkout.domain.model.params.GetCardBinParams
import com.mercadopago.sdk.android.checkout.domain.repository.CardFormRepository
import com.mercadopago.sdk.android.checkout.presentation.extensions.getOrZero
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

internal class GetCardBinUseCase(
    private val repository: CardFormRepository,
) {
    suspend operator fun invoke(
        bin: String,
        amount: String?,
        checkoutType: String,
        processingMode: String,
        filter: CardBinFilter,
    ): Result<CardBinData, MercadoPagoCheckoutError> =
        withServiceRetry {
            repository.getCardBin(
                GetCardBinParams(
                    bin = bin,
                    amount = amount.getOrZero(),
                    checkoutType = checkoutType,
                    processingMode = processingMode,
                    filter = filter,
                ),
            )
        }.mapError(ErrorLocalized.CARD_BIN)
}
