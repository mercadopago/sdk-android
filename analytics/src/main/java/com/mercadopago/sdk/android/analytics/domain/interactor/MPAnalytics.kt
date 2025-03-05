package com.mercadopago.sdk.android.analytics.domain.interactor

import com.mercadopago.sdk.android.analytics.domain.exception.AnalyticsInitializationException
import com.mercadopago.sdk.android.analytics.domain.models.Metric
import com.mercadopago.sdk.android.core.utils.KoverIgnore
import kotlinx.coroutines.flow.Flow

/** Core analytics implementation for the MercadoPago SDK.
 *
 * This class is responsible for:
 * - Collecting events and screen views
 * - Aggregating environment data
 * - Formatting and sending analytics data
 *
 *  This class have to be initialized first with [initialize] method
 *  then get a instance by [getInstance]
 * @param sessionId session identification of this analytics instance
 * @param publicKey the public key used to initialize the SDK.
 * @param version this application SDK version.
 * @param getSiteIdFlow a flow that emits the current siteId.
 * */
class MPAnalytics internal constructor(
    private val sessionId: String,
    private val publicKey: String,
    private val version: String,
    private val getSiteIdFlow: Flow<String>,
) {

    companion object {
        @Volatile
        private var instance: MPAnalytics? = null

        fun getInstance(): MPAnalytics {
            return instance ?: throw AnalyticsInitializationException()
        }

        fun initialize(
            sessionId: String,
            publicKey: String,
            version: String,
            getSiteIdFlow: Flow<String>,
        ) {
            instance = MPAnalytics(
                sessionId,
                publicKey,
                version,
                getSiteIdFlow,
            )
        }
    }

    /** Processes and sends the current event
     * This method:
     * 1. Collects user information
     * 2. Builds the payload with all required data
     * 4. Sends the data (currently just prints to console)
     *
     * - Note: Actual data sending implementation should be added in the future */
    @KoverIgnore("implementation should be added in the future")
    @Suppress("EmptyFunctionBlock", "UnusedParameter")
    fun trackMetric(metric: Metric) { }
}
