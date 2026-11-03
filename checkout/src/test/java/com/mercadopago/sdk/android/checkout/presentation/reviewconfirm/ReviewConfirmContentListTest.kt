package com.mercadopago.sdk.android.checkout.presentation.reviewconfirm

import com.mercadopago.sdk.android.checkout.presentation.model.ReviewConfirmHeaderUiModel
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class ReviewConfirmContentListTest {
    @Test
    fun `given seller icon without name then seller section is displayed`() {
        val header = header(sellerName = null, sellerIconUrl = "https://example.com/default-logo.png")

        assertTrue(header.hasSellerSection())
    }

    @Test
    fun `given seller name without icon then seller section is displayed`() {
        val header = header(sellerName = "Test Store", sellerIconUrl = null)

        assertTrue(header.hasSellerSection())
    }

    @Test
    fun `given no seller information then seller section is not displayed`() {
        assertFalse(header(sellerName = null, sellerIconUrl = null).hasSellerSection())
    }

    private fun header(
        sellerName: String?,
        sellerIconUrl: String?,
    ) = ReviewConfirmHeaderUiModel(
        title = "Review your payment",
        sellerName = sellerName,
        sellerIconUrl = sellerIconUrl,
    )
}
