package com.mercadopago.sdk.android.checkout.di

import com.mercadopago.sdk.android.checkout.data.remote.service.CardFormService
import kotlin.test.Test
import kotlin.test.assertIs
import kotlin.test.assertNotNull

internal class RetrofitFactoryTest {
    private val baseUrl = "https://api.mercadopago.com/"
    private val factory = RetrofitFactory(baseUrl)

    @Test
    fun `given baseUrl then createService returns non-null instance of requested type`() {
        val service = factory.createService(CardFormService::class.java)

        assertNotNull(service)
        assertIs<CardFormService>(service)
    }
}
