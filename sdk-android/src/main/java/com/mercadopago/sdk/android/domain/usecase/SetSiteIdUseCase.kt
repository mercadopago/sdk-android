package com.mercadopago.sdk.android.domain.usecase

import com.mercadopago.sdk.android.domain.model.CountryCode
import com.mercadopago.sdk.android.domain.repository.SdkInitializationRepository
import kotlinx.coroutines.flow.Flow

internal class SetSiteIdUseCase(
    private val sdkInitializationRepository: SdkInitializationRepository,
) {

    operator fun invoke(publicKey: String, countryCode: CountryCode): Flow<Unit> {
        return sdkInitializationRepository.setSiteId(publicKey, countryCode)
    }
}
