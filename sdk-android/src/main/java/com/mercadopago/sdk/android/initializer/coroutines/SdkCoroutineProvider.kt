package com.mercadopago.sdk.android.initializer.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

internal object SdkCoroutineProvider {

    internal fun provideSDKCoroutineScope(): CoroutineScope = CoroutineScope(Dispatchers.IO)
}
