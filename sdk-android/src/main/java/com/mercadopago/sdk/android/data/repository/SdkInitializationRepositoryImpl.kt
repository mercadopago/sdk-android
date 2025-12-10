package com.mercadopago.sdk.android.data.repository

import com.mercadopago.sdk.android.data.local.datasource.SdkInitializationLocalDataSource
import com.mercadopago.sdk.android.data.local.mapper.toSiteId
import com.mercadopago.sdk.android.domain.model.CountryCode
import com.mercadopago.sdk.android.domain.model.SiteId
import com.mercadopago.sdk.android.domain.repository.SdkInitializationRepository
import kotlinx.coroutines.flow.Flow

internal class SdkInitializationRepositoryImpl(
    private val sdkInitializationLocalDataSource: SdkInitializationLocalDataSource,
) : SdkInitializationRepository {

    override fun getSiteId(publicKey: String): Flow<SiteId> {
        return sdkInitializationLocalDataSource.getSiteId(publicKey)
    }

    override fun setSiteId(publicKey: String, countryCode: CountryCode): Flow<Unit> {
        return sdkInitializationLocalDataSource.setSiteId(publicKey, SiteId(countryCode.toSiteId()))
    }
}
