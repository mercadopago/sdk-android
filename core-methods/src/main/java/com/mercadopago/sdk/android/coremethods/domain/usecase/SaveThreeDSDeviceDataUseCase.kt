package com.mercadopago.sdk.android.coremethods.domain.usecase

import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.model.params.DeviceRenderOptionsParams
import com.mercadopago.sdk.android.coremethods.domain.model.params.EphemeralPublicKeyParams
import com.mercadopago.sdk.android.coremethods.domain.model.params.SaveThreeDSDeviceDataParams
import com.mercadopago.sdk.android.coremethods.domain.repository.ThreeDSRepository
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

internal class SaveThreeDSDeviceDataUseCase(
    private val repository: ThreeDSRepository,
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
        return if (validationError != null) {
            Result.Error(validationError)
        } else {
            repository.saveThreeDSDeviceData(
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
