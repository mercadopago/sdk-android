package com.mercadopago.sdk.android.coremethods.domain.usecase

import com.mercadopago.sdk.android.coremethods.data.remote.mappers.toParams
import com.mercadopago.sdk.android.coremethods.domain.model.CardToken
import com.mercadopago.sdk.android.coremethods.domain.model.EphemeralPublicKey
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.model.params.DeviceRenderOptionsParams
import com.mercadopago.sdk.android.coremethods.domain.provider.ThreeDSProviderManager
import com.mercadopago.sdk.android.coremethods.domain.provider.models.ThreeDSRequestParams
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import com.mercadopago.sdk.android.coremethods.domain.utils.ThreeDSErrorMessages
import com.mercadopago.sdk.android.coremethods.domain.utils.flatMap
import com.mercadopago.sdk.android.coremethods.domain.utils.map
import com.mercadopago.sdk.android.coremethods.domain.utils.suspendFlatMap
import com.mercadopago.sdk.android.coremethods.domain.utils.toEphemeralPublicKey

internal class SaveThreeDSDeviceDataOrchestratorUseCase(
    private val providerManager: ThreeDSProviderManager,
    private val createTransactionUseCase: CreateTransactionUseCase,
    private val getAuthenticationRequestParametersUseCase: GetAuthenticationRequestParametersUseCase,
    private val saveThreeDSDeviceDataUseCase: SaveThreeDSDeviceDataUseCase,
) {
    suspend operator fun invoke(
        cardToken: CardToken,
    ): Result<Unit, ResultError> {
        return validateProvider()
            .flatMap { sdkVersion ->
                createTransactionUseCase.invoke(cardToken).map { sdkVersion }
            }
            .flatMap { sdkVersion ->
                getAuthenticationRequestParametersUseCase.invoke().map { params ->
                    Pair(sdkVersion, params)
                }
            }
            .flatMap { (sdkVersion, params) ->
                params.sdkEphemeralPublicKey.toEphemeralPublicKey().map { ephemeralKey ->
                    Triple(sdkVersion, params, ephemeralKey)
                }
            }
            .suspendFlatMap { (sdkVersion, params, ephemeralKey) ->
                executeDeviceDataSave(
                    cardToken = cardToken,
                    sdkVersion = sdkVersion,
                    params = params,
                    ephemeralKey = ephemeralKey,
                )
            }
    }

    private fun validateProvider(): Result<String, ResultError> {
        val sdkVersion = providerManager.getProvider()?.sdkVersion
            ?: return Result.Error(
                ResultError.Validation(
                    message = ThreeDSErrorMessages.PROVIDER_NOT_AVAILABLE,
                ),
            )
        return Result.Success(sdkVersion)
    }

    private suspend fun executeDeviceDataSave(
        cardToken: CardToken,
        sdkVersion: String,
        params: ThreeDSRequestParams,
        ephemeralKey: EphemeralPublicKey,
    ): Result<Unit, ResultError> {
        val deviceRenderOptions = DeviceRenderOptionsParams(
            sdkInterface = SDK_INTERFACE_NATIVE,
            uiTypes = DEFAULT_UI_TYPES,
        )
        return runCatching {
            saveThreeDSDeviceDataUseCase.invoke(
                appId = params.sdkAppId,
                integratorSdkVersion = INTEGRATOR_SDK_VERSION,
                threeDsSdkVersion = sdkVersion,
                cardTokenId = cardToken.token,
                deviceRenderOptions = deviceRenderOptions,
                encData = params.deviceData,
                ephemPubKey = ephemeralKey.toParams(),
                maxTimeout = DEFAULT_MAX_TIMEOUT,
                protocolVersion = PROTOCOL_VERSION,
                referenceNumber = params.sdkReferenceNumber,
                transId = params.sdkTransactionId,
            )
        }.fold(
            onSuccess = { result -> result },
            onFailure = { throwable ->
                Result.Error(
                    ResultError.Request(
                        code = "",
                        message = "Error saving 3DS device data: ${throwable.message}",
                    ),
                )
            },
        )
    }

    companion object {
        private const val DEFAULT_MAX_TIMEOUT = 5
        private const val INTEGRATOR_SDK_VERSION = "2.2.0"
        private const val SDK_INTERFACE_NATIVE = "Native"
        private const val PROTOCOL_VERSION = "2.2.0"
        private val DEFAULT_UI_TYPES = listOf("01", "02", "03", "04", "05")
    }
}
