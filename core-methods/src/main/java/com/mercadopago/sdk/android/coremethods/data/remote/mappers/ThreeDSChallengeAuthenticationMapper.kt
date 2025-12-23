package com.mercadopago.sdk.android.coremethods.data.remote.mappers

import com.mercadopago.sdk.android.coremethods.data.remote.response.ThreeDSChallengeAuthenticationResponse
import com.mercadopago.sdk.android.coremethods.data.remote.response.ThreeDSChallengeDataResponse
import com.mercadopago.sdk.android.coremethods.domain.model.ThreeDSChallengeAuthentication
import com.mercadopago.sdk.android.coremethods.domain.model.ThreeDSChallengeData

internal fun ThreeDSChallengeAuthenticationResponse.toModel(): ThreeDSChallengeAuthentication =
    ThreeDSChallengeAuthentication(
        status = this.status.orEmpty(),
        data = this.data?.toModel(),
    )

internal fun ThreeDSChallengeDataResponse.toModel(): ThreeDSChallengeData =
    ThreeDSChallengeData(
        acsReferenceNumber = this.acsReferenceNumber.orEmpty(),
        acsSignedContent = this.acsSignedContent.orEmpty(),
        acsTransId = this.acsTransId.orEmpty(),
        threeDsServerTransId = this.threeDsServerTransId.orEmpty(),
        callbackUrl = this.callbackUrl.orEmpty(),
    )
