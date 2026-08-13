package com.mercadopago.sdk.android.checkout.core.extensions

import android.content.Context
import com.mercadopago.sdk.android.checkout.core.MercadoPagoCheckout
import com.mercadopago.sdk.android.checkout.core.model.MPCheckoutType
import com.mercadopago.sdk.android.checkout.core.model.MPOrder
import com.mercadopago.sdk.android.checkout.core.model.MPSellerInfo
import com.mercadopago.sdk.android.checkout.core.model.internal.ScreenConfig
import io.mockk.mockk
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
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
    fun `given Payment builder when withReviewAndConfirm with seller then seller is stored`() {
        // Given
        val seller = MPSellerInfo(name = "Adidas Store", logoUrl = "https://logo.png")
        val builder = MercadoPagoCheckout.Builder(context, MPCheckoutType.Payment(order))

        // When
        builder.withReviewAndConfirm(seller = seller)

        // Then
        val config = builder.screenConfigs.filterIsInstance<ScreenConfig.ReviewAndConfirm>().first()
        assertEquals(seller, config.seller)
    }

    @Test
    fun `given Payment builder when withReviewAndConfirm without seller then seller is null`() {
        // Given
        val builder = MercadoPagoCheckout.Builder(context, MPCheckoutType.Payment(order))

        // When
        builder.withReviewAndConfirm()

        // Then
        val config = builder.screenConfigs.filterIsInstance<ScreenConfig.ReviewAndConfirm>().first()
        assertNull(config.seller)
    }

    @Test
    fun `given Payment builder when withReviewAndConfirm called twice then screenConfigs has one entry`() {
        // Given
        val builder = MercadoPagoCheckout.Builder(context, MPCheckoutType.Payment(order))

        // When
        builder.withReviewAndConfirm(seller = MPSellerInfo(name = "First"))
        builder.withReviewAndConfirm(seller = MPSellerInfo(name = "Second"))

        // Then
        assertEquals(1, builder.screenConfigs.filterIsInstance<ScreenConfig.ReviewAndConfirm>().size)
    }

    @Test
    fun `given Payment builder when withReviewAndConfirm called twice then last seller wins`() {
        // Given
        val builder = MercadoPagoCheckout.Builder(context, MPCheckoutType.Payment(order))

        // When
        builder.withReviewAndConfirm(seller = MPSellerInfo(name = "First"))
        builder.withReviewAndConfirm(seller = MPSellerInfo(name = "Second"))

        // Then
        val config = builder.screenConfigs.filterIsInstance<ScreenConfig.ReviewAndConfirm>().first()
        assertEquals("Second", config.seller?.name)
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
    fun `given CardTransaction builder when withReviewAndConfirm with seller then seller is stored`() {
        // Given
        val seller = MPSellerInfo(name = "Nike Store")
        val builder = MercadoPagoCheckout.Builder(context, MPCheckoutType.CardTransaction(order))

        // When
        builder.withReviewAndConfirm(seller = seller)

        // Then
        val config = builder.screenConfigs.filterIsInstance<ScreenConfig.ReviewAndConfirm>().first()
        assertEquals(seller, config.seller)
    }

    @Test
    fun `given CardTransaction builder when withReviewAndConfirm called twice then screenConfigs has one entry`() {
        // Given
        val builder = MercadoPagoCheckout.Builder(context, MPCheckoutType.CardTransaction(order))

        // When
        builder.withReviewAndConfirm(seller = MPSellerInfo(name = "First"))
        builder.withReviewAndConfirm(seller = MPSellerInfo(name = "Second"))

        // Then
        assertEquals(1, builder.screenConfigs.filterIsInstance<ScreenConfig.ReviewAndConfirm>().size)
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
}
