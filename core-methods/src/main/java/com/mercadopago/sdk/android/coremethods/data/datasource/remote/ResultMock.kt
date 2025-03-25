package com.mercadopago.sdk.android.coremethods.data.datasource.remote

import com.mercadopago.sdk.android.core.utils.KoverIgnore
import com.mercadopago.sdk.android.coremethods.domain.model.IdentificationType
import com.mercadopago.sdk.android.coremethods.domain.model.Installment
import com.mercadopago.sdk.android.coremethods.domain.model.PayerCost

@KoverIgnore("mock value")
internal fun getPreviewIdentificationTypes() = listOf(
    IdentificationType(
        id = "CPF",
        type = "number",
        name = "CPF",
        minLength = 11,
        maxLength = 11,
    ),
    IdentificationType(
        id = "CNPJ",
        type = "number",
        name = "CNPJ",
        minLength = 11,
        maxLength = 11,
    ),
)

@KoverIgnore("mock value")
internal fun getPreviewInstallmentList() = Installment(
    payerCost = listOf(
        PayerCost(
            instalments = 1,
            installmentAmount = 1000,
            totalAmount = 1000.00f,
        ),
        PayerCost(
            instalments = 2,
            installmentAmount = 500,
            totalAmount = 1000.00f,
        ),
        PayerCost(
            instalments = 3,
            installmentAmount = 370,
            totalAmount = 1080.00f,
        ),
        PayerCost(
            instalments = 4,
            installmentAmount = 250,
            totalAmount = 1200.00f,
        ),
        PayerCost(
            instalments = 5,
            installmentAmount = 150,
            totalAmount = 1700.00f,
        ),
        PayerCost(
            instalments = 6,
            installmentAmount = 120,
            totalAmount = 1800.00f,
        ),
        PayerCost(
            instalments = 7,
            installmentAmount = 95,
            totalAmount = 1900.00f,
        ),
        PayerCost(
            instalments = 8,
            installmentAmount = 67,
            totalAmount = 2200.00f,
        ),
    ),
)
