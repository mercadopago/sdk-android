package com.mercadopago.sdk.android.analytics.data.datasource.remote

import com.mercadopago.sdk.android.analytics.data.remote.models.request.NativeErrorRequest
import com.mercadopago.sdk.android.analytics.data.remote.service.NativeErrorService
import kotlinx.coroutines.CancellationException

internal class NativeErrorRemoteDataSourceImpl(
    private val service: NativeErrorService,
) : NativeErrorRemoteDataSource {
    override suspend fun report(request: NativeErrorRequest): Boolean = try {
        service.report(request).code() == HTTP_ACCEPTED
    } catch (cancellation: CancellationException) {
        throw cancellation
    } catch (_: Exception) {
        false
    }

    private companion object {
        const val HTTP_ACCEPTED = 202
    }
}
