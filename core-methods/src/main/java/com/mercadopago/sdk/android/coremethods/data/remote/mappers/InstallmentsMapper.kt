package com.mercadopago.sdk.android.coremethods.data.remote.mappers

import com.mercadopago.sdk.android.coremethods.data.remote.response.InstallmentsResponse
import com.mercadopago.sdk.android.coremethods.domain.model.Agreements
import com.mercadopago.sdk.android.coremethods.domain.model.Installment
import com.mercadopago.sdk.android.coremethods.domain.model.Issuer
import com.mercadopago.sdk.android.coremethods.domain.model.MerchantAccount
import com.mercadopago.sdk.android.coremethods.domain.model.PayerCost
import com.mercadopago.sdk.android.coremethods.domain.model.TimeFrame

internal fun InstallmentsResponse.toModel() = Installment(
    paymentMethodId = this.paymentMethodId,
    paymentTypeId = this.paymentTypeId,
    issuer = this.issuer?.let { Issuer(it.id, it.thumbnail) },
    processingMode = this.processingMode,
    merchantAccountId = this.merchantAccountId,
    payerCost = this.payerCost?.map { payerCost ->
        PayerCost(
            instalments = payerCost.instalments,
            installmentAmount = payerCost.installmentAmount,
            instalmentsRate = payerCost.instalmentsRate,
            installmentRateCollector = payerCost.installmentRateCollector,
            totalAmount = payerCost.totalAmount,
            minAllowedAmount = payerCost.minAllowedAmount,
            maxAllowedAmount = payerCost.maxAllowedAmount,
            discountRate = payerCost.discountRate,
            reimbursementRate = payerCost.reimbursementRate,
            labels = payerCost.labels,
            paymentMethodOptionId = payerCost.paymentMethodOptionId
        )
    },
    agreements = this.agreements?.map { agreement ->
        Agreements(
            merchantAccount = agreement.merchantAccount?.map { merchantAccount ->
                MerchantAccount(
                    id = merchantAccount.id,
                    paymentMethodOptionId = merchantAccount.paymentMethodOptionId
                )
            },
            timeFrame = agreement.timeFrame?.let { timeFrame ->
                TimeFrame(
                    startDate = timeFrame.startDate,
                    endDate = timeFrame.endDate
                )
            }
        )
    }
)
