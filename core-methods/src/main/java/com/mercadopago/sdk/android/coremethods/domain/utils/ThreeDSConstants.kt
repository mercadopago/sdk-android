package com.mercadopago.sdk.android.coremethods.domain.utils

internal object ThreeDSErrorMessages {
    const val PROVIDER_NOT_AVAILABLE =
        "3DS provider not available. Please use setThreeDSProvider() method."
    const val FAILED_TO_GET_WARNINGS = "Failed to get 3DS warnings."
    const val CHALLENGE_DATA_NOT_AVAILABLE =
        "Challenge data not available in authentication response."
    const val FAILED_TO_EXECUTE_CHALLENGE = "Failed to execute 3DS challenge."
    const val ERROR_GETTING_WARNINGS = "Error getting warnings: "
    const val ERROR_AUTHENTICATING_CHALLENGE = "Error authenticating 3DS challenge: "
    const val ERROR_DURING_CHALLENGE = "Error during 3DS challenge: "
    const val FAILED_TO_CLOSE_TRANSACTION = "Failed to close transaction: "
    const val FAILED_TO_CREATE_TRANSACTION = "Failed to create transaction: "
    const val FAILED_TO_GET_AUTH_PARAMETERS = "Failed to get authentication request parameters. " +
        "Make sure a transaction was created."
    const val ERROR_GETTING_AUTH_PARAMETERS = "Error getting authentication request parameters: "
}

internal object ThreeDSSuccessMessages {
    const val TRANSACTION_CLOSED = "Transaction closed"
    const val TRANSACTION_CREATED = "Transaction created"
}

internal object ThreeDSErrorCodes {
    const val EMPTY = ""
    const val BAD_REQUEST = "400"
}
