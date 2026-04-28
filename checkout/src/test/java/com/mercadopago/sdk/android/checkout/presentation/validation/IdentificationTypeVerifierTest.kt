package com.mercadopago.sdk.android.checkout.presentation.validation

import com.mercadopago.sdk.android.checkout.presentation.state.IdentificationTypeState
import com.mercadopago.sdk.android.checkout.presentation.state.ValidationState
import com.mercadopago.sdk.android.coremethods.domain.model.IdentificationType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class IdentificationTypeVerifierTest {
    private val verifier = IdentificationTypeVerifier()

    private val validation = ValidationState(
        errorEmpty = "Campo obrigatório",
        errorIncomplete = "Documento incompleto",
        errorInvalid = "Documento inválido",
    )

    private val selected = IdentificationType(minLength = 7, maxLength = 11)

    private fun state(
        value: String,
        selected: IdentificationType? = this.selected,
    ) = IdentificationTypeState(value = value, selected = selected, validation = validation)

    // region checkEmpty

    @Test
    fun `given empty value then returns errorEmpty`() {
        val result = verifier.verify(state(value = ""))

        assertEquals("Campo obrigatório", result)
    }

    @Test
    fun `given empty value with null selected then still returns errorEmpty`() {
        val result = verifier.verify(state(value = "", selected = null))

        assertEquals("Campo obrigatório", result)
    }

    // endregion

    // region checkIncomplete

    @Test
    fun `given value shorter than minLength then returns errorIncomplete`() {
        val result = verifier.verify(state(value = "12345")) // length 5 < min 7

        assertEquals("Documento incompleto", result)
    }

    @Test
    fun `given value longer than maxLength then returns errorIncomplete`() {
        val result = verifier.verify(state(value = "123456789012")) // length 12 > max 11

        assertEquals("Documento incompleto", result)
    }

    @Test
    fun `given all-zeros value shorter than minLength then incomplete takes priority over invalid`() {
        val result = verifier.verify(state(value = "000000")) // length 6 < min 7, but also all zeros

        assertEquals("Documento incompleto", result)
    }

    @Test
    fun `given null selected then checkIncomplete is skipped`() {
        val result = verifier.verify(state(value = "123", selected = null))

        assertTrue(result.isEmpty())
    }

    // endregion

    // region checkAllZeros

    @Test
    fun `given all-zeros value within valid range then returns errorInvalid`() {
        val result = verifier.verify(state(value = "00000000")) // length 8, in 7..11

        assertEquals("Documento inválido", result)
    }

    @Test
    fun `given all-zeros value at minLength then returns errorInvalid`() {
        val result = verifier.verify(state(value = "0000000")) // length 7 == min

        assertEquals("Documento inválido", result)
    }

    @Test
    fun `given all-zeros value at maxLength then returns errorInvalid`() {
        val result = verifier.verify(state(value = "00000000000")) // length 11 == max

        assertEquals("Documento inválido", result)
    }

    // endregion

    // region valid cases

    @Test
    fun `given value with exactly minLength and no zeros then returns empty`() {
        val result = verifier.verify(state(value = "1234567")) // length 7 == min

        assertTrue(result.isEmpty())
    }

    @Test
    fun `given value with exactly maxLength and no zeros then returns empty`() {
        val result = verifier.verify(state(value = "12345678901")) // length 11 == max

        assertTrue(result.isEmpty())
    }

    @Test
    fun `given valid value within range then returns empty`() {
        val result = verifier.verify(state(value = "12345678")) // length 8, in 7..11

        assertTrue(result.isEmpty())
    }

    // endregion
}
