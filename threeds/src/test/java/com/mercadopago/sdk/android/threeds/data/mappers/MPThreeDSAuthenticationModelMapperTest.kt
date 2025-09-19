package com.mercadopago.sdk.android.threeds.data.mappers

import com.mercadopago.sdk.android.threeds.data.model.MPThreeDSAuthenticationParams
import org.junit.Test
import org.junit.Assert.*

class MPThreeDSAuthenticationModelMapperTest {

    @Test
    fun `toChallengeModel should map all properties correctly`() {
        // Arrange
        val inputAuthParams = MPThreeDSAuthenticationParams(
            response = "CHALLENGE",
            threeDSServerTransID = "server-trans-123",
            acsReferenceNumber = "acs-ref-456",
            dsTransID = "ds-trans-789",
            acsTransID = "acs-trans-101",
            acsSignedContent = "signed-content-abc"
        )

        // Act
        val actualChallengeModel = inputAuthParams.toChallengeModel()

        // Assert
        assertEquals("server-trans-123", actualChallengeModel.threeDSServerTransID)
        assertEquals("acs-ref-456", actualChallengeModel.acsReferenceNumber)
        assertEquals("ds-trans-789", actualChallengeModel.dsTransID)
        assertEquals("acs-trans-101", actualChallengeModel.acsTransID)
        assertEquals("signed-content-abc", actualChallengeModel.acsSignedContent)
    }

    @Test
    fun `toChallengeModel should handle empty strings`() {
        // Arrange
        val inputAuthParams = MPThreeDSAuthenticationParams(
            response = "",
            threeDSServerTransID = "",
            acsReferenceNumber = "",
            dsTransID = "",
            acsTransID = "",
            acsSignedContent = ""
        )

        // Act
        val actualChallengeModel = inputAuthParams.toChallengeModel()

        // Assert
        assertEquals("", actualChallengeModel.threeDSServerTransID)
        assertEquals("", actualChallengeModel.acsReferenceNumber)
        assertEquals("", actualChallengeModel.dsTransID)
        assertEquals("", actualChallengeModel.acsTransID)
        assertEquals("", actualChallengeModel.acsSignedContent)
    }

    @Test
    fun `toChallengeModel should handle special characters`() {
        // Arrange
        val inputAuthParams = MPThreeDSAuthenticationParams(
            response = "CHALLENGE",
            threeDSServerTransID = "server-trans-!@#$%",
            acsReferenceNumber = "acs-ref-&*()_+",
            dsTransID = "ds-trans-{}|:<>?",
            acsTransID = "acs-trans-[]\\;'\".,/",
            acsSignedContent = "signed-content-~`"
        )

        // Act
        val actualChallengeModel = inputAuthParams.toChallengeModel()

        // Assert
        assertEquals("server-trans-!@#$%", actualChallengeModel.threeDSServerTransID)
        assertEquals("acs-ref-&*()_+", actualChallengeModel.acsReferenceNumber)
        assertEquals("ds-trans-{}|:<>?", actualChallengeModel.dsTransID)
        assertEquals("acs-trans-[]\\;'\".,/", actualChallengeModel.acsTransID)
        assertEquals("signed-content-~`", actualChallengeModel.acsSignedContent)
    }
}
