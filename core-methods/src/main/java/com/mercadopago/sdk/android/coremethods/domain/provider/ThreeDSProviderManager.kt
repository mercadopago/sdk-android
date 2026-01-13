package com.mercadopago.sdk.android.coremethods.domain.provider

internal class ThreeDSProviderManager {
    @Volatile
    private var provider: ThreeDSProvider? = null

    fun setProvider(
        provider: ThreeDSProvider,
    ) {
        synchronized(this) {
            this.provider = provider
        }
    }

    fun getProvider(): ThreeDSProvider? {
        return provider
    }

    fun hasProvider(): Boolean {
        return provider != null
    }
}
