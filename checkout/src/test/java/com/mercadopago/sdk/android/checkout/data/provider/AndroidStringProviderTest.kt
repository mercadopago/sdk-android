package com.mercadopago.sdk.android.checkout.data.provider

import android.content.Context
import android.content.res.Configuration
import com.mercadopago.sdk.android.domain.model.CountryCode
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.Locale
import kotlin.test.assertEquals

internal class AndroidStringProviderTest {
    private val mockContext = mockk<Context>(relaxed = true)
    private val localizedContext = mockk<Context>(relaxed = true)

    @Before
    fun setUp() {
        mockkConstructor(Configuration::class)
        every { anyConstructed<Configuration>().setLocale(any()) } just Runs
        every { mockContext.createConfigurationContext(any()) } returns localizedContext
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `when getString called then returns string from localizedContext`() {
        val resId = 1
        val expected = "some string"
        every { localizedContext.getString(resId) } returns expected
        val provider = AndroidStringProvider(mockContext, CountryCode.BRA)

        val result = provider.getString(resId)

        assertEquals(expected, result)
    }

    @Test
    fun `when getString called then createConfigurationContext is invoked`() {
        val provider = AndroidStringProvider(mockContext, CountryCode.ARG)

        provider.getString(1)

        verify { mockContext.createConfigurationContext(any()) }
    }

    @Test
    fun `when countryCode is BRA then setLocale is called with pt-BR`() {
        val provider = AndroidStringProvider(mockContext, CountryCode.BRA)

        provider.getString(1)

        verify { anyConstructed<Configuration>().setLocale(Locale("pt", "BR")) }
    }

    @Test
    fun `when countryCode is ARG then setLocale is called with es-AR`() {
        val provider = AndroidStringProvider(mockContext, CountryCode.ARG)

        provider.getString(1)

        verify { anyConstructed<Configuration>().setLocale(Locale("es", "AR")) }
    }

    @Test
    fun `when countryCode is null then setLocale is called with default locale`() {
        val provider = AndroidStringProvider(mockContext, null)

        provider.getString(1)

        verify { anyConstructed<Configuration>().setLocale(Locale.getDefault()) }
    }

    @Test
    fun `when localizedContext is built then it is reused on subsequent getString calls`() {
        val provider = AndroidStringProvider(mockContext, CountryCode.MEX)

        provider.getString(1)
        provider.getString(2)

        verify(exactly = 1) { mockContext.createConfigurationContext(any()) }
    }
}
