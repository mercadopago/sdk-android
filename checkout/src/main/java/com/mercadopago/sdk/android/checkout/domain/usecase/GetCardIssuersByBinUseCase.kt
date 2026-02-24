package com.mercadopago.sdk.android.checkout.domain.usecase

import com.mercadopago.sdk.android.coremethods.domain.interactor.CoreMethods
import com.mercadopago.sdk.android.coremethods.domain.interactor.coreMethods
import com.mercadopago.sdk.android.coremethods.domain.model.CardIssuer
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import com.mercadopago.sdk.android.initializer.MercadoPagoSDK

internal class GetCardIssuersByBinUseCase(
    private val coreMethods: CoreMethods = MercadoPagoSDK.getInstance().coreMethods,
) {
    suspend operator fun invoke(
        bin: String,
        paymentMethodId: String,
    ): Result<List<CardIssuer>, ResultError> = coreMethods.getCardIssuers(bin = bin, paymentMethodId = paymentMethodId)
}
