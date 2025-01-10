package com.mercadopago.sdk.android.analytics.domain.interactor

import androidx.annotation.VisibleForTesting
import com.mercadopago.sdk.android.analytics.data.remote.models.ApplicationRequest
import com.mercadopago.sdk.android.analytics.data.remote.models.DeviceRequest
import com.mercadopago.sdk.android.analytics.data.remote.models.TrackRequest
import com.mercadopago.sdk.android.analytics.data.remote.models.UserRequest
import com.mercadopago.sdk.android.analytics.domain.exception.AnalyticsInitializationException
import com.mercadopago.sdk.android.analytics.domain.models.EventData
import com.mercadopago.sdk.android.analytics.domain.models.Metric

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

    /** Processes and sends the current event
     * This method:
     * 1. Collects user information
     * 2. Builds the payload with all required data
     * 4. Sends the data (currently just prints to console)
     *
     * - Note: Actual data sending implementation should be added in the future */
    fun trackEvent(metric: Metric) {
        setupTrackRequest(
            path = metric.path,
            type = metric.type.name,
            eventData = metric.data
        )
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun setupTrackRequest(
        path: String,
        type: String,
        eventData: EventData
    ): TrackRequest {
        return TrackRequest(
            path = path,
            user = UserRequest(
                uid = ""
            ),
            type = type,
            id = this.sessionId,
            userTime = System.currentTimeMillis().toString(),
            eventData = eventData,
            application = ApplicationRequest(
                business = "mercadopago",
                siteId = this.siteId,
                version = this.version
            ),
            device = DeviceRequest(
                platform = "/mobile/android"
            )
        )
    }
}
