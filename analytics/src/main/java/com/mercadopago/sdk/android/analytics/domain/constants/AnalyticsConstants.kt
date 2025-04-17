package com.mercadopago.sdk.android.analytics.domain.constants

import com.google.gson.annotations.SerializedName
import com.mercadopago.sdk.android.analytics.domain.models.EventData

/**
 *  This object holds constants used for analytics events and properties throughout the application.
 *  It serves as a central place to manage and document these constants, ensuring consistency
 *  and reducing the likelihood of errors due to typos or inconsistencies.
 */
object AnalyticsConstants {

    /**
     * Used for focus events
     */
    const val FOCUS_PATH = "/focus"

    /**
     * Used for error events
     */
    const val ERROR_PATH = "/error"

    /**
     * Builds a [MetricErrorData] object containing error information.
     *
     * This function simplifies the creation of [MetricErrorData] instances by taking an error string as input and
     * encapsulating it within a [MetricErrorData] object. This is useful for reporting errors in a standardized format,
     * likely for metrics or error tracking within the application.
     *
     * @param error A string representing the error message.
     * @return A [MetricErrorData] object containing the provided error string.
     */
    fun buildErrorData(error: String) = MetricErrorData(error)
}

/**
 * Data class representing an error related to a metric.
 *
 * This class encapsulates information about a specific metric error, including the type of error that occurred.
 * It extends the `EventData` class, indicating that it represents data associated with an event within the system.
 *
 * @property errorType A string representing the type of metric error.
 * This field is serialized to JSON with the key "error_type".
 * Examples might include "INVALID_METRIC_NAME", "METRIC_NOT_FOUND", or "TIMEOUT".
 */
data class MetricErrorData(
    @SerializedName("error_type")
    val errorType: String,
) : EventData
