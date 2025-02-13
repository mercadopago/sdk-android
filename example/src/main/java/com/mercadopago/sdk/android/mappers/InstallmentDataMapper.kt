package com.mercadopago.sdk.android.mappers

import com.mercadopago.sdk.android.coremethods.domain.model.PayerCost
import com.mercadopago.sdk.android.presentation.data.Installment

fun List<PayerCost>.toInstallmentModel() =
    this.map {
        Installment(
            value = "${it.instalments}x R$: ${it.installmentAmount} (R$: ${it.totalAmount})"
        )
    }
