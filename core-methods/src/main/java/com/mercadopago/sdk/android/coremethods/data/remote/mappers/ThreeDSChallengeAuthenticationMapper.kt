package com.mercadopago.sdk.android.coremethods.data.remote.mappers

import com.mercadopago.sdk.android.coremethods.data.remote.response.ThreeDSChallengeAuthenticationResponse
import com.mercadopago.sdk.android.coremethods.data.remote.response.ThreeDSChallengeDataResponse
import com.mercadopago.sdk.android.coremethods.domain.model.ThreeDSChallengeAuthentication
import com.mercadopago.sdk.android.coremethods.domain.provider.models.ThreeDSAuthenticationModel

internal fun ThreeDSChallengeAuthenticationResponse.toModel(): ThreeDSChallengeAuthentication =
    ThreeDSChallengeAuthentication(
        status = this.status.orEmpty(),
        data = this.data?.toModel(),
    )

internal fun ThreeDSChallengeDataResponse.toModel(): ThreeDSAuthenticationModel =
    ThreeDSAuthenticationModel(
        acsReferenceNumber = this.acsReferenceNumber.orEmpty(),
        acsSignedContent = this.acsSignedContent.orEmpty(),
        acsTransID = this.acsTransId.orEmpty(),
        threeDSServerTransID = this.threeDsServerTransId.orEmpty(),
        dsTransID = this.dsTransId.orEmpty(),
        callbackUrl = this.callbackUrl.orEmpty(),
    )
