package com.mercadopago.sdk.android.checkout.presentation.factory

import com.mercadopago.android.sdk.checkout.R
import com.mercadopago.sdk.android.checkout.domain.provider.StringProvider
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

internal class CardPaymentScreenStateFactoryTest {
    private val stringProvider = mockk<StringProvider>()
    private val factory = CardPaymentScreenStateFactory(stringProvider)

    @Test
    fun `given factory then getGenericErrorMessage returns string from provider`() {
        every { stringProvider.getString(R.string.card_form_generic_error) } returns "Algo deu errado"

        val result = factory.getGenericErrorMessage()

        assertEquals("Algo deu errado", result)
    }

    @Test
    fun `given factory then getGenericErrorMessage delegates to stringProvider`() {
        every { stringProvider.getString(R.string.card_form_generic_error) } returns ""

        factory.getGenericErrorMessage()

        verify(exactly = 1) { stringProvider.getString(R.string.card_form_generic_error) }
    }

    @Test
    fun `given factory then getStringProvider returns the injected provider`() {
        val result = factory.getStringProvider()

        assertSame(stringProvider, result)
    }
}
