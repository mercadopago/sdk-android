package com.mercadopago.sdk.android.threeds.interactor

import com.mercadopago.sdk.android.initializer.MercadoPagoSDK

/**
 * Extension property to access the MPThreeDS functionality from MercadoPagoSDK.
 * This provides a convenient way to access 3DS authentication methods.
 * The MPThreeDS instance is automatically created using the same context from MercadoPagoSDK.
 *
 * Example:
 * ```kotlin
 * val threeDS = MercadoPagoSDK.getInstance().threeDS
 * threeDS.createTransaction("visa")
 * val params = threeDS.getAuthenticationRequestParameters()
 * // ... perform 3DS authentication
 * ```
 *
 * @return MPThreeDS instance for 3DS authentication operations
 */
val MercadoPagoSDK.threeDS: MPThreeDS
    get() = MPThreeDS.getInstance()
