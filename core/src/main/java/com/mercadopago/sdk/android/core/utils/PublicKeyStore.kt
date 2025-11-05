package com.mercadopago.sdk.android.core.utils

import androidx.annotation.RestrictTo

/**
 * Public Key store
 * @property publicKey - public key
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
object PublicKeyStore {
    @Volatile
    var publicKey: String? = null
}
