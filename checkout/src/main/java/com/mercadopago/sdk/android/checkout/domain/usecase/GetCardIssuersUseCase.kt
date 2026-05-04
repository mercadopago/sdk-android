package com.mercadopago.sdk.android.checkout.domain.usecase

import com.mercadopago.sdk.android.checkout.domain.exception.ErrorLocalized
import com.mercadopago.sdk.android.checkout.domain.exception.mapToCheckoutError
import com.mercadopago.sdk.android.checkout.domain.extensions.withErrorHandling
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError
import com.mercadopago.sdk.android.coremethods.domain.interactor.CoreMethods
import com.mercadopago.sdk.android.coremethods.domain.interactor.coreMethods
import com.mercadopago.sdk.android.coremethods.domain.model.CardIssuer
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import com.mercadopago.sdk.android.initializer.MercadoPagoSDK

internal class GetCardIssuersUseCase(
    private val coreMethods: CoreMethods = MercadoPagoSDK.getInstance().coreMethods,
) {
    suspend operator fun invoke(
        bin: String,
        paymentMethodId: String,
    ): Result<List<CardIssuer>, MercadoPagoCheckoutError> =
        withErrorHandling {
            coreMethods.getCardIssuers(bin = bin, paymentMethodId = paymentMethodId)
        }.mapToCheckoutError(ErrorLocalized.ISSUERS)
}
