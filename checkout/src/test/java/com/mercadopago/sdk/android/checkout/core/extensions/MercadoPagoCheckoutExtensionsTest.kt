package com.mercadopago.sdk.android.checkout.core.extensions

import android.content.Context
import com.mercadopago.sdk.android.checkout.core.MercadoPagoCheckout
import com.mercadopago.sdk.android.checkout.core.model.MPCheckoutType
import com.mercadopago.sdk.android.checkout.core.model.MPOrder
import com.mercadopago.sdk.android.checkout.core.model.MPPaymentMethodConfig
import com.mercadopago.sdk.android.checkout.core.model.internal.ScreenConfig
import io.mockk.mockk
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame
import kotlin.test.assertTrue

internal class MercadoPagoCheckoutExtensionsTest {
    private val context = mockk<Context>(relaxed = true)
    private val order = MPOrder(orderId = "ORD_TEST", clientToken = "token-test")

    @Test
    fun `given Payment builder when withReviewAndConfirm then returns same builder`() {
        // Given
        val builder = MercadoPagoCheckout.Builder(context, MPCheckoutType.Payment(order))

        // When
        val result = builder.withReviewAndConfirm()

        // Then
        assertSame(builder, result)
    }

    @Test
    fun `given Payment builder when withReviewAndConfirm then screenConfigs contains ReviewAndConfirm`() {
        // Given
        val builder = MercadoPagoCheckout.Builder(context, MPCheckoutType.Payment(order))

        // When
        builder.withReviewAndConfirm()

        // Then
        assertTrue(builder.screenConfigs.any { it is ScreenConfig.ReviewAndConfirm })
    }

    @Test
    fun `given Payment builder when withReviewAndConfirm called twice then screenConfigs has one entry`() {
        // Given
        val builder = MercadoPagoCheckout.Builder(context, MPCheckoutType.Payment(order))

        // When
        builder.withReviewAndConfirm()
        builder.withReviewAndConfirm()

        // Then
        assertEquals(1, builder.screenConfigs.filterIsInstance<ScreenConfig.ReviewAndConfirm>().size)
    }

    @Test
    fun `given CardTransaction builder when withReviewAndConfirm then returns same builder`() {
        // Given
        val builder = MercadoPagoCheckout.Builder(context, MPCheckoutType.CardTransaction(order))

        // When
        val result = builder.withReviewAndConfirm()

        // Then
        assertSame(builder, result)
    }

    @Test
    fun `given CardTransaction builder when withReviewAndConfirm then screenConfigs contains ReviewAndConfirm`() {
        // Given
        val builder = MercadoPagoCheckout.Builder(context, MPCheckoutType.CardTransaction(order))

        // When
        builder.withReviewAndConfirm()

        // Then
        assertTrue(builder.screenConfigs.any { it is ScreenConfig.ReviewAndConfirm })
    }

    @Test
    fun `given CardTransaction builder when withReviewAndConfirm called twice then screenConfigs has one entry`() {
        // Given
        val builder = MercadoPagoCheckout.Builder(context, MPCheckoutType.CardTransaction(order))

        // When
        builder.withReviewAndConfirm()
        builder.withReviewAndConfirm()

        // Then
        assertEquals(1, builder.screenConfigs.filterIsInstance<ScreenConfig.ReviewAndConfirm>().size)
    }

    @Test
    fun `given Payment builder when withReviewAndConfirm with callback then onEmailChangeRequested is stored`() {
        // Given
        val builder = MercadoPagoCheckout.Builder(context, MPCheckoutType.Payment(order))
        var called = false

        // When
        builder.withReviewAndConfirm(onEmailChangeRequested = { called = true })

        // Then
        val config = builder.screenConfigs.filterIsInstance<ScreenConfig.ReviewAndConfirm>().first()
        config.onEmailChangeRequested?.invoke()
        assertTrue(called)
    }

    @Test
    fun `given Payment builder when withReviewAndConfirm without callback then onEmailChangeRequested is null`() {
        // Given
        val builder = MercadoPagoCheckout.Builder(context, MPCheckoutType.Payment(order))

        // When
        builder.withReviewAndConfirm()

        // Then
        val config = builder.screenConfigs.filterIsInstance<ScreenConfig.ReviewAndConfirm>().first()
        assertEquals(null, config.onEmailChangeRequested)
    }

    @Test
    fun `given fresh Payment builder then screenConfigs is empty`() {
        // Given / When
        val builder = MercadoPagoCheckout.Builder(context, MPCheckoutType.Payment(order))

        // Then
        assertTrue(builder.screenConfigs.isEmpty())
    }

    @Test
    fun `given fresh CardTransaction builder then screenConfigs is empty`() {
        // Given / When
        val builder = MercadoPagoCheckout.Builder(context, MPCheckoutType.CardTransaction(order))

        // Then
        assertTrue(builder.screenConfigs.isEmpty())
    }

