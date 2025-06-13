package com.mercadopago.sdk.android.coremethods.domain.usecase

import com.mercadopago.sdk.android.coremethods.domain.model.IdentificationType
import com.mercadopago.sdk.android.coremethods.domain.model.MPError
import com.mercadopago.sdk.android.coremethods.domain.repository.CoreMethodsRepository
import com.mercadopago.sdk.android.coremethods.domain.utils.MPResult

internal class GetIdentificationTypesUseCase(
    private val repository: CoreMethodsRepository,
) {
    suspend operator fun invoke(): MPResult<List<IdentificationType>, MPError> {
        return repository.getIdentificationTypes()
    }
}
