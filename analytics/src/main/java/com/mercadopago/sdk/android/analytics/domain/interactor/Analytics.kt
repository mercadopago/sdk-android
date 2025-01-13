package com.mercadopago.sdk.android.analytics.domain.interactor

import com.mercadopago.sdk.android.analytics.domain.exception.AnalyticsInitializationException

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
