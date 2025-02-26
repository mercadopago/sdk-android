package com.mercadopago.sdk.android.domain.repository

import com.mercadopago.sdk.android.domain.model.CountryCode
import com.mercadopago.sdk.android.domain.model.SiteId
import kotlinx.coroutines.flow.Flow

internal interface SdkInitializationRepository {

    fun fetchSiteId(publicKey: String): Flow<SiteId>

    fun getSiteId(publicKey: String): Flow<SiteId>

    fun setSiteId(publicKey: String, countryCode: CountryCode): Flow<Unit>
}
