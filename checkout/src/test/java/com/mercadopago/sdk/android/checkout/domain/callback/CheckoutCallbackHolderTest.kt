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
        CheckoutCallbackHolder.setStatusScreenExitCallback(null)
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

    @Test
    fun `notifyKeepingHostOpen delivers the result but does not finish the host`() {
        // Given
        val result = mockk<MercadoPagoCheckoutResult<MPPaymentData.CardSave, MPUserCancelledContext.CardSave>>()
        val callback: (MercadoPagoCheckoutResult<MPPaymentData.CardSave, MPUserCancelledContext.CardSave>) -> Unit =
            mockk(relaxed = true)
        val activityCallback = mockk<() -> Unit>(relaxed = true)
        CheckoutCallbackHolder.setCallback(callback)
        CheckoutCallbackHolder.setActivityCallback(activityCallback)

        // When
        CheckoutCallbackHolder.notifyKeepingHostOpen(result)

        // Then
        verify { callback(result) }
        verify(exactly = 0) { activityCallback() }
    }

    @Test
    fun `given a result already delivered when Status Screen closes then dismiss still finishes the host`() {
        // Given
        val delivered = mutableListOf<MercadoPagoCheckoutResult<*, *>>()
        val activityCallback = mockk<() -> Unit>(relaxed = true)
        CheckoutCallbackHolder.setCallback<MPPaymentData.CardSave, MPUserCancelledContext.CardSave> {
            delivered += it
        }
        CheckoutCallbackHolder.setActivityCallback(activityCallback)
        val success = mockk<MercadoPagoCheckoutResult<MPPaymentData.CardSave, MPUserCancelledContext.CardSave>>()
        CheckoutCallbackHolder.notifyKeepingHostOpen(success)

        // When
        CheckoutCallbackHolder.dismiss()

        // Then
        assertEquals(listOf<MercadoPagoCheckoutResult<*, *>>(success), delivered)
        verify { activityCallback() }
    }

    @Test
    fun `given status screen exit callback is set then it can be retrieved`() {
        val callback = {}

        CheckoutCallbackHolder.setStatusScreenExitCallback(callback)

        assertSame(callback, CheckoutCallbackHolder.statusScreenExitCallbackOrNull())
    }

    @Test
    fun `given dismiss is called then the status screen exit callback is cleared`() {
        CheckoutCallbackHolder.setStatusScreenExitCallback {}

        CheckoutCallbackHolder.dismiss()

        assertNull(CheckoutCallbackHolder.statusScreenExitCallbackOrNull())
    }

    @Test
    fun `given dismiss finishes the host then it returns true`() {
        // Given / When
        val finished = CheckoutCallbackHolder.dismiss()

        // Then
        assertEquals(true, finished)
    }

    @Test
    fun `given dismiss re-entered from the activity callback then the inner call returns false`() {
        // Given
        var innerResult: Boolean? = null
        CheckoutCallbackHolder.setActivityCallback {
            innerResult = CheckoutCallbackHolder.dismiss()
        }

        // When
        val outerResult = CheckoutCallbackHolder.dismiss()

        // Then
        assertEquals(true, outerResult)
        assertEquals(false, innerResult)
    }

    @Test
    fun `given dismiss re-entered from the activity callback then it finishes the host only once`() {
        // Given
        var activityInvokeCount = 0
        CheckoutCallbackHolder.setActivityCallback {
            activityInvokeCount++
            if (activityInvokeCount == 1) {
                CheckoutCallbackHolder.dismiss()
            }
        }

        // When
        CheckoutCallbackHolder.dismiss()

        // Then
        assertEquals(1, activityInvokeCount)
    }

    @Test
    fun `given notify already finished the host then a late dismiss race does not deliver a second result`() {
        // Given
        val delivered = mutableListOf<MercadoPagoCheckoutResult<*, *>>()
        var activityInvokeCount = 0
        CheckoutCallbackHolder.setCallback<MPPaymentData.CardSave, MPUserCancelledContext.CardSave> {
            delivered += it
        }
        CheckoutCallbackHolder.setActivityCallback { activityInvokeCount++ }
        val error = mockk<MercadoPagoCheckoutResult<MPPaymentData.CardSave, MPUserCancelledContext.CardSave>>()

        // When
        CheckoutCallbackHolder.notify(error)
        CheckoutCallbackHolder.dismiss()

        // Then
        assertEquals(listOf<MercadoPagoCheckoutResult<*, *>>(error), delivered)
        assertEquals(1, activityInvokeCount)
    }

    @Test
    fun `given a call in flight when notify is re-entered from the callback then it finishes the host only once`() {
        // Given
        var activityInvokeCount = 0
        var callbackInvokeCount = 0
        CheckoutCallbackHolder.setActivityCallback { activityInvokeCount++ }
        CheckoutCallbackHolder.setCallback<MPPaymentData.CardSave, MPUserCancelledContext.CardSave> {
            callbackInvokeCount++
            if (callbackInvokeCount == 1) {
                CheckoutCallbackHolder.notify(it)
            }
        }

        // When
        CheckoutCallbackHolder.notify(mockk())

        // Then
        assertEquals(1, activityInvokeCount)
        assertEquals(1, callbackInvokeCount)
    }
}
