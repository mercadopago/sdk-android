package com.mercadopago.sdk.android.example.mappers

import com.mercadopago.sdk.android.coremethods.domain.model.PayerCost
import com.mercadopago.sdk.android.example.presentation.data.Installment
import kotlin.collections.map

internal fun List<PayerCost>.toInstallmentModel() =
    this.map {
        Installment(
            value = "${it.instalments}x R$: ${it.installmentAmount} (R$: ${it.totalAmount})"
        )
    }
