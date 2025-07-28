package com.mercadopago.sdk.android.threeds.data.repository

import com.mercadopago.sdk.android.threeds.data.datasource.remote.ThreeDSRemoteDataSource
import com.mercadopago.sdk.android.threeds.data.remote.mappers.toRequest
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSAuthenticationModel
import com.mercadopago.sdk.android.threeds.domain.model.params.ThreeDSAuthenticationParams
import com.mercadopago.sdk.android.threeds.domain.repository.ThreeDSRepository
import kotlinx.coroutines.flow.Flow

internal class ThreeDSRepositoryImpl(
    private val remoteDataSource: ThreeDSRemoteDataSource,
) : ThreeDSRepository {
    override fun authenticate(params: ThreeDSAuthenticationParams): Flow<MPThreeDSAuthenticationModel> {
        return remoteDataSource.authenticate(params.toRequest())
    }
}
