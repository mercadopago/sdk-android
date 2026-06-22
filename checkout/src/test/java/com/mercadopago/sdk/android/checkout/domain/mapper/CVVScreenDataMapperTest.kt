package com.mercadopago.sdk.android.checkout.domain.mapper

import com.mercadopago.sdk.android.checkout.domain.model.SecurityCodeFieldOutput
import com.mercadopago.sdk.android.checkout.domain.model.SecurityCodeScreenOutput
import kotlin.test.Test
import kotlin.test.assertEquals

internal class CVVScreenDataMapperTest {
    private fun buildScreen(
        headerTitle: String = "Ingresá el código de seguridad",
        label: String = "Código de seguridad",
        placeholder: String = "Ej.: 123",
        helper: String = "Está en el reverso de tu tarjeta.",
        continueButtonLabel: String = "Continuar",
    ) = SecurityCodeScreenOutput(
        headerTitle = headerTitle,
        field = SecurityCodeFieldOutput(label = label, placeholder = placeholder, helper = helper),
        continueButtonLabel = continueButtonLabel,
    )

    @Test
    fun `given SecurityCodeScreenOutput then headerTitle is mapped`() {
        val data = buildScreen(headerTitle = "Enter CVV").toCVVScreenData(expectedLength = 3)

        assertEquals("Enter CVV", data.headerTitle)
    }

    @Test
    fun `given SecurityCodeScreenOutput then continueButtonLabel is mapped`() {
        val data = buildScreen(continueButtonLabel = "Continue").toCVVScreenData(expectedLength = 3)

        assertEquals("Continue", data.continueButtonLabel)
    }

    @Test
    fun `given SecurityCodeScreenOutput then field label is mapped`() {
        val data = buildScreen(label = "Security code").toCVVScreenData(expectedLength = 3)

        assertEquals("Security code", data.field.label)
    }

    @Test
    fun `given SecurityCodeScreenOutput then field placeholder is mapped`() {
        val data = buildScreen(placeholder = "Ej.: 1234").toCVVScreenData(expectedLength = 3)

        assertEquals("Ej.: 1234", data.field.placeholder)
    }

    @Test
    fun `given SecurityCodeScreenOutput then field helper is mapped`() {
        val data = buildScreen(helper = "4 digits on front").toCVVScreenData(expectedLength = 3)

        assertEquals("4 digits on front", data.field.helper)
    }

    @Test
    fun `given SecurityCodeFieldOutput then toCVVFieldConfig maps all fields`() {
        val field = SecurityCodeFieldOutput(
            label = "CVV",
            placeholder = "Ej.: 123",
            helper = "3 digits",
        )

        val config = field.toCVVFieldConfig()

        assertEquals("CVV", config.label)
        assertEquals("Ej.: 123", config.placeholder)
        assertEquals("3 digits", config.helper)
    }

    @Test
    fun `given full screen then CVVScreenData is a value type`() {
        val data1 = buildScreen().toCVVScreenData(expectedLength = 3)
        val data2 = buildScreen().toCVVScreenData(expectedLength = 3)

        assertEquals(data1, data2)
        assertEquals(data1.hashCode(), data2.hashCode())
    }
}
