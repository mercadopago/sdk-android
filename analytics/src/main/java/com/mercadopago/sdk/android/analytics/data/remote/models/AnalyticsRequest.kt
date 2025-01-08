package com.mercadopago.sdk.android.analytics.data.remote.models

import com.google.gson.annotations.SerializedName
import com.mercadopago.sdk.android.analytics.domain.models.AnalyticsEventData

data class AnalyticsRequest(
    @SerializedName("tracks")
    val tracks: List<TrackRequest>
)

data class TrackRequest(
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
    val eventData: AnalyticsEventData?,
    @SerializedName("application")
    val application: ApplicationRequest,
    @SerializedName("device")
    val device: DeviceRequest
)

data class UserRequest(
    @SerializedName("uid")
    val uid: String
)

data class ApplicationRequest(
    @SerializedName("business")
    val business: String,
    @SerializedName("site_id")
    val siteId: String,
    @SerializedName("version")
    val version: String
)

data class DeviceRequest(
    @SerializedName("platform")
    val platform: String
)
