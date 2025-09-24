package com.mercadopago.sdk.android.data.repository

import com.mercadopago.sdk.android.data.local.datasource.SdkInitializationLocalDataSource
import com.mercadopago.sdk.android.data.local.mapper.toSiteId
import com.mercadopago.sdk.android.data.remote.datasource.SdkInitializationRemoteDataSource
import com.mercadopago.sdk.android.domain.model.CountryCode
import com.mercadopago.sdk.android.domain.model.SiteId
import com.mercadopago.sdk.android.domain.repository.SdkInitializationRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.timeout
import kotlin.time.DurationUnit
import kotlin.time.toDuration

private const val MAX_RETRY_COUNT = 3L
private const val TIMEOUT = 10000L

internal class SdkInitializationRepositoryImpl(
    private val sdkInitializationRemoteDataSource: SdkInitializationRemoteDataSource,
    private val sdkInitializationLocalDataSource: SdkInitializationLocalDataSource,
) : SdkInitializationRepository {

    @OptIn(FlowPreview::class)
    override fun fetchSiteId(publicKey: String): Flow<SiteId> = flow {
        val cachedSiteId: SiteId = sdkInitializationLocalDataSource.getSiteId(publicKey).first()

        if (cachedSiteId.siteId.isNotEmpty()) {
            emit(cachedSiteId)
        } else {
            emitAll(
                sdkInitializationRemoteDataSource.fetchSiteId(publicKey)
                    .retry(MAX_RETRY_COUNT)
                    .timeout(TIMEOUT.toDuration(DurationUnit.MILLISECONDS))
                    .onEach { siteId ->
                        sdkInitializationLocalDataSource.setSiteId(publicKey, siteId)
                    }
                    .catch { _ ->
                        emitAll(sdkInitializationLocalDataSource.getSiteId(publicKey))
                    }
            )
        }
    }

    override fun getSiteId(publicKey: String): Flow<SiteId> {
        return sdkInitializationLocalDataSource.getSiteId(publicKey)
    }

    override fun setSiteId(publicKey: String, countryCode: CountryCode): Flow<Unit> {
        return sdkInitializationLocalDataSource.setSiteId(publicKey, SiteId(countryCode.toSiteId()))
    }
}
