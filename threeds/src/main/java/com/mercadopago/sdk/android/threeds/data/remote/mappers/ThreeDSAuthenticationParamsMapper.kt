package com.mercadopago.sdk.android.threeds.data.remote.mappers

import com.mercadopago.sdk.android.threeds.data.remote.request.ThreeDSAuthenticationRequest
import com.mercadopago.sdk.android.threeds.domain.model.params.ThreeDSAuthenticationParams

internal fun ThreeDSAuthenticationParams.toRequest() =
    ThreeDSAuthenticationRequest(
        token = this.token,
        sdkAppId = this.sdkAppId,
        sdkEncData = this.sdkEncData,
        sdkEphemPubKey = this.sdkEphemPubKey,
        sdkMaxTimeout = this.sdkMaxTimeout,
        sdkReferenceNumber = this.sdkReferenceNumber,
        sdkTransId = this.sdkTransId
    )
