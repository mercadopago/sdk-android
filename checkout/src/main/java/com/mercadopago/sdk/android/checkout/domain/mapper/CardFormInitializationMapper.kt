package com.mercadopago.sdk.android.checkout.domain.mapper

import com.mercadopago.android.sdk.checkout.R
import com.mercadopago.sdk.android.checkout.data.remote.mapper.toDomain
import com.mercadopago.sdk.android.checkout.data.remote.response.CardFormInitResponse
import com.mercadopago.sdk.android.checkout.data.remote.response.CardNumberConfig
import com.mercadopago.sdk.android.checkout.data.remote.response.CardNumberTranslations
import com.mercadopago.sdk.android.checkout.data.remote.response.DocumentTranslations
import com.mercadopago.sdk.android.checkout.data.remote.response.ExpirationDateConfig
import com.mercadopago.sdk.android.checkout.data.remote.response.ExpirationDateTranslations
import com.mercadopago.sdk.android.checkout.data.remote.response.HolderNameConfig
import com.mercadopago.sdk.android.checkout.data.remote.response.HolderNameTranslations
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
import com.mercadopago.sdk.android.checkout.domain.model.LengthRange
import com.mercadopago.sdk.android.checkout.domain.model.SecurityCodeField
import com.mercadopago.sdk.android.checkout.domain.model.Validation
import com.mercadopago.sdk.android.checkout.domain.provider.StringProvider

internal fun CardFormInitResponse.toDomain(
    stringProvider: StringProvider,
): CardFormInitializationOutput =
    CardFormInitializationOutput(
        title = translations.cardFormTitle,
        button = translations.installments.payButtonLabel,
        fields = CardFormFields(
            cardNumber = cardNumber.toCardNumberField(translations.cardNumber, stringProvider),
            holderName = holderName.toCardHolderField(translations.holderName),
            expirationDate = expirationDate.toExpirationDateField(translations.expirationDate),
            securityCode = securityCode.toSecurityCodeField(translations.securityCode),
            document = translations.document.toDocumentField(),
        ),
        identificationTypes = identificationTypes.map { it.toDomain() },
    )

private fun CardNumberConfig.toCardNumberField(
    translations: CardNumberTranslations,
    stringProvider: StringProvider,
): CardNumberField =
    CardNumberField(
        label = translations.label,
        placeholder = translations.placeholder,
        validation = CardNumberValidation(
            errorEmpty = translations.errorEmptyField,
            errorIncomplete = translations.errorIncompleteField,
            errorInvalid = translations.errorInvalidField,
            errorMethodNotAllowed = stringProvider.getString(R.string.card_form_error_card_brand_not_accepted),
            errorTypeNotAllowed = stringProvider.getString(R.string.card_form_error_card_type_not_accepted),
        ),
        config = CardFieldConfig(
            type = type,
            length = LengthRange(
                min = length.min,
                max = length.max,
            ),
        ),
    )

private fun HolderNameConfig.toCardHolderField(
    translations: HolderNameTranslations,
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
            length = LengthRange(
                min = length.min,
                max = length.max,
            ),
        ),
    )

private fun ExpirationDateConfig.toExpirationDateField(
    translations: ExpirationDateTranslations,
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
            length = LengthRange(
                min = length.min,
                max = length.max,
            ),
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
