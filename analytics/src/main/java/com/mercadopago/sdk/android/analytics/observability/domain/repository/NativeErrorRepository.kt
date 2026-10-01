package com.mercadopago.sdk.android.analytics.observability.domain.repository

import com.mercadopago.sdk.android.analytics.observability.domain.models.PendingNativeError

internal interface NativeErrorRepository {
    suspend fun report(error: PendingNativeError): Boolean
}
