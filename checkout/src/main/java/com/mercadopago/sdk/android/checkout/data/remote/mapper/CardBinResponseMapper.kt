package com.mercadopago.sdk.android.checkout.data.remote.mapper

import com.mercadopago.sdk.android.checkout.data.remote.response.CardBinResponse
import com.mercadopago.sdk.android.checkout.data.remote.response.CardNumberConfigResponse
import com.mercadopago.sdk.android.checkout.data.remote.response.FieldTranslationResponse
import com.mercadopago.sdk.android.checkout.data.remote.response.FieldTranslations
import com.mercadopago.sdk.android.checkout.data.remote.response.IssuerResponse
import com.mercadopago.sdk.android.checkout.data.remote.response.QuotaResponse
import com.mercadopago.sdk.android.checkout.data.remote.response.SecurityCodeConfigResponse
import com.mercadopago.sdk.android.checkout.data.remote.response.SecurityCodeTranslationResponse
import com.mercadopago.sdk.android.checkout.data.remote.response.SecurityCodeTranslations
import com.mercadopago.sdk.android.checkout.data.remote.response.TranslationsResponse
import com.mercadopago.sdk.android.checkout.domain.model.BinIssuer
import com.mercadopago.sdk.android.checkout.domain.model.BinSecurityCodeConfig
import com.mercadopago.sdk.android.checkout.domain.model.CardBinData
import com.mercadopago.sdk.android.checkout.domain.model.CardFormTranslations
import com.mercadopago.sdk.android.checkout.domain.model.CardNumberConfig
import com.mercadopago.sdk.android.checkout.domain.model.Quota

internal fun CardBinResponse.toDomain(): CardBinData {
    val paymentMethod = paymentMethods?.firstOrNull()
    return CardBinData(
        id = paymentMethod?.id,
        paymentTypeId = paymentMethod?.paymentTypeId,
        cardNumber = paymentMethod?.cardNumber?.toDomain(),
        securityCode = paymentMethod?.securityCode?.toDomain(),
        issuers = paymentMethod?.issuers?.map { it.toDomain() } ?: emptyList(),
        quotas = installment?.quotas?.map { it.toDomain() } ?: emptyList(),
        translations = translations?.toDomain(),
    )
}

private fun CardNumberConfigResponse.toDomain(): CardNumberConfig =
    CardNumberConfig(
        length = length?.max ?: length?.min,
        validation = validation,
        mask = mask,
    )

private fun SecurityCodeConfigResponse.toDomain(): BinSecurityCodeConfig =
    BinSecurityCodeConfig(
        mode = mode,
        length = length,
        cardLocation = cardLocation,
        tooltip = tooltip,
        placeholder = placeholder,
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
        cardHolderName = cardHolderName?.toHolderNameTranslations(),
        expirationDate = expirationDate?.toExpirationDateTranslations(),
        securityCode = securityCode?.toDomain(),
    )

private fun FieldTranslationResponse.toDomain() =
    FieldTranslations(
        label = label.orEmpty(),
        placeholder = placeholder.orEmpty(),
        helper = helper,
        // TechDebt get error of empty field
        errorEmptyField = "",
        errorIncompleteField = error?.incomplete.orEmpty(),
        errorInvalidField = error?.invalid.orEmpty(),
    )

private fun FieldTranslationResponse.toHolderNameTranslations() =
    FieldTranslations(
        label = label.orEmpty(),
        placeholder = placeholder.orEmpty(),
        helper = helper,
        errorEmptyField = "",
        errorIncompleteField = error?.incomplete.orEmpty(),
        errorInvalidField = error?.invalid.orEmpty(),
    )

private fun FieldTranslationResponse.toExpirationDateTranslations() =
    FieldTranslations(
        label = label.orEmpty(),
        placeholder = placeholder.orEmpty(),
        helper = helper,
        errorEmptyField = "",
        errorIncompleteField = error?.incomplete.orEmpty(),
        errorInvalidField = error?.invalid.orEmpty(),
    )

private fun SecurityCodeTranslationResponse.toDomain() =
    SecurityCodeTranslations(
        label = label.orEmpty(),
        placeholder = placeholder.orEmpty(),
        helper = helper,
        tooltip = tooltip.orEmpty(),
        errorEmptyField = "",
        errorIncompleteField = error?.incomplete.orEmpty(),
    )
