package com.mercadopago.sdk.android.analytics

import com.mercadopago.sdk.android.analytics.models.Application
import com.mercadopago.sdk.android.analytics.models.Device
import com.mercadopago.sdk.android.analytics.models.Track
import com.mercadopago.sdk.android.analytics.models.User
import java.util.UUID

/** AnalyticsEventData open class
 *
 * Extend this class with your data class
 * that's will be sent in track's event data.
 *
 * Example:
 * ```kotlin
 * data class PaymentTrackData(
 *     val paymentID: String = "paymentID"
 * ) : AnalyticsEventData()
 * ```
 * */
open class AnalyticsEventData

/** Track Type enum class */
enum class TrackType {
    VIEW,
    EVENT
}

interface AnalyticsInterface {

    /** Sets custom data for the next event
     *
     * @param data Object implementing `AnalyticsEventData` containing event data
     * @return Self instance for method chaining */
    fun setEventData(data: AnalyticsEventData): AnalyticsInterface

    /** Tracks a custom event
     *
     * @param path Path identifying the event (e.g., "payment/credit_card")
     * @return Self instance for method chaining */
    fun trackEvent(path: String): AnalyticsInterface

    /** Tracks a screen view
     *
     * @param path Path identifying the screen (e.g., "checkout/review")
     * @return Self instance for method chaining */
    fun trackView(path: String): AnalyticsInterface

    /** Sets the site ID for the next event
     *
     * @param siteId Site identifier (e.g., "MLB" )
     * @return Self instance for method chaining */
    fun setSiteId(siteId: String): AnalyticsInterface

    fun send(): Track
}

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
class Analytics : AnalyticsInterface {

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

    override fun trackEvent(path: String): AnalyticsInterface {
        this.type = TrackType.EVENT
        this.path = path
        return this
    }

    override fun trackView(path: String): AnalyticsInterface {
        this.type = TrackType.VIEW
        this.path = path
        return this
    }

    override fun setSiteId(siteId: String): AnalyticsInterface {
        this.siteId = siteId
        return this
    }

    override fun setEventData(data: AnalyticsEventData): AnalyticsInterface {
        eventData = data
        return this
    }

    /** Sets the SDK version for the next event
     *
     * @param version String containing the version (e.g., "1.0.0")
     * @return Self instance for method chaining */
    fun setVersion(version: String): AnalyticsInterface {
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
    override fun send(): Track {
        val track = Track(
            path = this.path,
            user = User(
                uid = ""
            ),
            type = this.type.name,
            id = this.sessionId,
            userTime = System.currentTimeMillis().toString(),
            eventData = this.eventData,
            application = Application(
                business = "mercadopago",
                siteId = this.siteId,
                version = this.version
            ),
            device = Device(
                platform = "/mobile/android"
            )
        )
        return track
    }
}
