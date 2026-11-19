package com.mercadopago.sdk.android.googlepay.domain.model

internal enum class GooglePayEnvironment {
    TEST,
    PRODUCTION,
    ;

    companion object {
        private const val TEST_KEY_PREFIX = "TEST-"

        fun fromPublicKey(
            publicKey: String,
        ): GooglePayEnvironment = if (publicKey.startsWith(TEST_KEY_PREFIX)) TEST else PRODUCTION
    }
}
