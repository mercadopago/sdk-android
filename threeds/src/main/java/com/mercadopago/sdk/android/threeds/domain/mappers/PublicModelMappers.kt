package com.mercadopago.sdk.android.threeds.domain.mappers

import com.mercadopago.sdk.android.threeds.MPThreeDSAuthRequestParameters
import com.mercadopago.sdk.android.threeds.MPThreeDSAuthenticationResponse
import com.mercadopago.sdk.android.threeds.MPThreeDSChallengeError
import com.mercadopago.sdk.android.threeds.MPThreeDSChallengeResponse
import com.mercadopago.sdk.android.threeds.MPThreeDSChallengeResult
import com.mercadopago.sdk.android.threeds.MPThreeDSSeverity
import com.mercadopago.sdk.android.threeds.MPThreeDSWarning
import com.mercadopago.sdk.android.threeds.domain.model.MPSeverity
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSAuthenticationModel
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSChallengeModel
import com.mercadopago.sdk.android.threeds.domain.model.params.ThreeDSAuthRequestParameters
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSAuthenticated as InternalMPThreeDSAuthenticated
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSChallengeError as InternalMPThreeDSChallengeError
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSChallengeResult as InternalMPThreeDSChallengeResult
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSWarning as InternalMPThreeDSWarning

// Warning mappers
internal fun InternalMPThreeDSWarning.toPublic(): MPThreeDSWarning {
    return MPThreeDSWarning(
        id = this.id,
        message = this.message,
        severity = this.severity.toPublic()
    )
}

internal fun MPSeverity.toPublic(): MPThreeDSSeverity {
    return when (this) {
        MPSeverity.LOW -> MPThreeDSSeverity.LOW
        MPSeverity.MEDIUM -> MPThreeDSSeverity.MEDIUM
        MPSeverity.HIGH -> MPThreeDSSeverity.HIGH
        MPSeverity.NONE -> MPThreeDSSeverity.NONE
    }
}

// Auth request parameters mappers
internal fun ThreeDSAuthRequestParameters.toPublic(): MPThreeDSAuthRequestParameters {
    return MPThreeDSAuthRequestParameters(
        sdkAppId = this.sdkAppId,
        deviceData = this.deviceData,
        sdkEphemeralPublicKey = this.sdkEphemeralPublicKey,
        sdkReferenceNumber = this.sdkReferenceNumber,
        sdkTransactionId = this.sdkTransactionId
    )
}

// Authentication response mappers
internal fun MPThreeDSAuthenticationResponse.toInternal(): MPThreeDSAuthenticationModel {
    return MPThreeDSAuthenticationModel(
        response = this.response,
        threeDSServerTransID = this.threeDSServerTransID,
        acsReferenceNumber = this.acsReferenceNumber,
        dsTransID = this.dsTransID,
        acsTransID = this.acsTransID,
        acsSignedContent = this.acsSignedContent
    )
}

// Challenge result mappers
internal fun InternalMPThreeDSChallengeResult.toPublic(): MPThreeDSChallengeResult {
    return when (this) {
        is InternalMPThreeDSChallengeResult.OnSuccess -> {
            MPThreeDSChallengeResult.OnSuccess(this.result.toPublic())
        }
        is InternalMPThreeDSChallengeResult.OnError -> {
            MPThreeDSChallengeResult.OnError(this.error.toPublic())
        }
        is InternalMPThreeDSChallengeResult.OnCancel -> {
            MPThreeDSChallengeResult.OnCancel
        }
        is InternalMPThreeDSChallengeResult.OnTimedOut -> {
            MPThreeDSChallengeResult.OnTimedOut
        }
    }
}

internal fun InternalMPThreeDSAuthenticated.toPublic(): com.mercadopago.sdk.android.threeds.MPThreeDSAuthenticated {
    return com.mercadopago.sdk.android.threeds.MPThreeDSAuthenticated(
        challengeResponse = this.challengeResponse.toPublic(),
        challengeCompleted = this.challengeCompleted
    )
}

internal fun MPThreeDSChallengeModel.toPublic(): MPThreeDSChallengeResponse {
    return MPThreeDSChallengeResponse(
        threeDSServerTransID = this.threeDSServerTransID,
        acsReferenceNumber = this.acsReferenceNumber,
        dsTransID = this.dsTransID,
        acsTransID = this.acsTransID,
        acsSignedContent = this.acsSignedContent
    )
}

internal fun InternalMPThreeDSChallengeError.toPublic(): MPThreeDSChallengeError {
    return MPThreeDSChallengeError(
        code = this.code,
        message = this.message,
        details = this.details,
        cause = this.cause
    )
}
