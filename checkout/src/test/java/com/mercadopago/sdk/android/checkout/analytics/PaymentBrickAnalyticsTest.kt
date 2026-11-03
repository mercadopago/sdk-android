package com.mercadopago.sdk.android.checkout.analytics

import com.mercadopago.sdk.android.analytics.domain.models.TrackType
import com.mercadopago.sdk.android.checkout.analytics.CheckoutAnalyticsConstants.CHECKOUT_PAYMENT_BRICK_PATH
import com.mercadopago.sdk.android.initializer.analytics.SDK_NATIVE_PATH
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class PaymentBrickAnalyticsTest {
    @Test
    fun `given payment brick initialize then metric is a VIEW on the initialize path without data`() {
        // Given
        val expectedPath = "$SDK_NATIVE_PATH$CHECKOUT_PAYMENT_BRICK_PATH/initialize"

        // When
        val metric = metricPaymentBrickInitialize()

        // Then
        assertEquals(expectedPath, metric.path)
        assertEquals(TrackType.VIEW, metric.type)
        assertNull(metric.data)
    }

    @Test
    fun `given payment brick initialize error then metric is an EVENT on the initialize_error path without data`() {
        // Given
        val expectedPath = "$SDK_NATIVE_PATH$CHECKOUT_PAYMENT_BRICK_PATH/initialize_error"

        // When
        val metric = metricPaymentBrickInitializeError()

        // Then
        assertEquals(expectedPath, metric.path)
        assertEquals(TrackType.EVENT, metric.type)
        assertNull(metric.data)
    }

    @Test
    fun `given payment brick selected then metric is an EVENT on the selected path without data`() {
        // Given
        val expectedPath = "$SDK_NATIVE_PATH$CHECKOUT_PAYMENT_BRICK_PATH/selected"

        // When
        val metric = metricPaymentBrickSelected()

        // Then
        assertEquals(expectedPath, metric.path)
        assertEquals(TrackType.EVENT, metric.type)
        assertNull(metric.data)
    }
}
