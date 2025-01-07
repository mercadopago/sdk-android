package com.mercadopago.sdk.android.analytics.models

import com.google.gson.annotations.SerializedName
import com.mercadopago.sdk.android.analytics.AnalyticsEventData

data class AnalyticsRequest(
    @SerializedName("tracks")
    val tracks: List<Track>
)

data class Track(
    @SerializedName("path")
    val path: String,
    @SerializedName("user")
    val user: User,
    @SerializedName("type")
    val type: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("user_time")
    val userTime: String,
    @SerializedName("event_data")
    val eventData: AnalyticsEventData?,
    @SerializedName("application")
    val application: Application,
    @SerializedName("device")
    val device: Device
)

data class User(
    @SerializedName("uid")
    val uid: String
)

data class Application(
    @SerializedName("business")
    val business: String,
    @SerializedName("site_id")
    val siteId: String,
    @SerializedName("version")
    val version: String
)

data class Device(
    @SerializedName("platform")
    val platform: String
)
