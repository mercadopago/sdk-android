package com.mercadopago.sdk.android.threeds.data.mappers

import com.mercadopago.sdk.android.threeds.data.model.MPThreeDSAuthenticationParams
import com.mercadopago.sdk.android.threeds.data.model.ThreeDSAuthRequestParameters
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSAuthenticationModel
import com.mercadopago.sdk.android.threeds.domain.model.params.MPThreeDSRequestParams

internal fun ThreeDSAuthRequestParameters.toModel(): MPThreeDSRequestParams =
    MPThreeDSRequestParams(
        sdkAppId = this.sdkAppId,
        deviceData = this.deviceData,
        sdkEphemeralPublicKey = this.sdkEphemeralPublicKey,
        sdkReferenceNumber = this.sdkReferenceNumber,
        sdkTransactionId = this.sdkTransactionId,
    )

internal fun MPThreeDSAuthenticationModel.toParams(): MPThreeDSAuthenticationParams =
    MPThreeDSAuthenticationParams(
        threeDSServerTransID = this.threeDSServerTransID,
        acsReferenceNumber = this.acsReferenceNumber,
        dsTransID = this.dsTransID,
        acsTransID = this.acsTransID,
        acsSignedContent = this.acsSignedContent,
    )
