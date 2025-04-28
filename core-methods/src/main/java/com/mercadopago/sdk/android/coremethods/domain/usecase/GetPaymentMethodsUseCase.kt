package com.mercadopago.sdk.android.coremethods.domain.usecase

import com.mercadopago.sdk.android.coremethods.domain.model.PaymentMethod
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.model.params.GetPaymentMethodsParams
import com.mercadopago.sdk.android.coremethods.domain.repository.CoreMethodsRepository
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

internal class GetPaymentMethodsUseCase(
    private val repository: CoreMethodsRepository,
) {
    suspend operator fun invoke(bin: String): Result<List<PaymentMethod>, ResultError> {
        return repository.getPaymentMethods(
            GetPaymentMethodsParams(
                productId = "",
                bin = bin.toIntOrNull(),
            ),
        )
    }
}
