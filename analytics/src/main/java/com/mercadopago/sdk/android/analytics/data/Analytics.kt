package com.mercadopago.sdk.android.analytics.data

import androidx.annotation.VisibleForTesting
import com.mercadopago.sdk.android.analytics.data.remote.models.ApplicationRequest
import com.mercadopago.sdk.android.analytics.data.remote.models.DeviceRequest
import com.mercadopago.sdk.android.analytics.data.remote.models.TrackRequest
import com.mercadopago.sdk.android.analytics.data.remote.models.UserRequest
import com.mercadopago.sdk.android.analytics.domain.SetupTrack
import com.mercadopago.sdk.android.analytics.domain.models.AnalyticsEventData
import com.mercadopago.sdk.android.analytics.domain.models.TrackType
import java.util.UUID

/** Core analytics implementation for the MercadoPago SDK.
 *
 * This class is responsible for:
 * - Collecting events and screen views
 * - Aggregating environment data
 * - Formatting and sending analytics data
 *
 * Example:
 *
 * ```kotlin
 * Analytics()
 *      .setEventData(paymentData)
 *      .trackView("payment/credit_card")
 *      .setSiteId("MLB")
 *      .send()
 * ```
 * */
class Analytics : SetupTrack {

    /** Unique identifier for the current analytics session */
    private var sessionId: String = UUID.randomUUID().toString()

    /** Custom data for the current event */
    private var eventData: AnalyticsEventData? = null

    /** Path identifying the current event or view */
    private var path = ""

    /** Type of the current tracking (event or view) */
    private var type: TrackType = TrackType.VIEW

    /** SDK version */
    private var version = ""

    /** Site ID (e.g., MLB, MLA)*/
    private var siteId = ""

    override fun trackEvent(path: String): Analytics {
        this.type = TrackType.EVENT
        this.path = path
        return this
    }

    override fun trackView(path: String): Analytics {
        this.type = TrackType.VIEW
        this.path = path
        return this
    }

    override fun setSiteId(siteId: String): Analytics {
        this.siteId = siteId
        return this
    }

    override fun setTrackData(data: AnalyticsEventData): Analytics {
        this.eventData = data
        return this
    }

    /** Sets the SDK version for the next event
     *
     * @param version String containing the version (e.g., "1.0.0")
     * @return Self instance for method chaining */
    override fun setVersion(version: String): Analytics {
        this.version = version
        return this
    }

    /** Processes and sends the current event
     * This method:
     * 1. Collects user information
     * 2. Builds the payload with all required data
     * 4. Sends the data (currently just prints to console)
     *
     * - Note: Actual data sending implementation should be added in the future */
    fun send() {
        setupTrackRequest()
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun setupTrackRequest(): TrackRequest {
        return TrackRequest(
            path = this.path,
            user = UserRequest(
                uid = ""
            ),
            type = this.type.name,
            id = this.sessionId,
            userTime = System.currentTimeMillis().toString(),
            eventData = this.eventData,
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
