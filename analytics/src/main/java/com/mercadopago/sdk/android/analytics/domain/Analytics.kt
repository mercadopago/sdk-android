package com.mercadopago.sdk.android.analytics.domain

import com.mercadopago.sdk.android.analytics.domain.models.AnalyticsEventData

interface Analytics {
    /** Sets custom data for the next event
     *
     * @param data Object implementing `AnalyticsEventData` containing event data
     * @return Self instance for method chaining */
    fun setEventData(data: AnalyticsEventData): Analytics

    /** Tracks a custom event
     *
     * @param path Path identifying the event (e.g., "payment/credit_card")
     * @return Self instance for method chaining */
    fun trackEvent(path: String): Analytics

    /** Tracks a screen view
     *
     * @param path Path identifying the screen (e.g., "checkout/review")
     * @return Self instance for method chaining */
    fun trackView(path: String): Analytics

    /** Sets the site ID for the next event
     *
     * @param siteId Site identifier (e.g., "MLB" )
     * @return Self instance for method chaining */
    fun setSiteId(siteId: String): Analytics
}
