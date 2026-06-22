package com.mercadopago.sdk.android.checkout.domain.mapper

import com.mercadopago.sdk.android.checkout.data.remote.response.CardFieldTranslations
import com.mercadopago.sdk.android.checkout.data.remote.response.CardIssuerConfig
import com.mercadopago.sdk.android.checkout.data.remote.response.CardNumberFieldConfig
import com.mercadopago.sdk.android.checkout.data.remote.response.CardQuota
import com.mercadopago.sdk.android.checkout.data.remote.response.CardSecurityCodeTranslations
import com.mercadopago.sdk.android.checkout.data.remote.response.CardTranslations
import com.mercadopago.sdk.android.checkout.data.remote.response.InstallmentConfig
import com.mercadopago.sdk.android.checkout.data.remote.response.PaymentMethodConfig
import com.mercadopago.sdk.android.checkout.data.remote.response.SecurityCodeFieldConfig
import com.mercadopago.sdk.android.checkout.domain.model.CardFieldTranslationsOutput
import com.mercadopago.sdk.android.checkout.domain.model.CardHolderNameTranslationsOutput
import com.mercadopago.sdk.android.checkout.domain.model.CardInstallmentsHeaderOutput
import com.mercadopago.sdk.android.checkout.domain.model.CardInstallmentsTranslationsOutput
import com.mercadopago.sdk.android.checkout.domain.model.CardIssuerConfigOutput
import com.mercadopago.sdk.android.checkout.domain.model.CardNumberFieldConfigOutput
import com.mercadopago.sdk.android.checkout.domain.model.CardQuotaOutput
import com.mercadopago.sdk.android.checkout.domain.model.CardSecurityCodeTranslationsOutput
import com.mercadopago.sdk.android.checkout.domain.model.CardTranslationsOutput
import com.mercadopago.sdk.android.checkout.domain.model.InstallmentConfigOutput
import com.mercadopago.sdk.android.checkout.domain.model.PaymentMethodConfigOutput
import com.mercadopago.sdk.android.checkout.domain.model.SecurityCodeFieldConfigOutput

internal fun CardTranslations.toDomain(): CardTranslationsOutput =
    CardTranslationsOutput(
        cardFormTitle = cardFormTitle,
        cardFormFooterButtonLabel = cardFormFooterButtonLabel,
        cardNumber = cardNumber.toDomain(),
        securityCode = securityCode.toDomain(),
        expirationDate = expirationDate.toDomain(),
        holderName = CardHolderNameTranslationsOutput(
            label = holderName.label,
            placeholder = holderName.placeholder,
            helper = holderName.helper,
        ),
        installments = CardInstallmentsTranslationsOutput(
            header = CardInstallmentsHeaderOutput(title = installments.header.title),
            interestFreeLabel = installments.interestFreeLabel,
            totalLabel = installments.totalLabel,
        ),
    )

internal fun CardFieldTranslations.toDomain(): CardFieldTranslationsOutput =
    CardFieldTranslationsOutput(
        label = label,
        placeholder = placeholder,
        errorEmptyField = errorEmptyField,
        errorIncompleteField = errorIncompleteField,
        errorInvalidField = errorInvalidField,
        helper = helper,
    )

internal fun CardSecurityCodeTranslations.toDomain(): CardSecurityCodeTranslationsOutput =
    CardSecurityCodeTranslationsOutput(
        label = label,
        placeholder = placeholder,
        tooltip = tooltip,
        errorEmptyField = errorEmptyField,
        errorIncompleteField = errorIncompleteField,
    )

internal fun InstallmentConfig.toDomain(): InstallmentConfigOutput =
    InstallmentConfigOutput(
        selectionType = selectionType,
        quotas = quotas.map { it.toDomain() },
    )

internal fun CardQuota.toDomain(): CardQuotaOutput =
    CardQuotaOutput(
        installments = installments,
        installmentAmount = installmentAmount,
        totalAmount = totalAmount,
        primaryLabel = primaryLabel,
        secondaryLabel = secondaryLabel,
        state = state,
        accessibilityLabel = accessibilityLabel,
    )

internal fun PaymentMethodConfig.toDomain(): PaymentMethodConfigOutput =
    PaymentMethodConfigOutput(
        id = id,
        paymentTypeId = paymentTypeId,
        cardNumber = cardNumber?.toDomain(),
        securityCode = securityCode?.toDomain(),
        issuers = issuers?.map { it.toDomain() },
    )

internal fun CardNumberFieldConfig.toDomain(): CardNumberFieldConfigOutput =
    CardNumberFieldConfigOutput(
        type = type,
        minLength = length.min,
        maxLength = length.max,
        mask = mask,
    )

internal fun SecurityCodeFieldConfig.toDomain(): SecurityCodeFieldConfigOutput =
    SecurityCodeFieldConfigOutput(
        mode = mode,
        length = length,
        placeholder = placeholder,
    )

internal fun CardIssuerConfig.toDomain(): CardIssuerConfigOutput = CardIssuerConfigOutput(id = id, name = name)
