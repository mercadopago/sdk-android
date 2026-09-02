package com.mercadopago.sdk.android.coremethods.analytics

import com.mercadopago.sdk.android.analytics.domain.models.TrackType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

internal class GenerateCardTokenAnalyticsTest {
    @Test
    fun `success metric has tokenization path and contains only tokenization properties`() {
        val metric = metricGenerateCardTokenCallSuccess(
            identityType = "CPF",
            isSavedCard = true,
            typeWallet = "coremethods",
        )

        assertEquals("/checkout_api_native/core_methods/tokenization", metric.path)
        assertEquals(TrackType.EVENT, metric.type)
        val data = assertIs<GenerateCardAnalyticsData>(metric.data)
        assertEquals("CPF", data.identityType)
        assertEquals(true, data.isSavedCard)
        assertEquals("coremethods", data.typeWallet)
    }

    @Test
    fun `success metric with null identity type still passes other props`() {
        val metric = metricGenerateCardTokenCallSuccess(identityType = null)
        val data = assertIs<GenerateCardAnalyticsData>(metric.data)
        assertNull(data.identityType)
        assertEquals(false, data.isSavedCard)
        assertEquals("coremethods", data.typeWallet)
    }

    @Test
    fun `error metric uses error path with error_type, identity_document_type and type_wallet`() {
        val metric = metricGenerateCardTokenCallError(
            error = "INVALID_DATA",
            observabilityEventId = "event-id",
            identityType = "CPF",
            typeWallet = "coremethods",
        )

        assertEquals("/checkout_api_native/core_methods/tokenization/error", metric.path)
        assertEquals(TrackType.EVENT, metric.type)
        val data = assertIs<GenerateCardTokenErrorData>(metric.data)
        assertEquals("INVALID_DATA", data.errorType)
        assertEquals("event-id", data.observabilityEventId)
        assertEquals("CPF", data.identityType)
        assertEquals("coremethods", data.typeWallet)
    }

    @Test
    fun `error metric defaults type_wallet to coremethods when omitted`() {
        val metric = metricGenerateCardTokenCallError(
            error = "NETWORK_ERROR",
            observabilityEventId = "event-id",
        )
        val data = assertIs<GenerateCardTokenErrorData>(metric.data)
        assertNull(data.identityType)
        assertEquals("coremethods", data.typeWallet)
    }
}
