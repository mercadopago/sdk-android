package com.mercadopago.sdk.android.analytics.domain.repository

import com.mercadopago.sdk.android.analytics.domain.models.PendingNativeError

internal interface NativeErrorRepository {
    suspend fun report(error: PendingNativeError): Boolean
}
