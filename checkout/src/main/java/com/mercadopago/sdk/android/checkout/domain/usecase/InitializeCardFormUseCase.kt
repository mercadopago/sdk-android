package com.mercadopago.sdk.android.checkout.domain.usecase

import com.mercadopago.sdk.android.checkout.domain.exception.ErrorLocalized
import com.mercadopago.sdk.android.checkout.domain.exception.ObservedCheckoutErrorFactory.mapResponseObserved
import com.mercadopago.sdk.android.checkout.domain.extensions.withServiceRetry
import com.mercadopago.sdk.android.checkout.domain.model.CardFormInitializationOutput
import com.mercadopago.sdk.android.checkout.domain.model.ObservedCheckoutError
import com.mercadopago.sdk.android.checkout.domain.model.params.InitializeCardFormParams
import com.mercadopago.sdk.android.checkout.domain.repository.CardFormRepository
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

internal class InitializeCardFormUseCase(
    private val repository: CardFormRepository,
) {
    suspend operator fun invoke(
        orderId: String?,
        clientToken: String?,
        checkoutType: String,
    ): Result<CardFormInitializationOutput, ObservedCheckoutError> =
        withServiceRetry {
            repository.fetchInitialization(
                InitializeCardFormParams(
                    orderId = orderId,
                    clientToken = clientToken,
                    checkoutType = checkoutType,
                ),
            )
        }.mapResponseObserved(ErrorLocalized.CARD_FORM_INITIALIZATION)
}
