package com.mercadopago.sdk.android.data.remote.datasource

import com.mercadopago.sdk.android.data.remote.mapper.toDomain
import com.mercadopago.sdk.android.data.remote.response.SiteIdResponse
import com.mercadopago.sdk.android.data.remote.service.SdkInitializationService
import com.mercadopago.sdk.android.domain.model.SiteId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

internal class SdkInitializationRemoteDataSourceImpl(
    private val sdkInitializationService: SdkInitializationService,
) : SdkInitializationRemoteDataSource {

    override fun fetchSiteId(publicKey: String): Flow<SiteId> {
        return flow { emit(sdkInitializationService.fetchSiteId()) }
            .map(SiteIdResponse::toDomain)
    }
}
