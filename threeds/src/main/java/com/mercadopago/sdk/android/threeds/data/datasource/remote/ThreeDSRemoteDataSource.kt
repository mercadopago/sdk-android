package com.mercadopago.sdk.android.threeds.data.datasource.remote

import com.mercadopago.sdk.android.threeds.data.remote.request.ThreeDSAuthenticationRequest
import com.mercadopago.sdk.android.threeds.data.remote.response.MPThreeDSAuthenticationResponse
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSAuthenticationModel
import com.mercadopago.sdk.android.threeds.domain.model.params.ThreeDSAuthenticationParams
import kotlinx.coroutines.flow.Flow

internal interface ThreeDSRemoteDataSource {
    fun authenticate(request: ThreeDSAuthenticationRequest): Flow<MPThreeDSAuthenticationModel>
}
