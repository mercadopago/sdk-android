package com.mercadopago.sdk.android.analytics.data.remote.models

import com.google.gson.annotations.SerializedName
import com.mercadopago.sdk.android.analytics.domain.models.EventData

internal data class AnalyticsRequest(
    @SerializedName("tracks")
    val tracks: List<TrackRequest>
)

internal data class TrackRequest(
    @SerializedName("path")
    val path: String,
    @SerializedName("user")
    val user: UserRequest,
    @SerializedName("type")
    val type: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("user_time")
    val userTime: String,
    @SerializedName("event_data")
    val eventData: EventData?,
    @SerializedName("application")
    val application: ApplicationRequest,
    @SerializedName("device")
    val device: DeviceRequest
)

internal data class UserRequest(
    @SerializedName("uid")
    val uid: String
)

internal data class ApplicationRequest(
    @SerializedName("business")
    val business: String,
    @SerializedName("site_id")
    val siteId: String,
    @SerializedName("version")
    val version: String
)

internal data class DeviceRequest(
    @SerializedName("platform")
    val platform: String
)
