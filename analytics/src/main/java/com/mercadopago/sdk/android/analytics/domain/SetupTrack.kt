package com.mercadopago.sdk.android.analytics.domain

import com.mercadopago.sdk.android.analytics.domain.models.AnalyticsEventData

internal interface SetupTrack {
    /** Sets custom data for the track
     *
     * @param data Object implementing `AnalyticsEventData` containing event data
     * @return self instance for method chaining */
    fun setTrackData(data: AnalyticsEventData): SetupTrack

    /** Tracks a custom event
     *
     * @param path Path identifying the event (e.g., "payment/credit_card")
     * @return self instance for method chaining */
    fun trackEvent(path: String): SetupTrack

    /** Tracks a screen view
     *
     * @param path Path identifying the screen (e.g., "checkout/review")
     * @return self instance for method chaining */
    fun trackView(path: String): SetupTrack

    /** Sets the site ID for the next event
     *
     * @param siteId Site identifier (e.g., "MLB" )
     * @return self instance for method chaining */
    fun setSiteId(siteId: String): SetupTrack

    /** Sets the SDK version for the next event
     *
     * @param version String containing the version (e.g., "1.0.0")
     * @return self instance for method chaining */
    fun setVersion(version: String): SetupTrack
}
