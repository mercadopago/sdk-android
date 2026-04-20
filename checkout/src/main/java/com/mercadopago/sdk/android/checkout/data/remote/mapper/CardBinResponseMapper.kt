package com.mercadopago.sdk.android.checkout.data.remote.mapper

import com.mercadopago.sdk.android.checkout.data.remote.response.CardBinResponse
import com.mercadopago.sdk.android.checkout.data.remote.response.CardNumberConfigResponse
import com.mercadopago.sdk.android.checkout.data.remote.response.FieldErrorTranslationResponse
import com.mercadopago.sdk.android.checkout.data.remote.response.FieldTranslationResponse
import com.mercadopago.sdk.android.checkout.data.remote.response.InstallmentsTranslationResponse
import com.mercadopago.sdk.android.checkout.data.remote.response.IssuerResponse
import com.mercadopago.sdk.android.checkout.data.remote.response.QuotaResponse
import com.mercadopago.sdk.android.checkout.data.remote.response.SecurityCodeConfigResponse
import com.mercadopago.sdk.android.checkout.data.remote.response.SecurityCodeTranslationResponse
import com.mercadopago.sdk.android.checkout.data.remote.response.TranslationsResponse
import com.mercadopago.sdk.android.checkout.domain.model.BinIssuer
import com.mercadopago.sdk.android.checkout.domain.model.BinSecurityCodeConfig
import com.mercadopago.sdk.android.checkout.domain.model.CardBinData
import com.mercadopago.sdk.android.checkout.domain.model.CardFormTranslations
import com.mercadopago.sdk.android.checkout.domain.model.CardNumberConfig
import com.mercadopago.sdk.android.checkout.domain.model.FieldErrorTranslation
import com.mercadopago.sdk.android.checkout.domain.model.FieldTranslation
import com.mercadopago.sdk.android.checkout.domain.model.InstallmentsFieldTranslation
import com.mercadopago.sdk.android.checkout.domain.model.Quota
import com.mercadopago.sdk.android.checkout.domain.model.SecurityCodeFieldTranslation

internal fun CardBinResponse.toDomain(): CardBinData =
    CardBinData(
        id = id,
        paymentTypeId = paymentTypeId,
        cardNumber = cardNumber?.toDomain(),
        securityCode = securityCode?.toDomain(),
        issuers = issuers?.map { it.toDomain() } ?: emptyList(),
        quotas = installment?.quotas?.map { it.toDomain() } ?: emptyList(),
        translations = translations?.toDomain(),
    )

private fun CardNumberConfigResponse.toDomain(): CardNumberConfig =
    CardNumberConfig(
        length = length,
        validation = validation,
        mask = mask,
    )

private fun SecurityCodeConfigResponse.toDomain(): BinSecurityCodeConfig =
    BinSecurityCodeConfig(
        mode = mode,
        length = length,
        cardLocation = cardLocation,
    )

private fun IssuerResponse.toDomain(): BinIssuer =
    BinIssuer(
        id = id,
        name = name,
        secureThumbnail = secureThumbnail,
    )

private fun QuotaResponse.toDomain(): Quota =
    Quota(
        quantity = quantity,
        installmentAmount = installmentAmount,
        totalAmount = totalAmount,
        label = label,
        discountRate = discountRate,
    )

private fun TranslationsResponse.toDomain(): CardFormTranslations =
    CardFormTranslations(
        cardNumber = cardNumber?.toDomain(),
        cardHolderName = cardHolderName?.toDomain(),
        expirationDate = expirationDate?.toDomain(),
        securityCode = securityCode?.toDomain(),
        identification = identification?.toDomain(),
        installments = installments?.toDomain(),
    )

private fun FieldTranslationResponse.toDomain(): FieldTranslation =
    FieldTranslation(
        label = label,
        placeholder = placeholder,
        helper = helper,
        error = error?.toDomain(),
    )

private fun FieldErrorTranslationResponse.toDomain(): FieldErrorTranslation =
    FieldErrorTranslation(
        invalid = invalid,
        incomplete = incomplete,
    )

private fun SecurityCodeTranslationResponse.toDomain(): SecurityCodeFieldTranslation =
    SecurityCodeFieldTranslation(
        label = label,
        placeholder = placeholder,
        helper = helper,
        tooltip = tooltip,
        error = error?.toDomain(),
    )

private fun InstallmentsTranslationResponse.toDomain(): InstallmentsFieldTranslation =
    InstallmentsFieldTranslation(
        label = label,
        installmentsSelectorPlaceholder = installmentsSelector?.placeholder,
    )
