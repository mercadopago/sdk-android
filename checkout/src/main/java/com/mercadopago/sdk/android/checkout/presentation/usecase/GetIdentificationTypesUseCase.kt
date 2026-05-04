package com.mercadopago.sdk.android.checkout.presentation.usecase

import com.mercadopago.sdk.android.checkout.domain.exception.ErrorLocalized
import com.mercadopago.sdk.android.checkout.domain.exception.mapToCheckoutError
import com.mercadopago.sdk.android.checkout.domain.extensions.withRetry
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError
import com.mercadopago.sdk.android.coremethods.domain.interactor.CoreMethods
import com.mercadopago.sdk.android.coremethods.domain.interactor.coreMethods
import com.mercadopago.sdk.android.coremethods.domain.model.IdentificationType
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import com.mercadopago.sdk.android.domain.model.CountryCode
import com.mercadopago.sdk.android.initializer.MercadoPagoSDK

internal class GetIdentificationTypesUseCase(
    private val coreMethods: CoreMethods = MercadoPagoSDK.getInstance().coreMethods,
) {
    suspend operator fun invoke(): Result<List<IdentificationType>, MercadoPagoCheckoutError> =
        if (MercadoPagoSDK.countryCode == CountryCode.MEX) {
            Result.Success(emptyList())
        } else {
            withRetry {
                coreMethods.getIdentificationTypes()
            }.mapToCheckoutError(ErrorLocalized.IDENTIFICATION)
        }
}