    @Test
    fun `given Payment builder when withStatusScreen then returns same builder`() {
        // Given
        val builder = MercadoPagoCheckout.Builder(context, MPCheckoutType.Payment(order))

        // When
        val result = builder.withStatusScreen {}

        // Then
        assertSame(builder, result)
    }

    @Test
    fun `given Payment builder when withStatusScreen then screenConfigs contains StatusScreen`() {
        // Given
        val builder = MercadoPagoCheckout.Builder(context, MPCheckoutType.Payment(order))

        // When
        builder.withStatusScreen {}

        // Then
        assertTrue(builder.screenConfigs.any { it is ScreenConfig.StatusScreen })
    }

    @Test
    fun `given Payment builder when withStatusScreen called twice then screenConfigs has one entry`() {
        // Given
        val builder = MercadoPagoCheckout.Builder(context, MPCheckoutType.Payment(order))

        // When
        builder.withStatusScreen {}
        builder.withStatusScreen {}

        // Then
        assertEquals(1, builder.screenConfigs.filterIsInstance<ScreenConfig.StatusScreen>().size)
    }

    @Test
    fun `given Payment builder when withStatusScreen with callback then onExit is stored`() {
        // Given
        val builder = MercadoPagoCheckout.Builder(context, MPCheckoutType.Payment(order))
        var called = false

        // When
        builder.withStatusScreen(onExit = { called = true })

        // Then
        val config = builder.screenConfigs.filterIsInstance<ScreenConfig.StatusScreen>().first()
        config.onExit?.invoke()
        assertTrue(called)
    }

    @Test
    fun `given CardTransaction builder when withStatusScreen with callback then onExit is stored`() {
        // Given
        val builder = MercadoPagoCheckout.Builder(context, MPCheckoutType.CardTransaction(order))
        var called = false

        // When
        builder.withStatusScreen(onExit = { called = true })

        // Then
        val config = builder.screenConfigs.filterIsInstance<ScreenConfig.StatusScreen>().first()
        config.onExit?.invoke()
        assertTrue(called)
    }

    @Test
    fun `given CardTransaction builder when withStatusScreen then returns same builder`() {
        // Given
        val builder = MercadoPagoCheckout.Builder(context, MPCheckoutType.CardTransaction(order))

        // When
        val result = builder.withStatusScreen {}

        // Then
        assertSame(builder, result)
    }

    @Test
    fun `given CardTransaction builder when withStatusScreen then screenConfigs contains StatusScreen`() {
        // Given
        val builder = MercadoPagoCheckout.Builder(context, MPCheckoutType.CardTransaction(order))

        // When
        builder.withStatusScreen {}

        // Then
        assertTrue(builder.screenConfigs.any { it is ScreenConfig.StatusScreen })
    }

    @Test
    fun `given CardTransaction builder when withStatusScreen called twice then screenConfigs has one entry`() {
        // Given
        val builder = MercadoPagoCheckout.Builder(context, MPCheckoutType.CardTransaction(order))

        // When
        builder.withStatusScreen {}
        builder.withStatusScreen {}

        // Then
        assertEquals(1, builder.screenConfigs.filterIsInstance<ScreenConfig.StatusScreen>().size)
    }

    @Test
    fun `given Payment builder when withReviewAndConfirm and withStatusScreen then both configs coexist`() {
        // Given
        val builder = MercadoPagoCheckout.Builder(context, MPCheckoutType.Payment(order))

        // When
        builder.withReviewAndConfirm()
        builder.withStatusScreen {}

        // Then
        assertTrue(builder.screenConfigs.any { it is ScreenConfig.ReviewAndConfirm })
        assertTrue(builder.screenConfigs.any { it is ScreenConfig.StatusScreen })
        assertEquals(2, builder.screenConfigs.size)
    }

    @Test
    fun `given Payment builder when withStatusScreen only then withReviewAndConfirm config is absent`() {
        // Given
        val builder = MercadoPagoCheckout.Builder(context, MPCheckoutType.Payment(order))

        // When
        builder.withStatusScreen {}

        // Then
        assertTrue(builder.screenConfigs.none { it is ScreenConfig.ReviewAndConfirm })
    }

    @Test
    fun `given CardSave builder when setPaymentMethodConfiguration then paymentMethodConfigs are stored`() {
        // Given
        val builder = MercadoPagoCheckout.Builder(context, MPCheckoutType.CardSave)
        val configs = listOf(MPPaymentMethodConfig.Card())

        // When
        val result = builder.setPaymentMethodConfiguration(configs)

        // Then
        assertSame(builder, result)
        assertEquals(configs, builder.paymentMethodConfigs)
    }
}
