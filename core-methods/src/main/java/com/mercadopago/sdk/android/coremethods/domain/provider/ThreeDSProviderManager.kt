package com.mercadopago.sdk.android.coremethods.domain.provider

internal class ThreeDSProviderManager {
    @Volatile
    private var provider: ThreeDSProvider? = null

    fun setProvider(
        provider: ThreeDSProvider,
    ) = synchronized(this) {
        this.provider = provider
    }

    fun getProvider(): ThreeDSProvider? = provider

    fun hasProvider(): Boolean = provider != null
}
