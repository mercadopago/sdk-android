package com.mercadopago.sdk.android.checkout.domain.callback

import io.mockk.mockk
import io.mockk.verify
import org.junit.After
import kotlin.test.Test
import kotlin.test.assertEquals

internal class CheckoutCallbackHolderTest {
    @After
    fun tearDown() {
        CheckoutCallbackHolder.setCallback(null)
        CheckoutCallbackHolder.setActivityCallback(null)
    }

    @Test
    fun `given callback is set then notify invokes it with the result`() {
        val result = mockk<MercadoPagoCheckoutResult>()
        val callback = mockk<(MercadoPagoCheckoutResult) -> Unit>(relaxed = true)
        CheckoutCallbackHolder.setCallback(callback)

        CheckoutCallbackHolder.notify(result)

        verify { callback(result) }
    }

    @Test
    fun `given activityCallback is set then notify invokes it`() {
        val activityCallback = mockk<() -> Unit>(relaxed = true)
        CheckoutCallbackHolder.setActivityCallback(activityCallback)

        CheckoutCallbackHolder.notify(mockk())

        verify { activityCallback() }
    }

    @Test
    fun `given both callbacks are set then notify invokes both`() {
        val result = mockk<MercadoPagoCheckoutResult>()
        val callback = mockk<(MercadoPagoCheckoutResult) -> Unit>(relaxed = true)
        val activityCallback = mockk<() -> Unit>(relaxed = true)
        CheckoutCallbackHolder.setCallback(callback)
        CheckoutCallbackHolder.setActivityCallback(activityCallback)

        CheckoutCallbackHolder.notify(result)

        verify { activityCallback() }
        verify { callback(result) }
    }

    @Test
    fun `given notify is called then callbacks are cleared`() {
        var invokeCount = 0
        CheckoutCallbackHolder.setCallback { invokeCount++ }

        CheckoutCallbackHolder.notify(mockk())
        CheckoutCallbackHolder.notify(mockk())

        assertEquals(1, invokeCount)
    }

    @Test
    fun `given callback is null then notify does not throw`() {
        CheckoutCallbackHolder.setCallback(null)

        CheckoutCallbackHolder.notify(mockk())
    }

    @Test
    fun `given activityCallback is null then notify does not throw`() {
        CheckoutCallbackHolder.setActivityCallback(null)

        CheckoutCallbackHolder.notify(mockk())
    }

    @Test
    fun `given setCallback with null then previous callback is not invoked on notify`() {
        val callback = mockk<(MercadoPagoCheckoutResult) -> Unit>(relaxed = true)
        CheckoutCallbackHolder.setCallback(callback)
        CheckoutCallbackHolder.setCallback(null)

        CheckoutCallbackHolder.notify(mockk())

        verify(exactly = 0) { callback(any()) }
    }
}
