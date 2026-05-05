package com.mercadopago.sdk.android.checkout.domain.usecase

import com.mercadopago.sdk.android.checkout.domain.model.CardFormInitializationOutput
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError
import com.mercadopago.sdk.android.checkout.domain.model.params.InitializeCardFormParams
import com.mercadopago.sdk.android.checkout.domain.repository.CardFormRepository
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

internal class InitializeCardFormUseCase(
    private val repository: CardFormRepository,
) {
    suspend operator fun invoke(
        amount: String,
        checkoutType: String,
    ): Result<CardFormInitializationOutput, MercadoPagoCheckoutError> =
        repository.fetchInitialization(
            InitializeCardFormParams(
                amount = amount,
                checkoutType = checkoutType,
            ),
        )
}
