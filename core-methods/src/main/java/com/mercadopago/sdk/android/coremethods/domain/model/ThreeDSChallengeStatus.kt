package com.mercadopago.sdk.android.coremethods.domain.model


internal enum class ThreeDSChallengeStatus(val value: String) {
    COMPLETED("COMPLETED"),
    CANCELLED("CANCELLED"),
    ERROR("ERROR"),
    TIMEOUT("TIMEOUT"),
}
