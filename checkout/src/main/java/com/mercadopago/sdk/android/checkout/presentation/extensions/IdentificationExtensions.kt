package com.mercadopago.sdk.android.checkout.presentation.extensions

import com.mercadopago.sdk.android.coremethods.domain.model.IdentificationType

private const val IDENTIFICATION_TYPE_CPF = "CPF"
private const val IDENTIFICATION_TYPE_CNPJ = "CNPJ"
private const val PLACEHOLDER_CPF = "999.999.999-99"
private const val PLACEHOLDER_CNPJ = "99.999.999/9999-99"

internal fun IdentificationType?.getPlaceholder(): String? =
    when (this?.id) {
        IDENTIFICATION_TYPE_CPF -> PLACEHOLDER_CPF
        IDENTIFICATION_TYPE_CNPJ -> PLACEHOLDER_CNPJ
        else -> null
    }
