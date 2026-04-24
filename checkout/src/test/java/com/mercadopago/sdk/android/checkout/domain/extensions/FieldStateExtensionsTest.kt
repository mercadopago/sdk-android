package com.mercadopago.sdk.android.checkout.domain.extensions

import com.mercadopago.sdk.android.checkout.presentation.state.CardNumberState
import com.mercadopago.sdk.android.checkout.presentation.state.IdentificationTypeState
import com.mercadopago.sdk.android.coremethods.domain.model.IdentificationType
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class FieldStateExtensionsTest {
    @Test
    fun `given length equals maxLength then CardNumberState isComplete returns true`() {
        val state = CardNumberState(length = 16, maxLength = 16)

        assertTrue(state.isComplete())
    }

    @Test
    fun `given length less than maxLength then CardNumberState isComplete returns false`() {
        val state = CardNumberState(length = 10, maxLength = 16)

        assertFalse(state.isComplete())
    }

    @Test
    fun `given length greater than maxLength then CardNumberState isComplete returns false`() {
        val state = CardNumberState(length = 17, maxLength = 16)

        assertFalse(state.isComplete())
    }

    @Test
    fun `given null selected then IdentificationTypeState isComplete returns false`() {
        val state = IdentificationTypeState(selected = null)

        assertFalse(state.isComplete(11))
    }

    @Test
    fun `given length equals selected maxLength then IdentificationTypeState isComplete returns true`() {
        val idType = IdentificationType(minLength = 9, maxLength = 11)
        val state = IdentificationTypeState(selected = idType)

        assertTrue(state.isComplete(11))
    }

    @Test
    fun `given length equals selected minLength then IdentificationTypeState isComplete returns true`() {
        val idType = IdentificationType(minLength = 9, maxLength = 11)
        val state = IdentificationTypeState(selected = idType)

        assertTrue(state.isComplete(9))
    }

    @Test
    fun `given length matches neither min nor max then IdentificationTypeState isComplete returns false`() {
        val idType = IdentificationType(minLength = 9, maxLength = 11)
        val state = IdentificationTypeState(selected = idType)

        assertFalse(state.isComplete(10))
    }
}
