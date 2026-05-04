package com.mercadopago.sdk.android.checkout.domain.mapper

import com.mercadopago.sdk.android.checkout.data.remote.response.CardFormInitResponse
import com.mercadopago.sdk.android.checkout.data.remote.response.CardNumberConfig
import com.mercadopago.sdk.android.checkout.data.remote.response.DocumentTranslations
import com.mercadopago.sdk.android.checkout.data.remote.response.ExpirationDateConfig
import com.mercadopago.sdk.android.checkout.data.remote.response.FieldTranslations
import com.mercadopago.sdk.android.checkout.data.remote.response.HolderNameConfig
import com.mercadopago.sdk.android.checkout.data.remote.response.LengthConfig
import com.mercadopago.sdk.android.checkout.data.remote.response.SecurityCodeConfig
import com.mercadopago.sdk.android.checkout.data.remote.response.SecurityCodeTranslations
import com.mercadopago.sdk.android.checkout.domain.model.CardFieldConfig
import com.mercadopago.sdk.android.checkout.domain.model.CardFormFields
import com.mercadopago.sdk.android.checkout.domain.model.CardFormInitializationOutput
import com.mercadopago.sdk.android.checkout.domain.model.CardHolderField
import com.mercadopago.sdk.android.checkout.domain.model.CardNumberField
import com.mercadopago.sdk.android.checkout.domain.model.CardNumberValidation
import com.mercadopago.sdk.android.checkout.domain.model.DocumentField
import com.mercadopago.sdk.android.checkout.domain.model.ExpirationDateField
import com.mercadopago.sdk.android.checkout.domain.model.IdentificationTypeItem
import com.mercadopago.sdk.android.checkout.domain.model.LengthRange
import com.mercadopago.sdk.android.checkout.domain.model.SecurityCodeField
import com.mercadopago.sdk.android.checkout.domain.model.Validation
import com.mercadopago.sdk.android.checkout.data.remote.response.IdentificationType as ResponseIdentificationType

internal fun CardFormInitResponse.toDomain(): CardFormInitializationOutput =
    CardFormInitializationOutput(
        title = translations.cardFormTitle,
        button = translations.cardFormFooterButtonLabel,
        fields = CardFormFields(
            cardNumber = cardNumber.toCardNumberField(translations.cardNumber),
            holderName = holderName.toCardHolderField(translations.holderName),
            expirationDate = expirationDate.toExpirationDateField(translations.expirationDate),
            securityCode = securityCode.toSecurityCodeField(translations.securityCode),
            document = translations.document.toDocumentField(),
        ),
        identificationTypes = identificationTypes.map { it.toDomain() },
    )

private fun CardNumberConfig.toCardNumberField(
    translations: FieldTranslations,
): CardNumberField =
    CardNumberField(
        label = translations.label,
        placeholder = translations.placeholder,
        validation = CardNumberValidation(
            errorEmpty = translations.errorEmptyField,
            errorIncomplete = translations.errorIncompleteField,
            errorInvalid = translations.errorInvalidField,
            // TechDebt
            errorMethodNotAllowed = "",
            errorTypeNotAllowed = "",
        ),
        config = CardFieldConfig(
            type = type,
            length = length.toLengthRange(),
        ),
    )

private fun HolderNameConfig.toCardHolderField(
    translations: FieldTranslations,
): CardHolderField =
    CardHolderField(
        label = translations.label,
        placeholder = translations.placeholder,
        validation = Validation(
            errorEmpty = translations.errorEmptyField,
            errorIncomplete = translations.errorIncompleteField,
            errorInvalid = translations.errorInvalidField,
        ),
        config = CardFieldConfig(
            type = type,
            length = length.toLengthRange(),
        ),
    )

private fun ExpirationDateConfig.toExpirationDateField(
    translations: FieldTranslations,
): ExpirationDateField =
    ExpirationDateField(
        label = translations.label,
        placeholder = translations.placeholder,
        validation = Validation(
            errorEmpty = translations.errorEmptyField,
            errorIncomplete = translations.errorIncompleteField,
            errorInvalid = translations.errorInvalidField,
        ),
        config = CardFieldConfig(
            type = type,
            length = length.toLengthRange(),
        ),
    )

private fun SecurityCodeConfig.toSecurityCodeField(
    translations: SecurityCodeTranslations,
): SecurityCodeField =
    SecurityCodeField(
        label = translations.label,
        placeholder = translations.placeholder,
        helper = translations.helper.orEmpty(),
        tooltip = translations.tooltip,
        validation = Validation(
            errorEmpty = translations.errorEmptyField,
            errorIncomplete = translations.errorIncompleteField,
            errorInvalid = translations.errorInvalidField.orEmpty(),
        ),
        config = CardFieldConfig(
            type = type,
            length = LengthRange(
                min = length,
                max = length,
            ),
        ),
    )

private fun DocumentTranslations.toDocumentField(): DocumentField =
    DocumentField(
        label = label,
        validation = Validation(
            errorEmpty = errorEmptyField,
            errorIncomplete = errorIncompleteField,
            errorInvalid = errorInvalidField,
        ),
    )

private fun LengthConfig.toLengthRange(): LengthRange =
    LengthRange(
        min = min,
        max = max,
    )

private fun ResponseIdentificationType.toDomain(): IdentificationTypeItem =
    IdentificationTypeItem(
        id = id,
        name = name,
        minLength = minLength,
        maxLength = maxLength,
        placeholder = placeholder,
        mask = mask,
        type = type,
        sequence = sequence,
    )
