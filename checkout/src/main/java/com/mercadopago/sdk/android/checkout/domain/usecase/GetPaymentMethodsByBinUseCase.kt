package com.mercadopago.sdk.android.checkout.domain.usecase

import com.mercadopago.sdk.android.coremethods.domain.interactor.CoreMethods
import com.mercadopago.sdk.android.coremethods.domain.interactor.coreMethods
import com.mercadopago.sdk.android.coremethods.domain.model.PaymentMethod
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import com.mercadopago.sdk.android.initializer.MercadoPagoSDK

internal class GetPaymentMethodsByBinUseCase(
    private val coreMethods: CoreMethods = MercadoPagoSDK.getInstance().coreMethods,
) {
    suspend operator fun invoke(
        bin: String,
    ): Result<List<PaymentMethod>, ResultError> = coreMethods.getPaymentMethods(bin = bin)
}
