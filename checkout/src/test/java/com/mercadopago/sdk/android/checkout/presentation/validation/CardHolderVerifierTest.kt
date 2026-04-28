package com.mercadopago.sdk.android.checkout.presentation.validation

import com.mercadopago.sdk.android.checkout.presentation.state.CardHolderState
import com.mercadopago.sdk.android.checkout.presentation.state.ValidationState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class CardHolderVerifierTest {
    private val verifier = CardHolderVerifier()

    private val validation = ValidationState(
        errorEmpty = "Campo obrigatório",
        errorIncomplete = "Nome incompleto",
        errorInvalid = "Formato inválido",
    )

    private fun state(
        value: String,
    ) = CardHolderState(
        value = value,
        validation = validation,
    )

    @Test
    fun `given empty value then returns errorEmpty`() {
        val result = verifier.verify(state(""))

        assertEquals("Campo obrigatório", result)
    }

    @Test
    fun `given value shorter than min characters then returns errorIncomplete`() {
        val result = verifier.verify(state("AB"))

        assertEquals("Nome incompleto", result)
    }

    @Test
    fun `given value with exactly min characters minus one then returns errorIncomplete`() {
        val result = verifier.verify(state("Jo"))

        assertEquals("Nome incompleto", result)
    }

    @Test
    fun `given value with special characters and sufficient length then returns errorInvalid`() {
        val result = verifier.verify(state("John@"))

        assertEquals("Formato inválido", result)
    }

    @Test
    fun `given value too short and with special characters then incomplete takes priority over invalid`() {
        val result = verifier.verify(state("A@"))

        assertEquals("Nome incompleto", result)
    }

    @Test
    fun `given value with exactly min characters and valid format then returns empty`() {
        val result = verifier.verify(state("Ana"))

        assertTrue(result.isEmpty())
    }

    @Test
    fun `given valid value with spaces then returns empty`() {
        val result = verifier.verify(state("John Doe"))

        assertTrue(result.isEmpty())
    }

    @Test
    fun `given valid value with accented characters then returns errorInvalid`() {
        val result = verifier.verify(state("José"))

        assertEquals("Formato inválido", result)
    }

    @Test
    fun `given valid long name then returns empty`() {
        val result = verifier.verify(state("Maria Clara Souza Silva"))

        assertTrue(result.isEmpty())
    }
}
