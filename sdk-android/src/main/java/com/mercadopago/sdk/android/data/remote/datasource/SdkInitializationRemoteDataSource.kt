package com.mercadopago.sdk.android.data.remote.datasource

import com.mercadopago.sdk.android.domain.model.SiteId
import kotlinx.coroutines.flow.Flow

internal interface SdkInitializationRemoteDataSource {

    fun fetchSiteId(publicKey: String): Flow<SiteId>
}
