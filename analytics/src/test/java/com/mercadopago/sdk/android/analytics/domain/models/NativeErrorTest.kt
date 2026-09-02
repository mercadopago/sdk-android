package com.mercadopago.sdk.android.analytics.domain.models

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

internal class NativeErrorTest {
    @Test
    fun `catalog derives category criticality module and service target`() {
        assertEquals("input_validation", NativeErrorCode.INPUT_VALIDATION_FAILED.category)
        assertFalse(NativeErrorCode.INPUT_VALIDATION_FAILED.critical)
        assertEquals("service", NativeErrorCode.REQUEST_TIMEOUT.category)
        assertTrue(NativeErrorCode.REQUEST_TIMEOUT.critical)
        assertEquals(NativeErrorModule.CORE_METHODS, NativeErrorOperation.ISSUERS.module)
        assertEquals("issuers", NativeErrorOperation.ISSUERS.serviceTarget)
        assertNull(NativeErrorOperation.CARD_TOKENIZATION.serviceTarget)
        assertEquals(NativeErrorModule.CHECKOUT, NativeErrorOperation.ORDER_SUBMISSION.module)
        assertEquals("orders", NativeErrorOperation.ORDER_SUBMISSION.serviceTarget)
    }

    @Test
    fun `unknown delivery mode fails closed to melidata only`() {
        assertEquals(
            NativeErrorDeliveryMode.MELIDATA_ONLY,
            NativeErrorDeliveryMode.from("unexpected")
        )
    }
}
