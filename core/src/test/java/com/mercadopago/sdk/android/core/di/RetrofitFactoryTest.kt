package com.mercadopago.sdk.android.core.di

import junit.framework.TestCase.assertNotNull
import org.junit.Test

class RetrofitFactoryTest {

    @Test
    fun `test RetrofitServiceFactory creation`() {
        val publicKey = "your_public_key"
        val url = "https://yourapi.com/"

        val factory = RetrofitServiceFactory(publicKey, url)

        val someService = factory.createService(SomeService::class.java)

        assertNotNull(someService)
    }

    internal interface SomeService
}
