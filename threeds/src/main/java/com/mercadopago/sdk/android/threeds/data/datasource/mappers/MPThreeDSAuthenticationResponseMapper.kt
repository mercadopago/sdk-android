package com.mercadopago.sdk.android.threeds.data.datasource.mappers

import com.mercadopago.sdk.android.threeds.data.remote.response.MPThreeDSAuthenticationResponse
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSAuthenticationModel

internal fun MPThreeDSAuthenticationResponse.toModel() =
    MPThreeDSAuthenticationModel(
        response = this.response,
        threeDSServerTransID = this.threeDSServerTransID,
        acsReferenceNumber = this.acsReferenceNumber,
        dsTransID = this.dsTransID,
        acsTransID = this.acsTransID,
        acsSignedContent = this.acsSignedContent
    )
