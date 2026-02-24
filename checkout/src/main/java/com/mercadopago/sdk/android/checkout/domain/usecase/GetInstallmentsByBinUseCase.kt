package com.mercadopago.sdk.android.checkout.domain.usecase

import com.mercadopago.sdk.android.coremethods.domain.interactor.CoreMethods
import com.mercadopago.sdk.android.coremethods.domain.interactor.coreMethods
import com.mercadopago.sdk.android.coremethods.domain.model.Installment
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import com.mercadopago.sdk.android.initializer.MercadoPagoSDK
import java.math.BigDecimal

internal class GetInstallmentsByBinUseCase(
    private val coreMethods: CoreMethods = MercadoPagoSDK.getInstance().coreMethods,
) {
    suspend operator fun invoke(
        bin: String,
        amount: BigDecimal,
    ): Result<List<Installment>, ResultError> = coreMethods.getInstallments(bin = bin, amount = amount)
}
