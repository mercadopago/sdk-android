package com.mercadopago.sdk.android.coremethods.domain.usecase

import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.model.params.DeviceRenderOptionsParams
import com.mercadopago.sdk.android.coremethods.domain.model.params.EphemeralPublicKeyParams
import com.mercadopago.sdk.android.coremethods.domain.model.params.SaveThreeDSDeviceDataParams
import com.mercadopago.sdk.android.coremethods.domain.repository.CoreMethodsRepository
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

/**
 * Use case responsible for saving 3DS device data to initiate the authentication process.
 *
 * This use case validates the input parameters and delegates the API call to the repository.
 */
internal class SaveThreeDSDeviceDataUseCase(
    private val repository: CoreMethodsRepository,
) {
    @Suppress("LongParameterList")
    suspend operator fun invoke(
        appId: String,
        integratorSdkVersion: String,
        threeDsSdkVersion: String,
        cardTokenId: String,
        deviceRenderOptions: DeviceRenderOptionsParams,
        encData: String,
        ephemPubKey: EphemeralPublicKeyParams,
        maxTimeout: Int,
        protocolVersion: String,
        referenceNumber: String,
        transId: String,
    ): Result<Unit, ResultError> {
        if (cardTokenId.isEmpty()) {
            return Result.Error(ResultError.Validation("card token id cannot be empty"))
        }
        if (appId.isEmpty()) {
            return Result.Error(ResultError.Validation("app id cannot be empty"))
        }
        if (encData.isEmpty()) {
            return Result.Error(ResultError.Validation("encrypted device data cannot be empty"))
        }
        if (transId.isEmpty()) {
            return Result.Error(ResultError.Validation("transaction id cannot be empty"))
        }
        return repository.saveThreeDSDeviceData(
            SaveThreeDSDeviceDataParams(
                appId = appId,
                integratorSdkVersion = integratorSdkVersion,
                threeDsSdkVersion = threeDsSdkVersion,
                cardTokenId = cardTokenId,
                deviceRenderOptions = deviceRenderOptions,
                encData = encData,
                ephemPubKey = ephemPubKey,
                maxTimeout = maxTimeout,
                protocolVersion = protocolVersion,
                referenceNumber = referenceNumber,
                transId = transId,
            ),
        )
    }
}
