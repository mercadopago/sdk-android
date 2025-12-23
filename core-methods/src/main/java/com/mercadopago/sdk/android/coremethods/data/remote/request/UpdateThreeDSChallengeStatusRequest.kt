package com.mercadopago.sdk.android.coremethods.data.remote.request

import com.google.gson.annotations.SerializedName

/**
 * Request body for updating the status of a 3DS challenge.
 *
 * @property status The new status of the challenge (COMPLETED, CANCELLED, ERROR, TIMEOUT)
 * @property errorDetail Optional error details when status is ERROR
 */
internal data class UpdateThreeDSChallengeStatusRequest(
    @SerializedName("status")
    val status: String,
    @SerializedName("error_detail")
    val errorDetail: ErrorDetailRequest? = null,
)

/**
 * Error detail object for the update request.
 *
 * @property type The type of the error
 * @property code The error code
 */
internal data class ErrorDetailRequest(
    @SerializedName("type")
    val type: String?,
    @SerializedName("code")
    val code: String?,
)
