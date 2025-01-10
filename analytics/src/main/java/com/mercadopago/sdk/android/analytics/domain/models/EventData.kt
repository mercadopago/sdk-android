package com.mercadopago.sdk.android.analytics.domain.models

/** EventData open class
 *
 * Extend this class with your data class.
 * That's will be sent in track's event data.
 *
 * @see Metric
 * Example:
 * ```kotlin
 * data class MyMetricData(
 *     val someProperty1: Int
 *     val someProperty2: Int
 *     val someProperty3: Int
 * ) : EventData
 * ```
 * */
interface EventData
