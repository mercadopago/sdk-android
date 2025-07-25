package com.mercadopago.sdk.android.threeds.data.repository

import com.mercadopago.sdk.android.threeds.data.datasource.remote.ThreeDSRemoteDataSource
import com.mercadopago.sdk.android.threeds.data.model.ThreeDSBody
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSAuthenticationResponse
import com.mercadopago.sdk.android.threeds.domain.repository.ThreeDSRepository
import kotlinx.coroutines.flow.Flow

/**
 * Implementation of ThreeDSRepository using remote data source.
 */
internal class ThreeDSRepositoryImpl(
    private val remoteDataSource: ThreeDSRemoteDataSource,
) : ThreeDSRepository {
    override fun authenticate(body: ThreeDSBody): Flow<MPThreeDSAuthenticationResponse> {
        return remoteDataSource.authenticate(body)
    }
}
