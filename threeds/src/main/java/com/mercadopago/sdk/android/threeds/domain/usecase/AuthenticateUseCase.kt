package com.mercadopago.sdk.android.threeds.domain.usecase

import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSAuthenticationModel
import com.mercadopago.sdk.android.threeds.domain.model.params.ThreeDSAuthenticationParams
import com.mercadopago.sdk.android.threeds.domain.repository.ThreeDSRepository
import kotlinx.coroutines.flow.Flow

internal class AuthenticateUseCase(
    private val repository: ThreeDSRepository,
) {
    operator fun invoke(
        params: ThreeDSAuthenticationParams
    ): Flow<MPThreeDSAuthenticationModel> {
        return repository.authenticate(
            params = params
        )
    }
}
