package com.mercadopago.sdk.android.domain.usecase

import com.mercadopago.sdk.android.domain.model.SiteId
import com.mercadopago.sdk.android.domain.repository.SdkInitializationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class GetSiteIdUseCase(
    private val sdkInitializationRepository: SdkInitializationRepository,
) {

    operator fun invoke(publicKey: String): Flow<SiteId> {
        return sdkInitializationRepository.getSiteId(publicKey)
            .map { siteId ->
                if (siteId.siteId.isEmpty()) {
                    SiteId(UNKNOWN_SITE_ID)
                } else {
                    siteId
                }
            }
    }
}
