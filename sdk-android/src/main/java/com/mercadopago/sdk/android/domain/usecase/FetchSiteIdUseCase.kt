package com.mercadopago.sdk.android.domain.usecase

import com.mercadopago.sdk.android.domain.model.CountryCode
import com.mercadopago.sdk.android.domain.model.SiteId
import com.mercadopago.sdk.android.domain.repository.SdkInitializationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal const val UNKNOWN_SITE_ID = "UNKNOWN"

internal class FetchSiteIdUseCase(
    private val sdkInitializationRepository: SdkInitializationRepository,
) {

    operator fun invoke(publicKey: String, countryCode: CountryCode): Flow<SiteId> {
        return sdkInitializationRepository.fetchSiteId(publicKey, countryCode)
            .map { siteId ->
                if (siteId.siteId.isEmpty()) {
                    SiteId(UNKNOWN_SITE_ID)
                } else {
                    siteId
                }
            }
    }
}
