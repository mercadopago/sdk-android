package com.mercadopago.sdk.android.threeds.data.datasource.remote

import com.mercadopago.sdk.android.threeds.data.model.ThreeDSBody
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSAuthenticationResponse
import kotlinx.coroutines.flow.Flow

/**
 * Interface for remote data source operations related to 3DS authentication.
 */
internal interface ThreeDSRemoteDataSource {

    /**
     * Authenticates a card token using 3DS authentication.
     *
     * @param body The request body containing card token and 3DS parameters
     * @return Flow emitting the authentication response
     */
    fun authenticate(body: ThreeDSBody): Flow<MPThreeDSAuthenticationResponse>
}
