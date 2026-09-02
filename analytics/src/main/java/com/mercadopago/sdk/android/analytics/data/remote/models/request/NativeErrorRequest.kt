package com.mercadopago.sdk.android.analytics.data.remote.models.request

import com.google.gson.annotations.SerializedName

internal data class NativeErrorRequest(
    @SerializedName("event_id") val eventId: String,
    @SerializedName("occurred_at") val occurredAt: String,
    @SerializedName("source") val source: NativeErrorSourceRequest,
    @SerializedName("site_id") val siteId: String,
    @SerializedName("error") val error: NativeErrorDetailRequest,
    @SerializedName("device") val device: NativeErrorDeviceRequest?,
)

internal data class NativeErrorSourceRequest(
    @SerializedName("sdk_name") val sdkName: String,
    @SerializedName("sdk_version") val sdkVersion: String,
    @SerializedName("host_platform") val hostPlatform: String,
    @SerializedName("sdk_technology") val sdkTechnology: String,
    @SerializedName("module") val module: String,
    @SerializedName("operation") val operation: String,
)

internal data class NativeErrorDetailRequest(
    @SerializedName("code") val code: String,
    @SerializedName("category") val category: String,
    @SerializedName("critical") val critical: Boolean,
    @SerializedName("status_code") val statusCode: Int?,
    @SerializedName("request_correlation_id") val requestCorrelationId: String?,
    @SerializedName("service_target") val serviceTarget: String?,
    @SerializedName("diagnostic_code") val diagnosticCode: String?,
)

internal data class NativeErrorDeviceRequest(
    @SerializedName("os_version") val osVersion: String?,
    @SerializedName("connectivity") val connectivity: String?,
)
