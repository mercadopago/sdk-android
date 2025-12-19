package com.mercadopago.sdk.android.coremethods.domain.model

/**
 * Represents the result of a 3DS challenge authentication.
 *
 * This data class contains the authentication status and optional challenge data
 * that may be required to display the 3DS challenge UI to the user.
 *
 * @property status The authentication status. Possible values: "authenticated", "challenge"
 * @property data Optional challenge data, present only when status is "challenge"
 *
 * Example:
 * ```kotlin
 * // When authentication is successful without challenge
 * val result = ThreeDSChallengeAuthentication(
 *     status = "authenticated",
 *     data = null
 * )
 *
 * // When challenge is required
 * val result = ThreeDSChallengeAuthentication(
 *     status = "challenge",
 *     data = ThreeDSChallengeData(
 *         acsReferenceNumber = "3DS_LOA_ACS_PPFU_020100_00009",
 *         acsSignedContent = "eyJhbGciOiJQUzI1NiIsIng1YyI6...",
 *         acsTransId = "d7c1ee99-9478-44a6-b1f2-391e29c6b340",
 *         threeDsServerTransId = "f25084f0-5b16-4c0a-ae5d-b24808a95e4b",
 *         callbackUrl = "https://api.mercadopago.com/3ds/callback"
 *     )
 * )
 * ```
 *
 * @see ThreeDSChallengeData
 */
data class ThreeDSChallengeAuthentication(
    val status: String,
    val data: ThreeDSChallengeData?,
)

/**
 * Contains the challenge data required to display the 3DS challenge UI.
 *
 * This data is provided by the Access Control Server (ACS) and is used
 * to render the challenge interface for user authentication.
 *
 * @property acsReferenceNumber Reference number of the ACS (Access Control Server)
 * @property acsSignedContent Signed content from the ACS for verification
 * @property acsTransId Transaction ID in the ACS system
 * @property threeDsServerTransId Transaction ID in the 3DS server
 * @property callbackUrl URL for callback notification after challenge completion
 *
 * @see ThreeDSChallengeAuthentication
 */
data class ThreeDSChallengeData(
    val acsReferenceNumber: String,
    val acsSignedContent: String,
    val acsTransId: String,
    val threeDsServerTransId: String,
    val callbackUrl: String,
)
