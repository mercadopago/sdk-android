package com.mercadopago.sdk.android.checkout.domain.mapper

import com.mercadopago.sdk.android.domain.model.CountryCode
import java.util.Locale
import kotlin.test.Test
import kotlin.test.assertEquals

internal class CountryCodeToLocaleMapperTest {
    @Test
    fun `given BRA then returns Portuguese Brazil locale`() {
        val result = CountryCodeToLocaleMapper.map(CountryCode.BRA)

        assertEquals("pt", result.language)
        assertEquals("BR", result.country)
    }

    @Test
    fun `given ARG then returns Spanish Argentina locale`() {
        val result = CountryCodeToLocaleMapper.map(CountryCode.ARG)

        assertEquals("es", result.language)
        assertEquals("AR", result.country)
    }

    @Test
    fun `given MEX then returns Spanish Mexico locale`() {
        val result = CountryCodeToLocaleMapper.map(CountryCode.MEX)

        assertEquals("es", result.language)
        assertEquals("MX", result.country)
    }

    @Test
    fun `given URY then returns Spanish Uruguay locale`() {
        val result = CountryCodeToLocaleMapper.map(CountryCode.URY)

        assertEquals("es", result.language)
        assertEquals("UY", result.country)
    }

    @Test
    fun `given COL then returns Spanish Colombia locale`() {
        val result = CountryCodeToLocaleMapper.map(CountryCode.COL)

        assertEquals("es", result.language)
        assertEquals("CO", result.country)
    }

    @Test
    fun `given PER then returns Spanish Peru locale`() {
        val result = CountryCodeToLocaleMapper.map(CountryCode.PER)

        assertEquals("es", result.language)
        assertEquals("PE", result.country)
    }

    @Test
    fun `given CHL then returns Spanish Chile locale`() {
        val result = CountryCodeToLocaleMapper.map(CountryCode.CHL)

        assertEquals("es", result.language)
        assertEquals("CL", result.country)
    }

    @Test
    fun `given null then returns default locale`() {
        val result = CountryCodeToLocaleMapper.map(null)

        assertEquals(Locale.getDefault(), result)
    }

    @Test
    fun `given unmapped country code then returns default locale`() {
        val result = CountryCodeToLocaleMapper.map(CountryCode.CRI)

        assertEquals(Locale.getDefault(), result)
    }
}
