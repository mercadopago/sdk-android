package com.mercadopago.sdk.android.threeds.data.mappers

import com.mercadopago.sdk.android.threeds.data.model.MPSeverityResponse
import com.mercadopago.sdk.android.threeds.data.model.MPThreeDSWarningResponse
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSSeverity
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSWarning

internal fun MPThreeDSWarningResponse.toModel(): MPThreeDSWarning {
    return MPThreeDSWarning(
        id = this.id,
        message = this.message,
        severity = this.severity.toModel(),
    )
}

internal fun MPSeverityResponse.toModel(): MPThreeDSSeverity {
    return when (this) {
        MPSeverityResponse.LOW -> MPThreeDSSeverity.LOW
        MPSeverityResponse.MEDIUM -> MPThreeDSSeverity.MEDIUM
        MPSeverityResponse.HIGH -> MPThreeDSSeverity.HIGH
        MPSeverityResponse.NONE -> MPThreeDSSeverity.NONE
    }
}
