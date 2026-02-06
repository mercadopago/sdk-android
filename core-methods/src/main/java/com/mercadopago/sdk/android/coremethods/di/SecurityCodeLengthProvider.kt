package com.mercadopago.sdk.android.coremethods.di

/**
 * Provides the expected security code length for the current flow.
 * The caller sets the value (same as SecurityCodeTextField securityCodeSize) before invoking
 * the card token use case, so validation uses the correct length without changing the use case signature.
 */
internal interface SecurityCodeLengthProvider {

    fun getExpectedLength(): Int?

    fun setExpectedLength(length: Int?)
}

/**
 * Thread-local implementation so concurrent calls do not overwrite each other.
 * Uses initialValue() override for API 23 compatibility (ThreadLocal.withInitial requires API 26).
 */
internal class SecurityCodeLengthProviderImpl : SecurityCodeLengthProvider {

    private val holder = object : ThreadLocal<Int?>() {
        override fun initialValue(): Int? = null
    }

    override fun getExpectedLength(): Int? = holder.get()

    override fun setExpectedLength(length: Int?) {
        holder.set(length)
    }
}
