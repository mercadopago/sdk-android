package com.mercadopago.sdk.android.analytics.data.remote.mapper

import android.content.Context
import android.os.Build
import com.mercadopago.sdk.android.analytics.data.remote.models.request.NativeErrorDetailRequest
import com.mercadopago.sdk.android.analytics.data.remote.models.request.NativeErrorDeviceRequest
import com.mercadopago.sdk.android.analytics.data.remote.models.request.NativeErrorRequest
import com.mercadopago.sdk.android.analytics.data.remote.models.request.NativeErrorSourceRequest
import com.mercadopago.sdk.android.analytics.domain.models.PendingNativeError
import com.mercadopago.sdk.android.core.BuildConfig
import com.mercadopago.sdk.android.core.utils.NetworkType
import com.mercadopago.sdk.android.core.utils.checkNetworkType

internal class NativeErrorRequestMapper(
    private val context: Context,
    private val siteId: String,
    private val sdkVersion: String = BuildConfig.SdkVersion,
    private val osVersion: () -> String = { Build.VERSION.RELEASE },
    private val networkType: (Context) -> NetworkType = ::checkNetworkType,
) {
    fun map(pending: PendingNativeError): NativeErrorRequest {
        val nativeError = pending.error
        val safeOsVersion = osVersion().takeIf { SAFE_OS_VERSION.matches(it) && it.length <= MAX_OS_VERSION_LENGTH }
        val connectivity = when (networkType(context)) {
            NetworkType.WIFI -> "wifi"
            NetworkType.CELLULAR_3G,
            NetworkType.CELLULAR_4G,
            NetworkType.CELLULAR_5G,
            NetworkType.CELLULAR_UNKNOWN -> "cellular"
            NetworkType.NONE -> "none"
        }
        return NativeErrorRequest(
            eventId = pending.eventId,
            occurredAt = pending.occurredAt,
            source = NativeErrorSourceRequest(
                sdkName = "openplatform_sdk_android",
                sdkVersion = sdkVersion,
                hostPlatform = "android",
                sdkTechnology = "native",
                module = nativeError.operation.module.value,
                operation = nativeError.operation.value,
            ),
            siteId = siteId,
            error = NativeErrorDetailRequest(
                code = nativeError.code.value,
                category = nativeError.code.category,
                critical = nativeError.code.critical,
                statusCode = nativeError.statusCode?.takeIf { it in MIN_HTTP_STATUS..MAX_HTTP_STATUS },
                requestCorrelationId = nativeError.requestCorrelationId?.takeIf {
                    it.length in 1..MAX_CORRELATION_ID_LENGTH && SAFE_CORRELATION_ID.matches(it)
                },
                serviceTarget = nativeError.operation.serviceTarget,
                diagnosticCode = nativeError.diagnostic?.value,
            ),
            device = NativeErrorDeviceRequest(
                osVersion = safeOsVersion,
                connectivity = connectivity,
            ),
        )
    }

    private companion object {
        const val MAX_CORRELATION_ID_LENGTH = 128
        const val MAX_OS_VERSION_LENGTH = 32
        const val MIN_HTTP_STATUS = 100
        const val MAX_HTTP_STATUS = 599
        val SAFE_CORRELATION_ID = Regex("[A-Za-z0-9._:-]+")
        val SAFE_OS_VERSION = Regex("[0-9A-Za-z._+\\-]+")
    }
}
