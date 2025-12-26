package com.mercadopago.sdk.android.coremethods.domain.model

import com.mercadopago.sdk.android.coremethods.domain.provider.models.ThreeDSAuthenticationModel

internal data class ThreeDSChallengeAuthentication(
    val status: String,
    val data: ThreeDSAuthenticationModel?,
)
