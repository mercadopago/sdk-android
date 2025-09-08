package com.mercadopago.sdk.android.threeds.data.mappers

import com.mercadopago.sdk.android.threeds.data.remote.mappers.toRequest
import org.junit.Test
import kotlin.test.assertEquals

internal class ThreeDSAuthenticationParamsMapperTest {

    @Test
    fun `when ThreeDSAuthenticationParams is mapped to request Then should map all fields correctly`() {
        // Given
        val params = ThreeDSAuthenticationParams(
            token = "test_token",
            sdkAppId = "test_sdk_app_id",
            sdkEncData = "test_enc_data",
            sdkEphemPubKey = "test_ephemeral_pub_key",
            sdkMaxTimeout = "10",
            sdkReferenceNumber = "test_reference_number",
            sdkTransId = "test_transaction_id"
        )

        // When
        val request = params.toRequest()

        // Then
        assertEquals(params.token, request.token)
        assertEquals(params.sdkAppId, request.sdkAppId)
        assertEquals(params.sdkEncData, request.sdkEncData)
        assertEquals(params.sdkEphemPubKey, request.sdkEphemPubKey)
        assertEquals(params.sdkMaxTimeout, request.sdkMaxTimeout)
        assertEquals(params.sdkReferenceNumber, request.sdkReferenceNumber)
        assertEquals(params.sdkTransId, request.sdkTransId)
    }

    @Test
    fun `when ThreeDSAuthenticationParams has empty values Then should map empty values correctly`() {
        // Given
        val params = ThreeDSAuthenticationParams(
            token = "",
            sdkAppId = "",
            sdkEncData = "",
            sdkEphemPubKey = "",
            sdkMaxTimeout = "",
            sdkReferenceNumber = "",
            sdkTransId = ""
        )

        // When
        val request = params.toRequest()

        // Then
        assertEquals("", request.token)
        assertEquals("", request.sdkAppId)
        assertEquals("", request.sdkEncData)
        assertEquals("", request.sdkEphemPubKey)
        assertEquals("", request.sdkMaxTimeout)
        assertEquals("", request.sdkReferenceNumber)
        assertEquals("", request.sdkTransId)
    }
}
