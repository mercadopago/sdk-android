package com.mercadopago.sdk.android.checkout.domain.callback

import com.mercadopago.sdk.android.checkout.domain.model.MPPaymentData
import com.mercadopago.sdk.android.checkout.domain.model.MPUserCancelledContext
import io.mockk.mockk
import io.mockk.verify
import org.junit.After
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertSame

internal class CheckoutCallbackHolderTest {
    @After
    fun tearDown() {
        CheckoutCallbackHolder.setCallback<MPPaymentData.CardSave, MPUserCancelledContext.CardSave>(null)
        CheckoutCallbackHolder.setActivityCallback(null)
        CheckoutCallbackHolder.setEmailChangeCallback(null)
    }

    @Test
    fun `given callback is set then notify invokes it with the result`() {
        val result = mockk<MercadoPagoCheckoutResult<MPPaymentData.CardSave, MPUserCancelledContext.CardSave>>()
        val callback: (MercadoPagoCheckoutResult<MPPaymentData.CardSave, MPUserCancelledContext.CardSave>) -> Unit =
            mockk(relaxed = true)
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
        val result = mockk<MercadoPagoCheckoutResult<MPPaymentData.CardSave, MPUserCancelledContext.CardSave>>()
        val callback: (MercadoPagoCheckoutResult<MPPaymentData.CardSave, MPUserCancelledContext.CardSave>) -> Unit =
            mockk(relaxed = true)
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
        CheckoutCallbackHolder.setCallback<MPPaymentData.CardSave, MPUserCancelledContext.CardSave> { invokeCount++ }
        CheckoutCallbackHolder.setEmailChangeCallback {}

        CheckoutCallbackHolder.notify(mockk())
        CheckoutCallbackHolder.notify(mockk())

        assertEquals(1, invokeCount)
        assertNull(CheckoutCallbackHolder.emailChangeCallbackOrNull())
    }

    @Test
    fun `given callback is null then notify does not throw`() {
        CheckoutCallbackHolder.setCallback<MPPaymentData.CardSave, MPUserCancelledContext.CardSave>(null)

        CheckoutCallbackHolder.notify(mockk())
    }

    @Test
    fun `given activityCallback is null then notify does not throw`() {
        CheckoutCallbackHolder.setActivityCallback(null)

        CheckoutCallbackHolder.notify(mockk())
    }

    @Test
    fun `given dismiss is called then only activity callback is invoked`() {
        val resultCallback: (
            MercadoPagoCheckoutResult<MPPaymentData.CardSave, MPUserCancelledContext.CardSave>,
        ) -> Unit =
            mockk(relaxed = true)
        val activityCallback = mockk<() -> Unit>(relaxed = true)
        CheckoutCallbackHolder.setCallback(resultCallback)
        CheckoutCallbackHolder.setActivityCallback(activityCallback)

        CheckoutCallbackHolder.dismiss()

        verify { activityCallback() }
        verify(exactly = 0) { resultCallback(any()) }
    }

    @Test
    fun `given dismiss is called then callbacks are cleared`() {
        var activityInvokeCount = 0
        var resultInvokeCount = 0
        CheckoutCallbackHolder.setActivityCallback { activityInvokeCount++ }
        CheckoutCallbackHolder.setCallback<MPPaymentData.CardSave, MPUserCancelledContext.CardSave> {
            resultInvokeCount++
        }

        CheckoutCallbackHolder.dismiss()
        CheckoutCallbackHolder.notify(mockk())

        assertEquals(1, activityInvokeCount)
        assertEquals(0, resultInvokeCount)
    }

    @Test
    fun `given setCallback with null then previous callback is not invoked on notify`() {
        val callback: (MercadoPagoCheckoutResult<MPPaymentData.CardSave, MPUserCancelledContext.CardSave>) -> Unit =
            mockk(relaxed = true)
        CheckoutCallbackHolder.setCallback(callback)
        CheckoutCallbackHolder.setCallback<MPPaymentData.CardSave, MPUserCancelledContext.CardSave>(null)

        CheckoutCallbackHolder.notify(mockk())

        verify(exactly = 0) { callback(any()) }
    }

    @Test
    fun `given email change callback is set then it can be retrieved`() {
        val callback = {}

        CheckoutCallbackHolder.setEmailChangeCallback(callback)

        assertSame(callback, CheckoutCallbackHolder.emailChangeCallbackOrNull())
    }
}
