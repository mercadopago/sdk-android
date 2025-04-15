package com.mercadopago.sdk.android.coremethods.data.remote.mappers

import com.mercadopago.sdk.android.coremethods.data.remote.response.AgreementsResponse
import com.mercadopago.sdk.android.coremethods.data.remote.response.InstallmentsResponse
import com.mercadopago.sdk.android.coremethods.data.remote.response.IssuerResponse
import com.mercadopago.sdk.android.coremethods.data.remote.response.PayerCostResponse
import com.mercadopago.sdk.android.coremethods.domain.model.Agreements
import com.mercadopago.sdk.android.coremethods.domain.model.Installment
import com.mercadopago.sdk.android.coremethods.domain.model.Issuer
import com.mercadopago.sdk.android.coremethods.domain.model.MerchantAccount
import com.mercadopago.sdk.android.coremethods.domain.model.PayerCost
import com.mercadopago.sdk.android.coremethods.domain.model.TimeFrame

internal fun InstallmentsResponse.toModel() =
    Installment(
        paymentMethodId = this.paymentMethodId,
        paymentTypeId = this.paymentTypeId,
        issuer = this.issuer?.toModel(),
        processingMode = this.processingMode,
        merchantAccountId = this.merchantAccountId,
        payerCost = this.payerCost?.map { payerCost -> payerCost.toModel() },
        agreements = this.agreements?.map { agreement -> agreement.toModel() },
    )

internal fun PayerCostResponse.toModel() =
    PayerCost(
        instalments = this.instalments,
        installmentAmount = this.installmentAmount,
        instalmentsRate = this.instalmentsRate,
        installmentRateCollector = this.installmentRateCollector,
        totalAmount = this.totalAmount,
        minAllowedAmount = this.minAllowedAmount,
        maxAllowedAmount = this.maxAllowedAmount,
        discountRate = this.discountRate,
        reimbursementRate = this.reimbursementRate,
        labels = this.labels,
        paymentMethodOptionId = this.paymentMethodOptionId,
    )

internal fun IssuerResponse.toModel() =
    Issuer(
        id = this.id,
        thumbnail = this.thumbnail,
        default = this.default
    )

internal fun AgreementsResponse.toModel() =
    Agreements(
        merchantAccount = this.merchantAccount?.map { merchantAccount ->
            MerchantAccount(
                id = merchantAccount.id,
                paymentMethodOptionId = merchantAccount.paymentMethodOptionId,
            )
        },
        timeFrame = this.timeFrame?.let { timeFrame ->
            TimeFrame(
                startDate = timeFrame.startDate,
                endDate = timeFrame.endDate,
            )
        },
    )
