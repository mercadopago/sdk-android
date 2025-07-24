package com.mercadopago.sdk.android.threeds.data.datasource.remote

import com.mercadopago.sdk.android.threeds.data.model.ThreeDSBody
import com.mercadopago.sdk.android.threeds.data.remote.service.ThreeDSService
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSAuthenticationResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Implementation of ThreeDSRemoteDataSource using Retrofit service.
 */
internal class ThreeDSRemoteDataSourceImpl(
    private val threeDSService: ThreeDSService,
) : ThreeDSRemoteDataSource {

    override fun authenticate(body: ThreeDSBody): Flow<MPThreeDSAuthenticationResponse> = flow {
        emit(threeDSService.authenticate(body))
    }
}
