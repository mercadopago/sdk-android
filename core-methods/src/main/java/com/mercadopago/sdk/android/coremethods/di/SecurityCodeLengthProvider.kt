package com.mercadopago.sdk.android.coremethods.di

internal interface SecurityCodeLengthProvider {
    fun getExpectedLength(): Int?

    fun setExpectedLength(
        length: Int?,
    )
}

internal class SecurityCodeLengthProviderImpl : SecurityCodeLengthProvider {
    private val holder = object : ThreadLocal<Int?>() {
        override fun initialValue(): Int? = null
    }

    override fun getExpectedLength(): Int? = holder.get()

    override fun setExpectedLength(
        length: Int?,
    ) {
        holder.set(length)
    }
}
