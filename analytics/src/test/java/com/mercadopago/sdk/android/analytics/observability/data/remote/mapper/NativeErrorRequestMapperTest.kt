package com.mercadopago.sdk.android.analytics.observability.data.remote.mapper

import android.content.Context
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.mercadopago.sdk.android.analytics.observability.domain.models.NativeError
import com.mercadopago.sdk.android.analytics.observability.domain.models.NativeErrorCode
import com.mercadopago.sdk.android.analytics.observability.domain.models.NativeErrorDeliveryPolicy
import com.mercadopago.sdk.android.analytics.observability.domain.models.NativeErrorDiagnostic
import com.mercadopago.sdk.android.analytics.observability.domain.models.NativeErrorOperation
import com.mercadopago.sdk.android.analytics.observability.domain.models.NativeObservabilityConfiguration
import com.mercadopago.sdk.android.analytics.observability.domain.models.PendingNativeError
import com.mercadopago.sdk.android.core.utils.NetworkType
import io.mockk.mockk
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

internal class NativeErrorRequestMapperTest {
    private val context = mockk<Context>(relaxed = true)

    @Test
    fun `maps the fixed android source and derived catalog values`() {
        // Given
        val mapper = NativeErrorRequestMapper(
            context = context,
            configuration = configuration("MLB"),
            networkType = { NetworkType.WIFI },
        )

        // When
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

        // Then
        assertEquals("openplatform_sdk_android", request.source.sdkName)
        assertEquals("android", request.source.hostPlatform)
        assertEquals("native", request.source.sdkTechnology)
        assertEquals("core_methods", request.source.module)
        assertEquals("issuers", request.source.operation)
        assertEquals("request_timeout", request.error.code)
        assertEquals("service", request.error.category)
        assertEquals(true, request.error.critical)
        assertEquals("issuers", request.error.serviceTarget)
        assertEquals("wifi", request.device.connectivity)
    }

    @Test
    fun `omits invalid error values and Gson never serializes null fields`() {
        // Given
        val mapper = NativeErrorRequestMapper(
            context = context,
            configuration = configuration("MLA"),
            networkType = { NetworkType.NONE },
        )

        // When
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

        // Then
        assertNull(request.error.statusCode)
        assertNull(request.error.requestCorrelationId)
        assertNull(request.error.serviceTarget)
        assertEquals("none", request.device.connectivity)
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
            configuration = configuration("MLB"),
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

        val actual = JsonParser().parse(GsonBuilder().create().toJson(request)).asJsonObject
        val expectedJson = checkNotNull(javaClass.getResource("/fixtures/native_error_android_checkout.json"))
            .readText()
        val expected = JsonParser().parse(expectedJson).asJsonObject
        val actualSource = actual.getAsJsonObject("source")
        val actualDevice = actual.getAsJsonObject("device")
        assertTrue(actualSource.has("sdk_version"))
        actualSource.remove("sdk_version")
        expected.getAsJsonObject("source").remove("sdk_version")
        actualDevice.remove("os_version")
        expected.getAsJsonObject("device").remove("os_version")
        assertEquals(expected, actual)
    }

    private companion object {
        fun configuration(siteId: String) = NativeObservabilityConfiguration(
            sdkName = "openplatform_sdk_android",
            sdkVersion = "1.0.0",
            siteId = siteId,
            deliveryPolicy = NativeErrorDeliveryPolicy(),
        )

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

    @Test
    fun `omits an unsafe OS version`() {
        // Given
        val unsafeOsVersion = "invalid value"

        // When
        val safeOsVersion = unsafeOsVersion.toSafeOsVersion()

        // Then
        assertNull(safeOsVersion)
    }
}
