package com.mercadopago.sdk.android.analytics.observability.data.remote.mapper

import android.content.Context
import android.os.Build
import com.mercadopago.sdk.android.analytics.observability.data.remote.models.request.NativeErrorDetailRequest
import com.mercadopago.sdk.android.analytics.observability.data.remote.models.request.NativeErrorDeviceRequest
import com.mercadopago.sdk.android.analytics.observability.data.remote.models.request.NativeErrorRequest
import com.mercadopago.sdk.android.analytics.observability.data.remote.models.request.NativeErrorSourceRequest
import com.mercadopago.sdk.android.analytics.observability.domain.classifier.validNativeCorrelationId
import com.mercadopago.sdk.android.analytics.observability.domain.classifier.validNativeHttpStatus
import com.mercadopago.sdk.android.analytics.observability.domain.models.NativeError
import com.mercadopago.sdk.android.analytics.observability.domain.models.NativeObservabilityConfiguration
import com.mercadopago.sdk.android.analytics.observability.domain.models.PendingNativeError
import com.mercadopago.sdk.android.core.utils.NetworkType

private const val MAX_OS_VERSION_LENGTH = 32
private val SAFE_OS_VERSION = Regex("[0-9A-Za-z._+\\-]+")

private fun PendingNativeError.toNativeErrorSource(
    configuration: NativeObservabilityConfiguration,
) = NativeErrorSourceRequest(
    sdkName = configuration.sdkName,
    sdkVersion = configuration.sdkVersion,
    hostPlatform = "android",
    sdkTechnology = "native",
    module = error.operation.module.value,
    operation = error.operation.value,
)

private fun NativeError.toNativeErrorDetail() = NativeErrorDetailRequest(
    code = code.value,
    category = code.category,
    critical = code.critical,
    statusCode = statusCode?.validNativeHttpStatus(),
    requestCorrelationId = requestCorrelationId?.validNativeCorrelationId(),
    serviceTarget = operation.serviceTarget,
    diagnosticCode = diagnostic?.value,
)

private fun NetworkType.toConnectivity(): String = when (this) {
    NetworkType.WIFI -> "wifi"
    NetworkType.CELLULAR_3G,
    NetworkType.CELLULAR_4G,
    NetworkType.CELLULAR_5G,
    NetworkType.CELLULAR_UNKNOWN -> "cellular"
    NetworkType.NONE -> "none"
}

internal fun PendingNativeError.toRequest(
    configuration: NativeObservabilityConfiguration,
    device: NativeErrorDeviceRequest,
): NativeErrorRequest = NativeErrorRequest(
    eventId = eventId,
    occurredAt = occurredAt,
    source = toNativeErrorSource(configuration),
    siteId = configuration.siteId,
    error = error.toNativeErrorDetail(),
    device = device,
)

internal fun Context.toNativeErrorDevice(networkType: (Context) -> NetworkType) = NativeErrorDeviceRequest(
    osVersion = Build.VERSION.RELEASE.orEmpty().toSafeOsVersion(),
    connectivity = networkType(this).toConnectivity(),
)

internal fun String.toSafeOsVersion(): String? = takeIf {
    SAFE_OS_VERSION.matches(it) && length <= MAX_OS_VERSION_LENGTH
}
