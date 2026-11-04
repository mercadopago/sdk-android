package com.mercadopago.sdk.android.coremethods.ui.components.textfield.identificationtextfield

import androidx.compose.ui.text.AnnotatedString
import com.mercadopago.sdk.android.coremethods.domain.model.IdentificationType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

internal class IdentificationExtensionsTest {
    @Test
    fun `given identification type with server mask then getVisualTransformation applies that mask`() {
        // Given
        val identificationType = IdentificationType(id = "DNI", type = "number", mask = "##.###.###")

        // When
        val result = identificationType.getVisualTransformation()
            .filter(AnnotatedString("28456789"))
            .text
            .text

        // Then
        assertEquals("28.456.789", result)
    }

    @Test
    fun `given identification type without mask then getVisualTransformation keeps the input unchanged`() {
        // Given
        val identificationType = IdentificationType(id = "DNI", type = "number", mask = null)

        // When
        val result = identificationType.getVisualTransformation()
            .filter(AnnotatedString("28456789"))
            .text
            .text

        // Then
        assertEquals("28456789", result)
    }

    @Test
    fun `given identification type with empty mask then getVisualTransformation keeps the input unchanged`() {
        // Given
        val identificationType = IdentificationType(id = "CPF", type = "number", mask = "")

        // When
        val result = identificationType.getVisualTransformation()
            .filter(AnnotatedString("12345678901"))
            .text
            .text

        // Then
        assertEquals("12345678901", result)
    }

    @Test
    fun `given null identification type then getVisualTransformation keeps the input unchanged`() {
        // Given
        val identificationType: IdentificationType? = null

        // When
        val result = identificationType.getVisualTransformation()
            .filter(AnnotatedString("12345678901"))
            .text
            .text

        // Then
        assertEquals("12345678901", result)
    }

    @Test
    fun `given lowercase string type then isNumeric returns false`() {
        // Given
        val identificationType = IdentificationType(id = "CNPJ", type = "string")

        // When
        val result = identificationType.isNumeric()

        // Then
        assertFalse(result)
    }

    @Test
    fun `given capitalized String type then isNumeric returns false`() {
        // Given
        val identificationType = IdentificationType(id = "CNPJ", type = "String")

        // When
        val result = identificationType.isNumeric()

        // Then
        assertFalse(result)
    }

    @Test
    fun `given capitalized Number type then isNumeric returns true`() {
        // Given
        val identificationType = IdentificationType(id = "CPF", type = "Number")

        // When
        val result = identificationType.isNumeric()

        // Then
        assertTrue(result)
    }

    @Test
    fun `given unknown type then isNumeric returns false`() {
        // Given
        val identificationType = IdentificationType(id = "RG", type = "rg")

        // When
        val result = identificationType.isNumeric()

        // Then
        assertFalse(result)
    }

    @Test
    fun `given null identification type then isNumeric returns false`() {
        // Given
        val identificationType: IdentificationType? = null

        // When
        val result = identificationType.isNumeric()

        // Then
        assertFalse(result)
    }
}
