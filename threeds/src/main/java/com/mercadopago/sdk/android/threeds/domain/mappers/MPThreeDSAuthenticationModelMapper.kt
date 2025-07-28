package com.mercadopago.sdk.android.threeds.domain.mappers

import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSAuthenticationModel
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSChallengeModel

internal fun MPThreeDSAuthenticationModel.toChallengeModel() =
    MPThreeDSChallengeModel(
        threeDSServerTransID = threeDSServerTransID,
        acsReferenceNumber = acsReferenceNumber,
        dsTransID = dsTransID,
        acsTransID = acsTransID,
        acsSignedContent = acsSignedContent
    )
