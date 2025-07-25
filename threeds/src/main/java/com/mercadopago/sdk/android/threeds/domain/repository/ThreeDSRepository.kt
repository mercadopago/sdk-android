package com.mercadopago.sdk.android.threeds.domain.repository

import com.mercadopago.sdk.android.threeds.data.remote.response.MPThreeDSAuthenticationResponse
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSAuthenticationModel
import com.mercadopago.sdk.android.threeds.domain.model.params.ThreeDSAuthenticationParams
import kotlinx.coroutines.flow.Flow

internal interface ThreeDSRepository {
    fun authenticate(params: ThreeDSAuthenticationParams): Flow<MPThreeDSAuthenticationModel>
}
