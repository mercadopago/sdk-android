package com.mercadopago.sdk.android.threeds.adapter.mappers

import com.mercadopago.sdk.android.coremethods.domain.provider.models.ThreeDSRequestParams
import com.mercadopago.sdk.android.threeds.domain.model.params.MPThreeDSRequestParams

/**
 * Mapper to convert between core-methods ThreeDSRequestParams and threeds MPThreeDSRequestParams.
 * This allows the adapter to translate between the two module's domain models.
 */
internal object ThreeDSRequestParamsMapper {
    /**
     * Converts MPThreeDSRequestParams (threeds model) to ThreeDSRequestParams (core-methods model).
     *
     * @param mpParams The MPThreeDSRequestParams to convert
     * @return The converted ThreeDSRequestParams
     */
    fun toThreeDSRequestParams(
        mpParams: MPThreeDSRequestParams,
    ): ThreeDSRequestParams {
        return ThreeDSRequestParams(
            sdkAppId = mpParams.sdkAppId,
            deviceData = mpParams.deviceData,
            sdkEphemeralPublicKey = mpParams.sdkEphemeralPublicKey,
            sdkReferenceNumber = mpParams.sdkReferenceNumber,
            sdkTransactionId = mpParams.sdkTransactionId,
        )
    }

    /**
     * Converts ThreeDSRequestParams (core-methods model) to MPThreeDSRequestParams (threeds model).
     *
     * @param params The ThreeDSRequestParams to convert
     * @return The converted MPThreeDSRequestParams
     */
    fun toMPThreeDSRequestParams(
        params: ThreeDSRequestParams,
    ): MPThreeDSRequestParams {
        return MPThreeDSRequestParams(
            sdkAppId = params.sdkAppId,
            deviceData = params.deviceData,
            sdkEphemeralPublicKey = params.sdkEphemeralPublicKey,
            sdkReferenceNumber = params.sdkReferenceNumber,
            sdkTransactionId = params.sdkTransactionId,
        )
    }
}
