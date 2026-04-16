package com.mercadopago.sdk.android.checkout.domain.mapper

import com.mercadopago.sdk.android.checkout.data.remote.mapper.toDomain
import com.mercadopago.sdk.android.checkout.data.remote.response.CardFormInitResponse
import com.mercadopago.sdk.android.checkout.data.remote.response.Translations
import com.mercadopago.sdk.android.checkout.domain.model.CardFormInitialization
import com.mercadopago.sdk.android.checkout.domain.model.CardNumberConfiguration
import com.mercadopago.sdk.android.checkout.domain.model.DocumentFieldTranslations
import com.mercadopago.sdk.android.checkout.domain.model.ExpirationDateConfiguration
import com.mercadopago.sdk.android.checkout.domain.model.FieldTranslations
import com.mercadopago.sdk.android.checkout.domain.model.FormTranslations
import com.mercadopago.sdk.android.checkout.domain.model.HolderNameConfiguration
import com.mercadopago.sdk.android.checkout.domain.model.SecurityCodeConfiguration
import com.mercadopago.sdk.android.checkout.domain.model.SecurityCodeFieldTranslations

internal fun CardFormInitResponse.toDomain(): CardFormInitialization =
    CardFormInitialization(
        identificationTypes = identificationTypes.map { it.toDomain() },
        cardNumber = CardNumberConfiguration(
            type = cardNumber.type,
            minLength = cardNumber.length.min,
            maxLength = cardNumber.length.max,
            mask = cardNumber.mask,
        ),
        securityCode = SecurityCodeConfiguration(
            length = securityCode.length,
            type = securityCode.type,
        ),
        holderName = HolderNameConfiguration(
            type = holderName.type,
            minLength = holderName.length.min,
            maxLength = holderName.length.max,
        ),
        expirationDate = ExpirationDateConfiguration(
            type = expirationDate.type,
            mask = expirationDate.mask,
            minLength = expirationDate.length.min,
            maxLength = expirationDate.length.max,
        ),
        translations = translations.toDomain(),
    )

private fun Translations.toDomain(): FormTranslations =
    FormTranslations(
        cardFormTitle = cardFormTitle,
        cardNumber = FieldTranslations(
            label = cardNumber.label,
            placeholder = cardNumber.placeholder,
            helper = cardNumber.helper.orEmpty(),
            errorEmptyField = cardNumber.errorEmptyField,
            errorIncompleteField = cardNumber.errorIncompleteField,
            errorInvalidField = cardNumber.errorInvalidField,
        ),
        holderName = FieldTranslations(
            label = holderName.label,
            placeholder = holderName.placeholder,
            helper = holderName.helper.orEmpty(),
            errorEmptyField = holderName.errorEmptyField,
            errorIncompleteField = holderName.errorIncompleteField,
            errorInvalidField = holderName.errorInvalidField,
        ),
        expirationDate = FieldTranslations(
            label = expirationDate.label,
            placeholder = expirationDate.placeholder,
            helper = expirationDate.helper.orEmpty(),
            errorEmptyField = expirationDate.errorEmptyField,
            errorIncompleteField = expirationDate.errorIncompleteField,
            errorInvalidField = expirationDate.errorInvalidField,
        ),
        securityCode = SecurityCodeFieldTranslations(
            label = securityCode.label,
            placeholder = securityCode.placeholder,
            helper = securityCode.helper.orEmpty(),
            tooltip = securityCode.tooltip,
            errorEmptyField = securityCode.errorEmptyField,
            errorIncompleteField = securityCode.errorIncompleteField,
            errorInvalidField = securityCode.errorInvalidField.orEmpty(),
        ),
        document = DocumentFieldTranslations(
            label = document.label,
            errorEmptyField = document.errorEmptyField,
            errorIncompleteField = document.errorIncompleteField,
            errorInvalidField = document.errorInvalidField,
        ),
        payButtonLabel = installments.payButtonLabel,
    )
