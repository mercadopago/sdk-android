package com.mercadopago.sdk.android.coremethods.data.remote.mappers

import com.mercadopago.sdk.android.coremethods.data.remote.response.ThreeDSChallengeAuthenticationResponse
import com.mercadopago.sdk.android.coremethods.data.remote.response.ThreeDSChallengeDataResponse
import com.mercadopago.sdk.android.coremethods.domain.model.ThreeDSChallengeAuthentication
import com.mercadopago.sdk.android.coremethods.domain.provider.models.ThreeDSAuthenticationModel

internal fun ThreeDSChallengeAuthenticationResponse.toModel(): ThreeDSChallengeAuthentication =
    ThreeDSChallengeAuthentication(
        status = status.orEmpty(),
        threeDSAuthenticationModel = data?.toModel(),
    )

internal fun ThreeDSChallengeDataResponse.toModel(): ThreeDSAuthenticationModel =
    ThreeDSAuthenticationModel(
        acsReferenceNumber = acsReferenceNumber.orEmpty(),
        acsSignedContent = acsSignedContent.orEmpty(),
        acsTransID = acsTransId.orEmpty(),
        threeDSServerTransID = threeDsServerTransId.orEmpty(),
        dsTransID = dsTransId.orEmpty(),
        callbackUrl = callbackUrl.orEmpty(),
    )
