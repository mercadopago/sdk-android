package com.mercadopago.sdk.android.analytics.domain.classifier

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.mercadopago.sdk.android.analytics.domain.models.NativeErrorCode
import com.mercadopago.sdk.android.analytics.domain.models.NativeErrorDiagnostic
import com.mercadopago.sdk.android.analytics.domain.models.NativeErrorOperation
import org.junit.Test
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import kotlin.test.assertEquals

internal class NativeErrorClassifierTest {
    private val classifier = NativeErrorClassifier()

    @Test
    fun `canonical vectors have the approved hash and outputs`() {
        val bytes = checkNotNull(javaClass.getResourceAsStream(FIXTURE)).readBytes()
        assertEquals(APPROVED_SHA256, bytes.sha256())
        val vectors = JsonParser().parse(String(bytes, StandardCharsets.UTF_8))
            .asJsonObject.getAsJsonArray("vectors")

        vectors.forEach { element ->
            val vector = element.asJsonObject
            val inputJson = vector.getAsJsonObject("input")
            val expected = vector.getAsJsonObject("expected")
            val operation = NativeErrorOperation.values().single { it.value == vector.string("operation") }
            val input = NativeErrorInput.create(
                type = NativeErrorType.values().single { it.value == inputJson.string("type") },
                code = inputJson.stringOrNull("code")?.let { value ->
                    NativeErrorEvidenceCode.values().single { it.value == value }
                },
                httpStatus = inputJson.intOrNull("http_status"),
                responseState = inputJson.stringOrNull("response_state")?.let { value ->
                    NativeErrorResponseState.values().single { it.value == value }
                },
                requestCorrelationId = inputJson.stringOrNull("request_correlation_id"),
            )
            val actual = classifier.classify(operation, input)

            assertEquals(expected.string("code"), actual.code.value, vector.string("id"))
            assertEquals(expected.string("category"), actual.code.category, vector.string("id"))
            assertEquals(expected.get("critical").asBoolean, actual.code.critical, vector.string("id"))
            assertEquals(expected.intOrNull("status_code"), actual.statusCode, vector.string("id"))
            assertEquals(expected.stringOrNull("request_correlation_id"), actual.requestCorrelationId)
            assertEquals(expected.stringOrNull("service_target"), actual.operation.serviceTarget)
            assertEquals(expected.stringOrNull("diagnostic_code"), actual.diagnostic?.value)
        }
    }

    @Test
    fun `neutral input exposes only closed values and sanitizes optionals`() {
        val input = NativeErrorInput.create(
            type = NativeErrorType.UNKNOWN,
            code = NativeErrorEvidenceCode.EXCEPTION,
            httpStatus = 700,
            requestCorrelationId = "unsafe correlation",
        )

        assertEquals(null, input.httpStatus)
        assertEquals(null, input.requestCorrelationId)
        assertEquals(
            NativeErrorCode.OPERATION_FAILED,
            classifier.classify(NativeErrorOperation.CARD_FORM_SUBMISSION, input).code,
        )
        assertEquals(
            setOf("type", "code", "httpStatus", "responseState", "requestCorrelationId"),
            NativeErrorInput::class.java.declaredFields
                .filterNot { it.isSynthetic || java.lang.reflect.Modifier.isStatic(it.modifiers) }
                .map { it.name }
                .toSet(),
        )
        assertEquals("cancelled", NativeErrorDiagnostic.CANCELLED.value)
    }

    private fun JsonObject.string(name: String) = get(name).asString
    private fun JsonObject.stringOrNull(name: String) = get(name)?.takeUnless { it.isJsonNull }?.asString
    private fun JsonObject.intOrNull(name: String) = get(name)?.takeUnless { it.isJsonNull }?.asInt
    private fun ByteArray.sha256() = MessageDigest.getInstance("SHA-256").digest(this)
        .joinToString("") { "%02x".format(it.toInt() and 0xff) }

    private companion object {
        const val FIXTURE = "/fixtures/classification_vectors.json"
        const val APPROVED_SHA256 = "9916d9e605a1458bfdcdc7fcd546832754a4f07d8ae7414111b396ad22dec51d"
    }
}
