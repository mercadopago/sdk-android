package com.mercadopago.sdk.android.checkout.presentation.mapper

import com.mercadopago.sdk.android.checkout.domain.extensions.maskOrNull
import com.mercadopago.sdk.android.checkout.domain.extensions.maxLengthOrNull
import com.mercadopago.sdk.android.checkout.domain.extensions.toMask
import com.mercadopago.sdk.android.checkout.domain.model.CardFormInitializationOutput
import com.mercadopago.sdk.android.checkout.domain.model.CardHolderField
import com.mercadopago.sdk.android.checkout.domain.model.CardNumberField
import com.mercadopago.sdk.android.checkout.domain.model.CardNumberValidation
import com.mercadopago.sdk.android.checkout.domain.model.DocumentField
import com.mercadopago.sdk.android.checkout.domain.model.ExpirationDateField
import com.mercadopago.sdk.android.checkout.domain.model.IdentificationTypeItem
import com.mercadopago.sdk.android.checkout.domain.model.SecurityCodeField
import com.mercadopago.sdk.android.checkout.domain.model.Validation
import com.mercadopago.sdk.android.checkout.presentation.extensions.getTotal
import com.mercadopago.sdk.android.checkout.presentation.extensions.getTotalDecimalPart
import com.mercadopago.sdk.android.checkout.presentation.state.CardHolderState
import com.mercadopago.sdk.android.checkout.presentation.state.CardNumberState
import com.mercadopago.sdk.android.checkout.presentation.state.CardPaymentScreenState
import com.mercadopago.sdk.android.checkout.presentation.state.ExpirationDateState
import com.mercadopago.sdk.android.checkout.presentation.state.FooterState
import com.mercadopago.sdk.android.checkout.presentation.state.IdentificationTypeState
import com.mercadopago.sdk.android.checkout.presentation.state.SecurityCodeState
import com.mercadopago.sdk.android.checkout.presentation.state.ValidationState
import com.mercadopago.sdk.android.coremethods.domain.model.IdentificationType
import java.math.BigDecimal

internal fun CardFormInitializationOutput.toCardPaymentScreenState(
    totalAmount: BigDecimal? = null,
) = with(fields) {
    val positiveAmount = totalAmount?.takeIf { it.signum() > 0 }
    CardPaymentScreenState(
        title = title,
        currencySymbol = currencySymbol,
        cardNumberState = cardNumber.toCardNumberState(),
        cardHolderState = holderName.toCardHolderState(),
        expirationDateState = expirationDate.toExpirationDateState(),
        secureCodeState = securityCode.toSecurityCodeState(),
        identificationTypeState = document.toIdentificationTypeState(identificationTypes),
        footerState = FooterState(
            title = positiveAmount?.let { footerTitle }.orEmpty(),
            currencySymbol = positiveAmount?.let { currencySymbol }.orEmpty(),
            amountIntegerPart = positiveAmount?.getTotal().orEmpty(),
            amountDecimalPart = positiveAmount?.getTotalDecimalPart().orEmpty(),
            buttonLabel = buttonLabel,
        ),
    )
}

private fun CardNumberField.toCardNumberState(): CardNumberState {
    val maxLength = config.maxLengthOrNull()
    return CardNumberState(
        label = label,
        placeHolder = placeholder,
        validation = validation.toValidationState(),
        maxLength = maxLength ?: CardNumberState().maxLength,
        mask = config.maskOrNull() ?: maxLength?.toMask() ?: CardNumberState().mask,
    )
}

private fun CardHolderField.toCardHolderState() =
    CardHolderState(
        label = label,
        placeHolder = placeholder,
        validation = validation.toValidationState(),
        helper = helper,
    )

private fun ExpirationDateField.toExpirationDateState() =
    ExpirationDateState(
        label = label,
        placeHolder = placeholder,
        validation = validation.toValidationState(),
    )

private fun SecurityCodeField.toSecurityCodeState(): SecurityCodeState {
    val maxLength = config.maxLengthOrNull()
    return SecurityCodeState(
        label = label,
        placeHolder = placeholder,
        helper = helper,
        messageTooltip = tooltip,
        validation = validation.toValidationState(),
        maxLength = maxLength ?: SecurityCodeState().maxLength,
        optional = maxLength == null,
    )
}

private fun DocumentField.toIdentificationTypeState(
    identificationTypes: List<IdentificationTypeItem>,
): IdentificationTypeState {
    val coreTypes = identificationTypes.map { it.toCoreType() }
    val firstItem = identificationTypes.firstOrNull()
    return IdentificationTypeState(
        label = label,
        show = identificationTypes.isNotEmpty(),
        identificationTypes = coreTypes,
        selected = coreTypes.firstOrNull(),
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
        placeholder = placeholder,
    )

internal fun Validation.toValidationState() =
    ValidationState(
        errorEmpty = errorEmpty,
        errorIncomplete = errorIncomplete,
        errorInvalid = errorInvalid,
    )

internal fun CardNumberValidation.toValidationState() =
    ValidationState(
        errorEmpty = errorEmpty,
        errorIncomplete = errorIncomplete,
        errorInvalid = errorInvalid,
    )
