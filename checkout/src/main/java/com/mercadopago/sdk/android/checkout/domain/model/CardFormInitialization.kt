package com.mercadopago.sdk.android.checkout.domain.model

import com.mercadopago.sdk.android.coremethods.domain.model.IdentificationType

internal data class CardFormInitialization(
    val identificationTypes: List<IdentificationType>,
    val cardNumber: CardNumberConfiguration,
    val securityCode: SecurityCodeConfiguration,
    val holderName: HolderNameConfiguration,
    val expirationDate: ExpirationDateConfiguration,
    val translations: FormTranslations,
)

internal data class CardNumberConfiguration(
    val type: String,
    val minLength: Int,
    val maxLength: Int,
    val mask: String,
)

internal data class SecurityCodeConfiguration(
    val length: Int,
    val type: String,
)

internal data class HolderNameConfiguration(
    val type: String,
    val minLength: Int,
    val maxLength: Int,
)

internal data class ExpirationDateConfiguration(
    val type: String,
    val mask: String,
    val minLength: Int,
    val maxLength: Int,
)

internal data class FormTranslations(
    val cardFormTitle: String,
    val cardNumber: FieldTranslations,
    val holderName: FieldTranslations,
    val expirationDate: FieldTranslations,
    val securityCode: SecurityCodeFieldTranslations,
    val document: DocumentFieldTranslations,
    val payButtonLabel: String,
)

internal data class FieldTranslations(
    val label: String,
    val placeholder: String,
    val helper: String,
    val errorEmptyField: String,
    val errorIncompleteField: String,
    val errorInvalidField: String,
)

internal data class SecurityCodeFieldTranslations(
    val label: String,
    val placeholder: String,
    val helper: String,
    val tooltip: String,
    val errorEmptyField: String,
    val errorIncompleteField: String,
    val errorInvalidField: String,
)

internal data class DocumentFieldTranslations(
    val label: String,
    val errorEmptyField: String,
    val errorIncompleteField: String,
    val errorInvalidField: String,
)
