package com.mercadopago.sdk.android.coremethods.domain.usecase

import com.mercadopago.sdk.android.coremethods.domain.model.Installment
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.model.params.GetInstallmentParams
import com.mercadopago.sdk.android.coremethods.domain.repository.CoreMethodsRepository
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

internal class GetInstallmentsUseCase(
    private val repository: CoreMethodsRepository
) {
    suspend operator fun invoke(
        bin: String?,
        amount: Long?
    ): Result<Installment, ResultError> {
        return repository.getInstallment(
            GetInstallmentParams(
                bin = bin?.toIntOrNull(),
                amount = amount
            )
        )
    }
}
