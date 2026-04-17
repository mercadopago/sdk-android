package com.mercadopago.sdk.android.checkout.domain.model

import com.mercadopago.sdk.android.checkout.data.remote.response.IdentificationType

internal data class CardFormInitializationOutput(
    val title: String,
    val button: String,
    val fields: CardFormFields,
    val identificationTypes: List<IdentificationType>,
)

internal data class CardFormFields(
    val cardNumber: CardNumberField,
    val holderName: CardHolderField,
    val expirationDate: ExpirationDateField,
    val securityCode: SecurityCodeField,
    val document: DocumentField,
)

internal data class CardNumberField(
    val label: String,
    val placeholder: String,
    val validation: CardNumberValidation,
    val config: CardFieldConfig,
)

internal data class CardNumberValidation(
    val errorEmpty: String,
    val errorIncomplete: String,
    val errorInvalid: String,
    val errorMethodNotAllowed: String,
    val errorTypeNotAllowed: String,
)

internal data class CardHolderField(
    val label: String,
    val placeholder: String,
    val validation: Validation,
    val config: CardFieldConfig,
)

internal data class ExpirationDateField(
    val label: String,
    val placeholder: String,
    val validation: Validation,
    val config: CardFieldConfig,
)

internal data class SecurityCodeField(
    val label: String,
    val placeholder: String,
    val helper: String,
    val tooltip: String,
    val validation: Validation,
    val config: CardFieldConfig,
)

internal data class DocumentField(
    val label: String,
    val validation: Validation,
)

internal data class Validation(
    val errorEmpty: String,
    val errorIncomplete: String,
    val errorInvalid: String,
)

internal data class CardFieldConfig(
    val type: String,
    val length: LengthRange,
)

internal data class LengthRange(
    val min: Int,
    val max: Int,
)
