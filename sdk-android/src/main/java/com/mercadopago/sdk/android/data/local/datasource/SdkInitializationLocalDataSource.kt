package com.mercadopago.sdk.android.data.local.datasource

import com.mercadopago.sdk.android.domain.model.SiteId
import kotlinx.coroutines.flow.Flow

internal interface SdkInitializationLocalDataSource {

    fun getSiteId(publicKey: String): Flow<SiteId>

    fun setSiteId(publicKey: String, siteId: SiteId): Flow<Unit>
}
