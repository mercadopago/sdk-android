package com.mercadopago.sdk.android.mpextended.domain.mapper

import com.mercadopago.sdk.android.domain.model.CountryCode

private val COUNTRY_CODE_TO_SITE_ID = mapOf(
    CountryCode.ARG to "MLA",
    CountryCode.BRA to "MLB",
    CountryCode.MEX to "MLM",
    CountryCode.COL to "MCO",
    CountryCode.CHL to "MLC",
    CountryCode.URY to "MLU",
    CountryCode.PER to "MPE",
    CountryCode.VEN to "MLV",
    CountryCode.PAN to "MPA",
    CountryCode.BOL to "MBO",
    CountryCode.PRY to "MPY",
    CountryCode.ECU to "MEC",
    CountryCode.DOM to "MRD",
    CountryCode.CRI to "MCR",
    CountryCode.GTM to "MGT",
    CountryCode.HND to "MHN",
    CountryCode.SLV to "MSV",
    CountryCode.NIC to "MNI",
    CountryCode.CUB to "MCU",
)

internal fun CountryCode?.toSiteId(): String = COUNTRY_CODE_TO_SITE_ID[this].orEmpty()
