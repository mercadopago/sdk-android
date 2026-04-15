package com.mercadopago.sdk.android.core.utils

import androidx.annotation.RestrictTo

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
internal object FuryTokenStore {
    @Volatile
    var token: String? = null
}
