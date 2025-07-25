package com.mercadopago.sdk.android.threeds.domain.exceptions

/**
 * Exception thrown when trying to initialize MPThreeDS when it's already initialized.
 */
class MPThreeDSAlreadyInitializedException : RuntimeException(
    "MPThreeDS is already initialized. Call clearInstance() first if you need to reinitialize.",
)

/**
 * Exception thrown when trying to use MPThreeDS before it's initialized.
 */
class MPThreeDSNotInitializedException : RuntimeException(
    "MPThreeDS is not initialized. Call MPThreeDS.initialize(context) first.",
)
