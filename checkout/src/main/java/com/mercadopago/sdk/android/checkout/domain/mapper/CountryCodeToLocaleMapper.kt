package com.mercadopago.sdk.android.checkout.domain.mapper

import com.mercadopago.sdk.android.domain.model.CountryCode
import java.util.Locale

internal object CountryCodeToLocaleMapper {
    fun map(
        countryCode: CountryCode?,
    ): Locale =
        when (countryCode) {
            CountryCode.BRA -> Locale("pt", "BR")
            CountryCode.ARG -> Locale("es", "AR")
            CountryCode.MEX -> Locale("es", "MX")
            CountryCode.URY -> Locale("es", "UY")
            CountryCode.COL -> Locale("es", "CO")
            CountryCode.PER -> Locale("es", "PE")
            CountryCode.CHL -> Locale("es", "CL")
            else -> Locale.getDefault()
        }

    fun toLocaleString(
        countryCode: CountryCode?,
    ): String =
        map(countryCode = countryCode).let {
            "${it.language}_${it.country}"
        }
}
