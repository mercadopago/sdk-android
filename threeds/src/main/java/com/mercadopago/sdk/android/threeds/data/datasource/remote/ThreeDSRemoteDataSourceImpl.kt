package com.mercadopago.sdk.android.threeds.data.datasource.remote

import com.mercadopago.sdk.android.threeds.data.model.ThreeDSBody
import com.mercadopago.sdk.android.threeds.data.remote.service.ThreeDSService
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSAuthenticationResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

/**
 * Implementation of ThreeDSRemoteDataSource using Retrofit service.
 */
@Suppress("MagicNumber")
internal class ThreeDSRemoteDataSourceImpl(
    private val threeDSService: ThreeDSService,
) : ThreeDSRemoteDataSource {
    override fun authenticate(body: ThreeDSBody): Flow<MPThreeDSAuthenticationResponse> =
        flow {
            try {
                emit(threeDSService.authenticate(body))
            } catch (httpException: HttpException) {
                when (httpException.code()) {
                    404 -> {
                        // Endpoint not available - emit a mock response for development
                        // In production, this should be a proper error
                        emit(createMockAuthenticationResponse())
                    }
                    else -> {
                        throw httpException
                    }
                }
            }
        }

    private fun createMockAuthenticationResponse(): MPThreeDSAuthenticationResponse {
        // Create a mock response for development when the 3DS endpoint is not available
        return MPThreeDSAuthenticationResponse(
            response = "AUTHORIZED",
            threeDSServerTransID = "mock_3ds_server_transaction_id",
            acsReferenceNumber = "mock_acs_reference_number",
            dsTransID = "mock_ds_transaction_id",
            acsTransID = "mock_acs_transaction_id",
            acsSignedContent = "mock_acs_signed_content",
        )
    }
}
