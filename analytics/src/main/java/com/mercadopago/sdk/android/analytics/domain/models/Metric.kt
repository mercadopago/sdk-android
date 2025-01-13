package com.mercadopago.sdk.android.analytics.domain.models

/** Metric data class
 *
 * Extend this class with your data class
 * that's will be sent in track's event data.
 *
 * @param type indicates this track [TrackType]
 * @param path indicates this track path
 * @param data indicates this track [EventData]
 * @see EventData
 * @see TrackType
 * */
data class Metric(val type: TrackType, val path: String, val data: EventData)
