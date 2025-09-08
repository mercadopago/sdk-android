package com.mercadopago.sdk.android.threeds

import com.mercadopago.sdk.android.initializer.MercadoPagoSDK
import com.mercadopago.sdk.android.threeds.interactor.MPThreeDS

/**
 * Extension property to access the MPThreeDS functionality from MercadoPagoSDK.
 * This provides a convenient way to access 3DS authentication methods.
 *
 * Example:
 * ```kotlin
 * val threeDS = MercadoPagoSDK.getInstance().threeDS
 * threeDS.requestChallenge(
 *     activity = this,
 *     cardToken = "your_card_token",
 *     paymentMethodId = "your_payment_method_id",
 *     delegate = myDelegate
 * )
 * ```
 *
 * @return MPThreeDS instance for 3DS authentication operations
 * @throws MPThreeDSNotInitializedException if MPThreeDS is not initialized
 */
val MercadoPagoSDK.threeDS: MPThreeDS
    get() = MPThreeDS.getInstance()
