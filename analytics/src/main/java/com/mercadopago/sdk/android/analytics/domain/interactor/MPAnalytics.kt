package com.mercadopago.sdk.android.analytics.domain.interactor

import com.mercadopago.sdk.android.analytics.domain.exception.AnalyticsInitializationException
import com.mercadopago.sdk.android.analytics.domain.models.Metric
import com.mercadopago.sdk.android.analytics.utils.KoverIgnore

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
 * @param siteId site Id of the seller
 * @param version this application SDK version
 * */
class MPAnalytics internal constructor(
    private val sessionId: String,
    private val siteId: String,
    private val version: String
) {

    companion object {
        @Volatile
        private var instance: MPAnalytics? = null

        fun getInstance(): MPAnalytics {
            return instance ?: throw AnalyticsInitializationException()
        }

        fun initialize(
            sessionId: String,
            siteId: String,
            version: String
        ) {
            instance = MPAnalytics(
                sessionId,
                siteId,
                version
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
