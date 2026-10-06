package com.mercadopago.sdk.android.analytics.observability.data.remote.mapper

import android.content.Context
import com.mercadopago.sdk.android.analytics.observability.data.remote.models.request.NativeErrorRequest
import com.mercadopago.sdk.android.analytics.observability.domain.models.NativeObservabilityConfiguration
import com.mercadopago.sdk.android.analytics.observability.domain.models.PendingNativeError
import com.mercadopago.sdk.android.core.utils.NetworkType
import com.mercadopago.sdk.android.core.utils.checkNetworkType

internal class NativeErrorRequestMapper(
    private val context: Context,
    private val configuration: NativeObservabilityConfiguration,
    private val networkType: (Context) -> NetworkType = ::checkNetworkType,
) {
    fun map(pending: PendingNativeError): NativeErrorRequest = pending.toRequest(
        configuration = configuration,
        device = context.toNativeErrorDevice(networkType),
    )
}
