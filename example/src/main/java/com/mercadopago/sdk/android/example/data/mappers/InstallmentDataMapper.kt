package com.mercadopago.sdk.android.example.data.mappers

import com.mercadopago.sdk.android.coremethods.domain.model.PayerCost
import com.mercadopago.sdk.android.example.extensions.toCurrencyString
import com.mercadopago.sdk.android.example.data.model.Installment
import kotlin.collections.map

internal fun List<PayerCost>.toInstallmentModel() =
    this.map {
        Installment(
            value = "${it.instalments}x ${it.installmentAmount?.toCurrencyString()} (${it.totalAmount?.toCurrencyString()})"
        )
    }
