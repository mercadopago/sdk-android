package com.mercadopago.sdk.android.checkout.presentation.mapper

import com.mercadopago.sdk.android.checkout.domain.model.CardFormInitializationOutput
import com.mercadopago.sdk.android.checkout.domain.model.CardHolderField
import com.mercadopago.sdk.android.checkout.domain.model.CardNumberField
import com.mercadopago.sdk.android.checkout.domain.model.CardNumberValidation
import com.mercadopago.sdk.android.checkout.domain.model.DocumentField
import com.mercadopago.sdk.android.checkout.domain.model.ExpirationDateField
import com.mercadopago.sdk.android.checkout.domain.model.IdentificationTypeItem
import com.mercadopago.sdk.android.checkout.domain.model.SecurityCodeField
import com.mercadopago.sdk.android.checkout.domain.model.Validation
import com.mercadopago.sdk.android.checkout.presentation.state.CardHolderState
import com.mercadopago.sdk.android.checkout.presentation.state.CardNumberState
import com.mercadopago.sdk.android.checkout.presentation.state.CardPaymentScreenState
import com.mercadopago.sdk.android.checkout.presentation.state.ExpirationDateState
import com.mercadopago.sdk.android.checkout.presentation.state.FixedFooterState
import com.mercadopago.sdk.android.checkout.presentation.state.IdentificationTypeState
import com.mercadopago.sdk.android.checkout.presentation.state.SecurityCodeState
import com.mercadopago.sdk.android.checkout.presentation.state.ValidationState
import com.mercadopago.sdk.android.coremethods.domain.model.IdentificationType

internal fun CardFormInitializationOutput.toCardPaymentScreenState() =
    with(fields) {
        CardPaymentScreenState(
            title = title,
            cardNumberState = cardNumber.toCardNumberState(),
            cardHolderState = holderName.toCardHolderState(),
            expirationDateState = expirationDate.toExpirationDateState(),
            secureCodeState = securityCode.toSecurityCodeState(),
            identificationTypeState = document.toIdentificationTypeState(identificationTypes),
            fixedFooterState = FixedFooterState(buttonText = button),
        )
    }

private fun CardNumberField.toCardNumberState() =
    CardNumberState(
        label = label,
        placeHolder = placeholder,
        validation = validation.toValidationState(),
    )

private fun CardHolderField.toCardHolderState() =
    CardHolderState(
        label = label,
        placeHolder = placeholder,
        validation = validation.toValidationState(),
    )

private fun ExpirationDateField.toExpirationDateState() =
    ExpirationDateState(
        label = label,
        placeHolder = placeholder,
        validation = validation.toValidationState(),
    )

private fun SecurityCodeField.toSecurityCodeState() =
    SecurityCodeState(
        label = label,
        placeHolder = placeholder,
        helper = helper,
        messageTooltip = tooltip,
        validation = validation.toValidationState(),
    )

private fun DocumentField.toIdentificationTypeState(
    identificationTypes: List<IdentificationTypeItem>,
): IdentificationTypeState {
    val coreTypes = identificationTypes.map { it.toCoreType() }
    val firstItem = identificationTypes.firstOrNull()
    val placeholdersByTypeId = identificationTypes
        .mapNotNull { item -> item.id?.let { id -> id to item.placeholder } }
        .toMap()
    return IdentificationTypeState(
        label = label,
        show = identificationTypes.isNotEmpty(),
        identificationTypes = coreTypes,
        selected = coreTypes.firstOrNull(),
        placeholdersByTypeId = placeholdersByTypeId,
        placeHolder = firstItem?.placeholder.orEmpty(),
        validation = validation.toValidationState(),
    )
}

private fun IdentificationTypeItem.toCoreType(): IdentificationType =
    IdentificationType(
        id = id,
        name = name,
        type = type,
        minLength = minLength,
        maxLength = maxLength,
        mask = mask,
    )

private fun Validation.toValidationState() =
    ValidationState(
        errorEmpty = errorEmpty,
        errorIncomplete = errorIncomplete,
        errorInvalid = errorInvalid,
    )

private fun CardNumberValidation.toValidationState() =
    ValidationState(
        errorEmpty = errorEmpty,
        errorIncomplete = errorIncomplete,
        errorInvalid = errorInvalid,
    )
