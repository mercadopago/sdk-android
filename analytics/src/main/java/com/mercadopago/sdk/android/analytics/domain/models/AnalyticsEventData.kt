package com.mercadopago.sdk.android.analytics.domain.models

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
