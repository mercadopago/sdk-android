package com.mercadopago.sdk.android.data.local.mapper

import com.mercadopago.sdk.android.domain.model.CountryCode
import kotlin.test.Test
import kotlin.test.assertEquals

internal class SiteIdMapperTest {

    @Test
    fun `when country code is Argentina then return MLA`() {
        val countryCode = CountryCode.ARG
        val siteId = countryCode.toSiteId()
        assertEquals("MLA", siteId)
    }

    @Test
    fun `when country code is Brazil then return MLB`() {
        val countryCode = CountryCode.BRA
        val siteId = countryCode.toSiteId()
        assertEquals("MLB", siteId)
    }

    @Test
    fun `when country code is Chile then return MLC`() {
        val countryCode = CountryCode.CHL
        val siteId = countryCode.toSiteId()
        assertEquals("MLC", siteId)
    }

    @Test
    fun `when country code is Colombia then return MCO`() {
        val countryCode = CountryCode.COL
        val siteId = countryCode.toSiteId()
        assertEquals("MCO", siteId)
    }

    @Test
    fun `when country code is Mexico then return MLM`() {
        val countryCode = CountryCode.MEX
        val siteId = countryCode.toSiteId()
        assertEquals("MLM", siteId)
    }

    @Test
    fun `when country code is Costa Rica then return MCR`() {
        val countryCode = CountryCode.CRI
        val siteId = countryCode.toSiteId()
        assertEquals("MCR", siteId)
    }

    @Test
    fun `when country code is Peru then return MPE`() {
        val countryCode = CountryCode.PER
        val siteId = countryCode.toSiteId()
        assertEquals("MPE", siteId)
    }

    @Test
    fun `when country code is Ecuador then return MEC`() {
        val countryCode = CountryCode.ECU
        val siteId = countryCode.toSiteId()
        assertEquals("MEC", siteId)
    }

    @Test
    fun `when country code is Dominican Republic then return MRD`() {
        val countryCode = CountryCode.DOM
        val siteId = countryCode.toSiteId()
        assertEquals("MRD", siteId)
    }

    @Test
    fun `when country code is Uruguay then return MLU`() {
        val countryCode = CountryCode.URY
        val siteId = countryCode.toSiteId()
        assertEquals("MLU", siteId)
    }

    @Test
    fun `when country code is Venezuela then return MLV`() {
        val countryCode = CountryCode.VEN
        val siteId = countryCode.toSiteId()
        assertEquals("MLV", siteId)
    }

    @Test
    fun `when country code is Panama then return MPA`() {
        val countryCode = CountryCode.PAN
        val siteId = countryCode.toSiteId()
        assertEquals("MPA", siteId)
    }

    @Test
    fun `when country code is Bolivia then return MBO`() {
        val countryCode = CountryCode.BOL
        val siteId = countryCode.toSiteId()
        assertEquals("MBO", siteId)
    }

    @Test
    fun `when country code is Paraguay then return MPY`() {
        val countryCode = CountryCode.PRY
        val siteId = countryCode.toSiteId()
        assertEquals("MPY", siteId)
    }

    @Test
    fun `when country code is Guatemala then return MGT`() {
        val countryCode = CountryCode.GTM
        val siteId = countryCode.toSiteId()
        assertEquals("MGT", siteId)
    }

    @Test
    fun `when country code is Honduras then return MHN`() {
        val countryCode = CountryCode.HND
        val siteId = countryCode.toSiteId()
        assertEquals("MHN", siteId)
    }

    @Test
    fun `when country code is El Salvador then return MSV`() {
        val countryCode = CountryCode.SLV
        val siteId = countryCode.toSiteId()
        assertEquals("MSV", siteId)
    }

    @Test
    fun `when country code is Nicaragua then return MNI`() {
        val countryCode = CountryCode.NIC
        val siteId = countryCode.toSiteId()
        assertEquals("MNI", siteId)
    }

    @Test
    fun `when country code is Cuba then return MCU`() {
        val countryCode = CountryCode.CUB
        val siteId = countryCode.toSiteId()
        assertEquals("MCU", siteId)
    }
}
