package com.mercadopago.sdk.android.coremethods.domain.usecase

import com.mercadopago.sdk.android.coremethods.domain.model.MPError
import com.mercadopago.sdk.android.coremethods.domain.model.PaymentMethod
import com.mercadopago.sdk.android.coremethods.domain.model.params.GetPaymentMethodsParams
import com.mercadopago.sdk.android.coremethods.domain.repository.CoreMethodsRepository
import com.mercadopago.sdk.android.coremethods.domain.utils.MPResult

internal class GetPaymentMethodsUseCase(
    private val repository: CoreMethodsRepository,
) {
    suspend operator fun invoke(bin: String): MPResult<List<PaymentMethod>, MPError> {
        return repository.getPaymentMethods(
            GetPaymentMethodsParams(
                bin = bin.toIntOrNull(),
            ),
        )
    }
}
