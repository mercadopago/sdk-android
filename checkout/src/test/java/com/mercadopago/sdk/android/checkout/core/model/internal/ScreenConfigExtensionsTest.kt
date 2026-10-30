package com.mercadopago.sdk.android.checkout.core.model.internal

import com.mercadopago.sdk.android.checkout.domain.model.Screen
import kotlin.test.Test
import kotlin.test.assertEquals

internal class ScreenConfigExtensionsTest {
    @Test
    fun `given ReviewAndConfirm when toScreen then returns REVIEW_AND_CONFIRM`() {
        // Given
        val config = ScreenConfig.ReviewAndConfirm()

        // When
        val result = config.toScreen()

        // Then
        assertEquals(Screen.REVIEW_AND_CONFIRM, result)
    }

    @Test
    fun `given StatusScreen when toScreen then returns STATUS_SCREEN`() {
        // Given
        val config: ScreenConfig = ScreenConfig.StatusScreen()

        // When
        val result = config.toScreen()

        // Then
        assertEquals(Screen.STATUS_SCREEN, result)
    }
}
