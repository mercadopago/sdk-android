package com.mercadopago.sdk.android.coremethods.data.remote.mappers

import com.mercadopago.sdk.android.coremethods.data.remote.response.CardResponse
import com.mercadopago.sdk.android.coremethods.data.remote.response.FinancialInstitutionResponse
import com.mercadopago.sdk.android.coremethods.data.remote.response.LengthResponse
import com.mercadopago.sdk.android.coremethods.data.remote.response.PaymentMethodResponse
import com.mercadopago.sdk.android.coremethods.data.remote.response.SecurityCodeResponse
import com.mercadopago.sdk.android.coremethods.domain.model.CardModel
import com.mercadopago.sdk.android.coremethods.domain.model.FinancialInstitutionModel
import com.mercadopago.sdk.android.coremethods.domain.model.LengthModel
import com.mercadopago.sdk.android.coremethods.domain.model.PaymentMethod
import com.mercadopago.sdk.android.coremethods.domain.model.SecurityCodeModel

internal fun PaymentMethodResponse.toModel() =
    PaymentMethod(
        financialInstitution = this.financialInstitution?.map { it.toModel() },
        payerCost = this.payerCost?.map { it.toModel() },
        issuer = this.issuer?.toModel(),
        totalFinancialCost = this.totalFinancialCost,
        minAccreditationDays = this.minAccreditationDays,
        maxAccreditationDays = this.maxAccreditationDays,
        merchantAccountId = this.merchantAccountId,
        id = this.id,
        paymentTypeId = this.paymentTypeId,
        accreditationTime = this.accreditationTime,
        card = this.card?.toModel(),
        thumbnail = this.thumbnail,
        bins = this.bins,
        marketplace = this.marketplace,
        deferredCapture = this.deferredCapture,
        agreements = this.agreements?.map { it.toModel() },
        labels = this.labels,
        siteId = this.siteId,
        processingMode = this.processingMode,
        additionalInfoNeeded = this.additionalInfoNeeded,
        status = this.status
    )

internal fun FinancialInstitutionResponse.toModel() =
    FinancialInstitutionModel(
        id = this.id,
        description = this.description
    )

internal fun CardResponse.toModel() =
    CardModel(
        bin = this.bin,
        length = this.length?.toModel(),
        validation = this.validation,
        securityCode = this.securityCode?.toModel()
    )

internal fun LengthResponse.toModel() =
    LengthModel(
        min = this.min,
        max = this.max
    )

internal fun SecurityCodeResponse.toModel() =
    SecurityCodeModel(
        mode = this.mode,
        location = this.location,
        length = this.length
    )
