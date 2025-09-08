package com.mercadopago.sdk.android.threeds.data.mappers

import com.mercadopago.sdk.android.threeds.data.model.MPThreeDSAuthenticationParams
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSChallengeModel

internal fun MPThreeDSAuthenticationParams.toChallengeModel() =
    MPThreeDSChallengeModel(
        threeDSServerTransID = threeDSServerTransID,
        acsReferenceNumber = acsReferenceNumber,
        dsTransID = dsTransID,
        acsTransID = acsTransID,
        acsSignedContent = acsSignedContent
    )
