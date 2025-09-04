package com.mercadopago.sdk.android.threeds

import com.mercadopago.sdk.android.threeds.domain.model.params.ThreeDSAuthenticationParams

// Mock data factories for testing

internal fun mockThreeDSAuthRequestParameters() = MPThreeDSAuthRequestParameters(
    sdkAppId = "mock_sdk_app_id",
    deviceData = "mock_device_data",
    sdkEphemeralPublicKey = "mock_ephemeral_pub_key",
    sdkReferenceNumber = "mock_reference_number",
    sdkTransactionId = "mock_transaction_id",
)

internal fun mockThreeDSAuthenticationParams() = ThreeDSAuthenticationParams(
    token = "mock_token",
    sdkAppId = "mock_sdk_app_id",
    sdkEncData = "mock_enc_data",
    sdkEphemPubKey = "mock_ephemeral_pub_key",
    sdkMaxTimeout = "10",
    sdkReferenceNumber = "mock_reference_number",
    sdkTransId = "mock_transaction_id",
)

internal fun mockThreeDSAuthenticationResponse(response: String = "AUTHORIZED") = MPThreeDSAuthenticationResponse(
    response = response,
    threeDSServerTransID = "mock_3ds_server_trans_id",
    acsReferenceNumber = "mock_acs_reference_number",
    dsTransID = "mock_ds_trans_id",
    acsTransID = "mock_acs_trans_id",
    acsSignedContent = "mock_acs_signed_content",
)

internal fun mockThreeDSChallengeResponse() = MPThreeDSChallengeResponse(
    threeDSServerTransID = "mock_3ds_server_trans_id",
    acsReferenceNumber = "mock_acs_reference_number",
    dsTransID = "mock_ds_trans_id",
    acsTransID = "mock_acs_trans_id",
    acsSignedContent = "mock_acs_signed_content",
)

internal fun mockThreeDSAuthenticated(challengeCompleted: Boolean = false) = MPThreeDSAuthenticated(
    challengeResponse = mockThreeDSChallengeResponse(),
    challengeCompleted = challengeCompleted,
)

internal fun mockThreeDSChallengeError() = MPThreeDSChallengeError(
    code = "MOCK_ERROR",
    message = "Mock error for testing"
)
