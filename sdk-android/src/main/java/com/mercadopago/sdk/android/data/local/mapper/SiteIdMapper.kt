package com.mercadopago.sdk.android.data.local.mapper

import com.mercadopago.sdk.android.domain.model.CountryCode

@Suppress("CyclomaticComplexMethod")
internal fun CountryCode.toSiteId(): String = when (this) {
    CountryCode.ARG -> "MLA"
    CountryCode.BRA -> "MLB"
    CountryCode.CHL -> "MLC"
    CountryCode.COL -> "MCO"
    CountryCode.MEX -> "MLM"
    CountryCode.CRI -> "MCR"
    CountryCode.PER -> "MPE"
    CountryCode.ECU -> "MEC"
    CountryCode.DOM -> "MRD"
    CountryCode.URY -> "MLU"
    CountryCode.VEN -> "MLV"
    CountryCode.PAN -> "MPA"
    CountryCode.BOL -> "MBO"
    CountryCode.PRY -> "MPY"
    CountryCode.GTM -> "MGT"
    CountryCode.HND -> "MHN"
    CountryCode.SLV -> "MSV"
    CountryCode.NIC -> "MNI"
    CountryCode.CUB -> "MCU"
}
