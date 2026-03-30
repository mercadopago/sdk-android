package com.mercadopago.sdk.android.coremethods.domain.usecase

import com.mercadopago.sdk.android.coremethods.domain.model.PaymentMethod
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.model.params.GetPaymentMethodsParams
import com.mercadopago.sdk.android.coremethods.domain.repository.CoreMethodsRepository
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

internal class GetPaymentMethodsUseCase(
    private val repository: CoreMethodsRepository,
) {
    suspend operator fun invoke(
        bin: String,
    ): Result<List<PaymentMethod>, ResultError> {
        if (bin.length < MIN_BIN_LENGTH) {
            return Result.Error(ResultError.Validation(message = "BIN must have at least 6 digits"))
        }
        return repository.getPaymentMethods(
            GetPaymentMethodsParams(
                bin = bin.toIntOrNull(),
            ),
        )
    }

    companion object {
        const val MIN_BIN_LENGTH = 6
    }
}
