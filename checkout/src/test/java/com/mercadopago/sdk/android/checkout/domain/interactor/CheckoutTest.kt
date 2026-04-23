package com.mercadopago.sdk.android.checkout.domain.interactor

import android.content.Context
import com.mercadopago.sdk.android.checkout.di.CheckoutModulesProvider
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.Koin
import kotlin.test.assertNotNull
import kotlin.test.assertSame

internal class CheckoutTest {
    private val context = mockk<Context>(relaxed = true)
    private val mockKoin = mockk<Koin>(relaxed = true)

    @Before
    fun setUp() {
        mockkConstructor(CheckoutModulesProvider::class)
        every { anyConstructed<CheckoutModulesProvider>().koinApp } returns mockKoin
        Checkout.clearInstance()
    }

    @After
    fun tearDown() {
        Checkout.clearInstance()
        unmockkAll()
    }

    @Test
    fun `getInstance creates new Checkout when instance is null`() {
        val checkout = Checkout.getInstance(context)

        assertNotNull(checkout)
        assertSame(mockKoin, checkout.koin)
    }

    @Test
    fun `getInstance returns same instance on repeated calls`() {
        val first = Checkout.getInstance(context)
        val second = Checkout.getInstance(context)

        assertSame(first, second)
    }

    @Test
    fun `clearInstance closes the Koin`() {
        Checkout.getInstance(context)

        Checkout.clearInstance()

        verify { mockKoin.close() }
    }

    @Test
    fun `clearInstance nulls the instance so next getInstance creates fresh`() {
        Checkout.getInstance(context)
        Checkout.clearInstance()

        val freshKoin = mockk<Koin>(relaxed = true)
        every { anyConstructed<CheckoutModulesProvider>().koinApp } returns freshKoin

        val second = Checkout.getInstance(context)

        assertNotNull(second)
        assertSame(freshKoin, second.koin)
    }
}
