package com.mercadopago.sdk.android.coremethods.data.remote.mappers

import com.mercadopago.sdk.android.coremethods.data.remote.request.ErrorDetailRequest
import com.mercadopago.sdk.android.coremethods.data.remote.request.UpdateThreeDSChallengeStatusRequest
import com.mercadopago.sdk.android.coremethods.domain.model.params.UpdateThreeDSChallengeStatusParams

internal fun UpdateThreeDSChallengeStatusParams.toRequest(): UpdateThreeDSChallengeStatusRequest =
    UpdateThreeDSChallengeStatusRequest(
        status = status.name,
        errorDetail = errorDetail?.let {
            ErrorDetailRequest(
                type = it.type,
                code = it.code,
            )
        },
    )
