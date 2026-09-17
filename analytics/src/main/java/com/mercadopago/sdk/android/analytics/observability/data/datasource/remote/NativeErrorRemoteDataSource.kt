package com.mercadopago.sdk.android.analytics.observability.data.datasource.remote

import com.mercadopago.sdk.android.analytics.observability.domain.models.PendingNativeError

internal interface NativeErrorRemoteDataSource {
    suspend fun report(error: PendingNativeError): Boolean
}
