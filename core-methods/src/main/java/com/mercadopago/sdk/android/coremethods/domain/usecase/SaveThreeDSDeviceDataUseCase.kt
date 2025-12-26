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
        val validationError = validateParams(cardTokenId, appId, encData, transId)
        if (validationError != null) {
            return Result.Error(validationError)
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

    private fun validateParams(
        cardTokenId: String,
        appId: String,
        encData: String,
        transId: String,
    ): ResultError.Validation? {
        return when {
            cardTokenId.isEmpty() -> ResultError.Validation("card token id cannot be empty")
            appId.isEmpty() -> ResultError.Validation("app id cannot be empty")
            encData.isEmpty() -> ResultError.Validation("encrypted device data cannot be empty")
            transId.isEmpty() -> ResultError.Validation("transaction id cannot be empty")
            else -> null
        }
    }
}
