package com.mercadopago.sdk.android.threeds.data.datasource.remote

import com.mercadopago.sdk.android.threeds.data.remote.request.ThreeDSAuthenticationRequest
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSAuthenticationModel
import kotlinx.coroutines.flow.Flow

internal interface ThreeDSRemoteDataSource {
    fun authenticate(request: ThreeDSAuthenticationRequest): Flow<MPThreeDSAuthenticationModel>
}
