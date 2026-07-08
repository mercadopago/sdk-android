package com.mercadopago.sdk.android.checkout.domain.mapper

import com.mercadopago.sdk.android.checkout.data.remote.response.CardData
import com.mercadopago.sdk.android.checkout.data.remote.response.Installments
import com.mercadopago.sdk.android.checkout.data.remote.response.InstallmentsHeader
import com.mercadopago.sdk.android.checkout.data.remote.response.Quota
import com.mercadopago.sdk.android.checkout.data.remote.response.SecurityCode
import com.mercadopago.sdk.android.checkout.data.remote.response.SecurityCodeField
import com.mercadopago.sdk.android.checkout.data.remote.response.SecurityCodeScreen
import com.mercadopago.sdk.android.checkout.domain.model.CardDataOutput
import com.mercadopago.sdk.android.checkout.domain.model.InstallmentsHeaderOutput
import com.mercadopago.sdk.android.checkout.domain.model.InstallmentsOutput
import com.mercadopago.sdk.android.checkout.domain.model.QuotaOutput
import com.mercadopago.sdk.android.checkout.domain.model.SecurityCodeFieldOutput
import com.mercadopago.sdk.android.checkout.domain.model.SecurityCodeOutput
import com.mercadopago.sdk.android.checkout.domain.model.SecurityCodeScreenOutput

internal fun CardData.toDomain(): CardDataOutput =
    CardDataOutput(
        id = id,
        bin = bin,
        lastFourDigits = lastFourDigits,
        paymentMethodId = paymentMethodId,
        paymentTypeId = paymentTypeId,
        issuerId = issuerId,
        securityCode = securityCode.toDomain(),
        installments = installments?.toDomain(),
    )

internal fun SecurityCode.toDomain(): SecurityCodeOutput =
    SecurityCodeOutput(
        length = length,
        screen = screen?.toDomain(),
    )

internal fun SecurityCodeScreen.toDomain(): SecurityCodeScreenOutput =
    SecurityCodeScreenOutput(
        headerTitle = header.title,
        field = field.toDomain(),
        buttonLabel = button.label,
    )

internal fun SecurityCodeField.toDomain(): SecurityCodeFieldOutput =
    SecurityCodeFieldOutput(
        label = label,
        placeholder = placeholder,
        helper = helper,
        error = error,
    )

internal fun Installments.toDomain(): InstallmentsOutput =
    InstallmentsOutput(
        header = header.toDomain(),
        totalLabel = totalLabel,
        payButtonLabel = payButtonLabel,
        selectionType = selectionType,
        quotas = quotas.map { it.toDomain() },
    )

internal fun InstallmentsHeader.toDomain(): InstallmentsHeaderOutput = InstallmentsHeaderOutput(title = title)

internal fun Quota.toDomain(): QuotaOutput =
    QuotaOutput(
        installments = installments,
        installmentAmount = installmentAmount,
        totalAmount = totalAmount,
        primaryLabel = primaryLabel,
        secondaryLabel = secondaryLabel,
        state = state,
        accessibilityLabel = accessibilityLabel,
    )
