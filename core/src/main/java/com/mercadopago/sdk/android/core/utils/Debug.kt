package com.mercadopago.sdk.android.core.utils

import com.mercadopago.sdk.android.core.BuildConfig
/**
* Use this method to check if the app is in debug mode.
**/
fun isDebugApp(): Boolean =
    BuildConfig.DEBUG
