package com.mercadopago.sdk.android.coremethods.domain.provider

internal class ThreeDSProviderManager {
    @Volatile
    private var provider: ThreeDSProvider? = null

    /**
     * Sets the 3DS provider implementation.
     * This method is thread-safe and can be called from any thread.
     *
     * @param provider The ThreeDSProvider implementation to use
     */
    fun setProvider(
        provider: ThreeDSProvider,
    ) {
        synchronized(this) {
            this.provider = provider
        }
    }

    /**
     * Gets the current 3DS provider instance.
     * Returns null if no provider has been set.
     *
     * @return The current ThreeDSProvider instance, or null if not set
     */
    fun getProvider(): ThreeDSProvider? {
        return provider
    }

    /**
     * Checks if a provider has been set.
     *
     * @return true if a provider is available, false otherwise
     */
    fun hasProvider(): Boolean {
        return provider != null
    }
}
