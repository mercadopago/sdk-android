package com.mercadopago.sdk.android.checkout.domain.extensions

import com.mercadopago.sdk.android.checkout.core.model.MPCheckoutType
import com.mercadopago.sdk.android.checkout.core.model.MPOrder
import com.mercadopago.sdk.android.checkout.core.model.MPSellerInfo
import com.mercadopago.sdk.android.checkout.core.model.internal.CheckoutConfiguration
import com.mercadopago.sdk.android.checkout.core.model.internal.ScreenConfig
import com.mercadopago.sdk.android.checkout.domain.model.params.ProcessOrderParams
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

internal class ReviewConfirmRequestExtensionsTest {
    private val params = ProcessOrderParams(
        orderId = "order-123",
        clientToken = "token-abc",
        paymentMethodId = "visa",
        paymentMethodType = "credit_card",
        token = "card-token-xyz",
        installments = 3,
        amount = "30000",
        bin = "411111",
    )

    @Test
    fun `buildReviewConfirmRequest maps orderId correctly`() {
        // Given
        val config: CheckoutConfiguration? = null

        // When
        val request = params.buildReviewConfirmRequest(config)

        // Then
        assertEquals("order-123", request.orderId)
    }

    @Test
    fun `buildReviewConfirmRequest maps paymentMethodId correctly`() {
        // Given
        val config: CheckoutConfiguration? = null

        // When
        val request = params.buildReviewConfirmRequest(config)

        // Then
        assertEquals("visa", request.paymentMethodId)
    }

    @Test
    fun `buildReviewConfirmRequest maps paymentMethodType correctly`() {
        // Given
        val config: CheckoutConfiguration? = null

        // When
        val request = params.buildReviewConfirmRequest(config)

        // Then
        assertEquals("credit_card", request.paymentMethodType)
    }

    @Test
    fun `buildReviewConfirmRequest maps bin correctly`() {
        // Given
        val config: CheckoutConfiguration? = null

        // When
        val request = params.buildReviewConfirmRequest(config)

        // Then
        assertEquals("411111", request.bin)
    }

    @Test
    fun `buildReviewConfirmRequest maps installments correctly`() {
        // Given
        val config: CheckoutConfiguration? = null

        // When
        val request = params.buildReviewConfirmRequest(config)

        // Then
        assertEquals(3, request.installments)
    }

    @Test
    fun `buildReviewConfirmRequest maps installmentAmount from amount`() {
        // Given
        val config: CheckoutConfiguration? = null

        // When
        val request = params.buildReviewConfirmRequest(config)

        // Then
        assertEquals("30000", request.installmentAmount)
    }

    @Test
    fun `buildReviewConfirmRequest sets emailChangeEnabled to true`() {
        // Given
        val config: CheckoutConfiguration? = null

        // When
        val request = params.buildReviewConfirmRequest(config)

        // Then
        assertEquals(true, request.emailChangeEnabled)
    }

    @Test
    fun `buildReviewConfirmRequest sets issuerId to null`() {
        // Given
        val config: CheckoutConfiguration? = null

        // When
        val request = params.buildReviewConfirmRequest(config)

        // Then
        assertNull(request.issuerId)
    }

    @Test
    fun `buildReviewConfirmRequest sets productId to null`() {
        // Given
        val config: CheckoutConfiguration? = null

        // When
        val request = params.buildReviewConfirmRequest(config)

        // Then
        assertNull(request.productId)
    }

    @Test
    fun `buildReviewConfirmRequest sets lastFourDigits to null`() {
        // Given
        val config: CheckoutConfiguration? = null

        // When
        val request = params.buildReviewConfirmRequest(config)

        // Then
        assertNull(request.lastFourDigits)
    }

    @Test
    fun `buildReviewConfirmRequest extracts sellerInfo from configuration`() {
        // Given
        val config = CheckoutConfiguration(
            checkoutType = MPCheckoutType.Payment(
                order = MPOrder(orderId = "order-123", clientToken = "token-abc"),
            ),
            paymentMethodConfigs = emptyList(),
            screenConfigs = listOf(
                ScreenConfig.ReviewAndConfirm(
                    seller = MPSellerInfo(name = "Loja Teste", logoUrl = "https://logo.url"),
                ),
            ),
        )

        // When
        val request = params.buildReviewConfirmRequest(config)

        // Then
        assertNotNull(request.sellerInfo)
        assertEquals("Loja Teste", request.sellerInfo?.name)
        assertEquals("https://logo.url", request.sellerInfo?.iconUrl)
    }

    @Test
    fun `buildReviewConfirmRequest sets sellerInfo to null when no ReviewAndConfirm in config`() {
        // Given
        val config = CheckoutConfiguration(
            checkoutType = MPCheckoutType.Payment(
                order = MPOrder(orderId = "order-123", clientToken = "token-abc"),
            ),
            paymentMethodConfigs = emptyList(),
            screenConfigs = emptyList(),
        )

        // When
        val request = params.buildReviewConfirmRequest(config)

        // Then
        assertNull(request.sellerInfo)
    }

    @Test
    fun `buildReviewConfirmRequest sets sellerInfo to null when config is null`() {
        // Given
        val config: CheckoutConfiguration? = null

        // When
        val request = params.buildReviewConfirmRequest(config)

        // Then
        assertNull(request.sellerInfo)
    }

    @Test
    fun `buildReviewConfirmRequest sets sellerInfo to null when seller is null in config`() {
        // Given
        val config = CheckoutConfiguration(
            checkoutType = MPCheckoutType.Payment(
                order = MPOrder(orderId = "order-123", clientToken = "token-abc"),
            ),
            paymentMethodConfigs = emptyList(),
            screenConfigs = listOf(
                ScreenConfig.ReviewAndConfirm(seller = null),
            ),
        )

        // When
        val request = params.buildReviewConfirmRequest(config)

        // Then
        assertNull(request.sellerInfo)
    }

    @Test
    fun `buildReviewConfirmRequest handles seller with only name`() {
        // Given
        val config = CheckoutConfiguration(
            checkoutType = MPCheckoutType.Payment(
                order = MPOrder(orderId = "order-123", clientToken = "token-abc"),
            ),
            paymentMethodConfigs = emptyList(),
            screenConfigs = listOf(
                ScreenConfig.ReviewAndConfirm(
                    seller = MPSellerInfo(name = "Only Name", logoUrl = null),
                ),
            ),
        )

        // When
        val request = params.buildReviewConfirmRequest(config)

        // Then
        assertNotNull(request.sellerInfo)
        assertEquals("Only Name", request.sellerInfo?.name)
        assertNull(request.sellerInfo?.iconUrl)
    }
}
