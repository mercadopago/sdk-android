package com.mercadopago.sdk.android.analytics.data.remote.mapper

import android.content.Context
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.mercadopago.sdk.android.analytics.domain.models.NativeError
import com.mercadopago.sdk.android.analytics.domain.models.NativeErrorCode
import com.mercadopago.sdk.android.analytics.domain.models.NativeErrorDiagnostic
import com.mercadopago.sdk.android.analytics.domain.models.NativeErrorOperation
import com.mercadopago.sdk.android.analytics.domain.models.PendingNativeError
import com.mercadopago.sdk.android.core.utils.NetworkType
import io.mockk.mockk
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull

internal class NativeErrorRequestMapperTest {
    private val context = mockk<Context>(relaxed = true)

    @Test
    fun `maps the fixed android source and derived catalog values`() {
        val mapper = NativeErrorRequestMapper(
            context = context,
            siteId = "MLB",
            sdkVersion = "1.2.3",
            osVersion = { "14" },
            networkType = { NetworkType.WIFI },
        )

        val request = mapper.map(
            PendingNativeError(
                eventId = "3f6fd694-4ba8-4f45-ae7c-871c4698aace",
                occurredAt = "2026-08-27T12:00:00.000Z",
                error = NativeError(
                    operation = NativeErrorOperation.ISSUERS,
                    code = NativeErrorCode.REQUEST_TIMEOUT,
                    statusCode = 504,
                    requestCorrelationId = "request:123",
                    diagnostic = NativeErrorDiagnostic.TIMEOUT,
                ),
            )
        )

        assertEquals("openplatform_sdk_android", request.source.sdkName)
        assertEquals("android", request.source.hostPlatform)
        assertEquals("native", request.source.sdkTechnology)
        assertEquals("core_methods", request.source.module)
        assertEquals("issuers", request.source.operation)
        assertEquals("request_timeout", request.error.code)
        assertEquals("service", request.error.category)
        assertEquals(true, request.error.critical)
        assertEquals("issuers", request.error.serviceTarget)
        assertEquals("wifi", request.device?.connectivity)
    }

    @Test
    fun `omits invalid optional values and Gson never serializes null fields`() {
        val mapper = NativeErrorRequestMapper(
            context = context,
            siteId = "MLA",
            sdkVersion = "1.0.0",
            osVersion = { "invalid value" },
            networkType = { NetworkType.NONE },
        )
        val request = mapper.map(
            PendingNativeError(
                eventId = "3f6fd694-4ba8-4f45-ae7c-871c4698aace",
                occurredAt = "2026-08-27T12:00:00.000Z",
                error = NativeError(
                    operation = NativeErrorOperation.CARD_TOKENIZATION,
                    code = NativeErrorCode.OPERATION_FAILED,
                    statusCode = 99,
                    requestCorrelationId = "not safe!",
                ),
            )
        )

        assertNull(request.error.statusCode)
        assertNull(request.error.requestCorrelationId)
        assertNull(request.error.serviceTarget)
        assertNull(request.device?.osVersion)
        val json = GsonBuilder().create().toJson(request)
        assertFalse(json.contains("status_code"))
        assertFalse(json.contains("request_correlation_id"))
        assertFalse(json.contains("service_target"))
        FORBIDDEN_JSON_KEYS.forEach { key ->
            assertFalse(json.contains("\"$key\""), "Forbidden key was serialized: $key")
        }
    }

    @Test
    fun `android checkout request matches the shared contract fixture`() {
        val mapper = NativeErrorRequestMapper(
            context = context,
            siteId = "MLB",
            sdkVersion = "1.2.3",
            osVersion = { "14" },
            networkType = { NetworkType.WIFI },
        )
        val request = mapper.map(
            PendingNativeError(
                eventId = "3f6fd694-4ba8-4f45-ae7c-871c4698aace",
                occurredAt = "2026-08-27T12:00:00.000Z",
                error = NativeError(
                    operation = NativeErrorOperation.ORDER_SUBMISSION,
                    code = NativeErrorCode.UPSTREAM_REJECTED,
                    statusCode = 503,
                ),
            ),
        )

        val actual = JsonParser().parse(GsonBuilder().create().toJson(request))
        val expectedJson = checkNotNull(javaClass.getResource("/fixtures/native_error_android_checkout.json"))
            .readText()
        assertEquals(JsonParser().parse(expectedJson), actual)
    }

    private companion object {
        val FORBIDDEN_JSON_KEYS = setOf(
            "authorization",
            "cookie",
            "public_key",
            "access_token",
            "pan",
            "bin",
            "cvv",
            "payer",
            "email",
            "order_id",
            "payment_id",
            "package",
            "device_id",
            "url",
            "request_body",
            "response_body",
            "raw_error",
            "message",
            "detail",
        )
    }
}
