package com.mercadopago.sdk.android.checkout.domain.usecase

import com.mercadopago.sdk.android.checkout.data.remote.datasource.CardFormRemoteDataSource
import com.mercadopago.sdk.android.checkout.domain.exception.ErrorLocalized
import com.mercadopago.sdk.android.checkout.domain.exception.mapToCheckoutError
import com.mercadopago.sdk.android.checkout.domain.extensions.map
import com.mercadopago.sdk.android.checkout.domain.extensions.withErrorHandling
import com.mercadopago.sdk.android.checkout.domain.mapper.toDomain
import com.mercadopago.sdk.android.checkout.domain.model.CardFormInitialization
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

internal class GetCardFormInitializationUseCase(
    private val cardFormRemoteDataSource: CardFormRemoteDataSource,
) {
    suspend operator fun invoke(
        locale: String,
        amount: String,
        checkoutType: String,
    ): Result<CardFormInitialization, MercadoPagoCheckoutError> =
        withErrorHandling {
            cardFormRemoteDataSource.getInitialization(
                locale = locale,
                amount = amount,
                checkoutType = checkoutType,
            )
        }
            .map { it.toDomain() }
            .mapToCheckoutError(ErrorLocalized.CARD_FORM_INITIALIZATION)
}
