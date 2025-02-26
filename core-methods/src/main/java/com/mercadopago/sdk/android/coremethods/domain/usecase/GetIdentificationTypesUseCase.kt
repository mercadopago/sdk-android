package com.mercadopago.sdk.android.coremethods.domain.usecase

import com.mercadopago.sdk.android.coremethods.domain.model.IdentificationTypes
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.repository.CoreMethodsRepository
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

internal class GetIdentificationTypesUseCase(
    private val repository: CoreMethodsRepository,
) {
    suspend operator fun invoke(): Result<List<IdentificationTypes>, ResultError> {
        return repository.getIdentificationTypes()
    }
}
