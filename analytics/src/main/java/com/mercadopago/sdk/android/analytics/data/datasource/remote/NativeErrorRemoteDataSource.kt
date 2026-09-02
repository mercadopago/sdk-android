package com.mercadopago.sdk.android.analytics.data.datasource.remote

import com.mercadopago.sdk.android.analytics.data.remote.models.request.NativeErrorRequest

internal interface NativeErrorRemoteDataSource {
    suspend fun report(request: NativeErrorRequest): Boolean
}
