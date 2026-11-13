package com.mercadopago.sdk.android.coremethods.di

import com.mercadopago.sdk.android.analytics.domain.interactor.MPAnalytics
import com.mercadopago.sdk.android.di.SessionIdProvider
import kotlin.coroutines.cancellation.CancellationException

internal interface MelidataSessionIdProvider {
    suspend fun getSessionId(): String?
}

internal class MelidataSessionIdProviderImpl(
    private val analytics: () -> MPAnalytics?,
    private val fallbackSessionIdProvider: SessionIdProvider,
) : MelidataSessionIdProvider {
    override suspend fun getSessionId(): String? {
        return getAnalyticsSessionIdOrNull() ?: fallbackSessionIdProvider.getSessionId()
    }

    @Suppress("TooGenericExceptionCaught", "SwallowedException")
    private suspend fun getAnalyticsSessionIdOrNull(): String? =
        try {
            analytics()?.getSessionId()?.takeIf(String::isNotEmpty)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            null
        }
}
