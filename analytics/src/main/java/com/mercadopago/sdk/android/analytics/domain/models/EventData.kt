package com.mercadopago.sdk.android.analytics.domain.models

import com.google.gson.annotations.SerializedName
import com.mercadopago.sdk.android.core.utils.isDebugApp

/** EventData open class
 *
 * Extend this class to create your data class.
 * That's will be sent in track's event data.
 * @param isDevelopment if the app is in debug mode.
 *
 * Example:
 * ```kotlin
 *  data class MyMetricData(
 *      val someProperty1: String
 *  ) : EventData
 * ```
 * */
open class EventData(
    @SerializedName("is_development")
    val isDevelopment: Boolean = isDebugApp(),
)
