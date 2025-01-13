package com.mercadopago.sdk.android.analytics.domain.interactor

import com.mercadopago.sdk.android.analytics.domain.exception.AnalyticsInitializationException

/** Core analytics implementation for the MercadoPago SDK.
 *
 * This class is responsible for:
 * - Collecting events and screen views
 * - Aggregating environment data
 * - Formatting and sending analytics data
 * */
class Analytics constructor(
    private val sessionId: String,
    private val siteId: String,
    private val version: String
) {

    companion object {
        private var instance: Analytics? = null

        fun getInstance(): Analytics {
            return instance ?: throw AnalyticsInitializationException()
        }

        fun initialize(
            sessionId: String,
            siteId: String,
            version: String
        ) {
            instance = Analytics(
                sessionId,
                siteId,
                version
            )
        }
    }
}
