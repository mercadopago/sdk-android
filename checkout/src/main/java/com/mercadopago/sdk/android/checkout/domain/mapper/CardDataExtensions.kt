@file:Suppress("TooManyFunctions")

package com.mercadopago.sdk.android.checkout.domain.mapper

import com.mercadopago.sdk.android.checkout.core.model.CardBrand
import com.mercadopago.sdk.android.checkout.core.model.CardType
import com.mercadopago.sdk.android.checkout.domain.model.CardData
import com.mercadopago.sdk.android.checkout.domain.model.SecurityCode
import com.mercadopago.sdk.android.checkout.presentation.state.CardNumberState
import com.mercadopago.sdk.android.checkout.presentation.state.DEFAULT_CARD_MASK
import com.mercadopago.sdk.android.checkout.presentation.state.DEFAULT_MAX_CARD_LENGTH
import com.mercadopago.sdk.android.checkout.presentation.state.IdentificationTypeState
import com.mercadopago.sdk.android.coremethods.domain.model.PaymentMethod
import com.mercadopago.sdk.android.checkout.core.model.PaymentMethod as CheckoutPaymentMethod

private const val CARD_LENGTH_8 = 8
private const val CARD_LENGTH_9 = 9
private const val CARD_LENGTH_10 = 10
private const val CARD_LENGTH_11 = 11
private const val CARD_LENGTH_12 = 12
private const val CARD_LENGTH_13 = 13
private const val CARD_LENGTH_14 = 14
private const val CARD_LENGTH_15 = 15
private const val CARD_LENGTH_16 = 16
private const val CARD_LENGTH_17 = 17
private const val CARD_LENGTH_19 = 19

private const val CARD_LENGTH_8_MASK = "#### ####"
private const val CARD_LENGTH_9_MASK = "#### #####"
private const val CARD_LENGTH_10_MASK = "#### ######"
private const val CARD_LENGTH_11_MASK = "#### #### ###"
private const val CARD_LENGTH_12_MASK = "#### #### ####"
private const val CARD_LENGTH_13_MASK = "#### ###### ###"
private const val CARD_LENGTH_14_MASK = "#### ###### ####"
private const val CARD_LENGTH_15_MASK = "#### ###### #####"
private const val CARD_LENGTH_17_MASK = "#### #### #### #####"
private const val CARD_LENGTH_19_MASK = "#### #### #### #### ###"

private const val DEFAULT_SECURITY_CODE_LENGTH = 3
private const val DEFAULT_SECURITY_CODE_MODE = "mandatory"
private const val SECURITY_CODE_LOCATION = "back"
private const val ISSUER_ID = "issuer_id"

internal fun PaymentMethod.toSecurityCode(): SecurityCode =
    SecurityCode(
        length = card?.securityCode?.length ?: DEFAULT_SECURITY_CODE_LENGTH,
        mode = card?.securityCode?.mode ?: DEFAULT_SECURITY_CODE_MODE,
        location = card?.securityCode?.location ?: SECURITY_CODE_LOCATION,
    )

internal fun SecurityCode.isOptional(): Boolean = length <= 0

internal fun CardData.getLength(): Int = paymentMethod.card?.length?.max ?: DEFAULT_MAX_CARD_LENGTH

internal fun SecurityCode.getMessage(): String {
    val location = if (location == "back") {
        "no verso"
    } else {
        "na parte da frente"
    }
    return "É um número de $length dígitos que está $location do seu cartão."
}

internal fun Int.toMask(): String =
    when (this) {
        CARD_LENGTH_8 -> CARD_LENGTH_8_MASK
        CARD_LENGTH_9 -> CARD_LENGTH_9_MASK
        CARD_LENGTH_10 -> CARD_LENGTH_10_MASK
        CARD_LENGTH_11 -> CARD_LENGTH_11_MASK
        CARD_LENGTH_12 -> CARD_LENGTH_12_MASK
        CARD_LENGTH_13 -> CARD_LENGTH_13_MASK
        CARD_LENGTH_14 -> CARD_LENGTH_14_MASK
        CARD_LENGTH_15 -> CARD_LENGTH_15_MASK
        CARD_LENGTH_16 -> DEFAULT_CARD_MASK
        CARD_LENGTH_17 -> CARD_LENGTH_17_MASK
        CARD_LENGTH_19 -> CARD_LENGTH_19_MASK
        else -> DEFAULT_CARD_MASK
    }

internal fun PaymentMethod.hasIssuers() =
    this.additionalInfoNeeded?.contains(ISSUER_ID) == true &&
        this.id != null

internal fun PaymentMethod.matchesCardBrand(
    cardBrands: List<CardBrand>,
): Boolean = cardBrands.isEmpty() || cardBrands.any { it.name.equals(this.id, ignoreCase = true) }

internal fun PaymentMethod.matchesCardType(
    cardTypes: List<CardType>,
): Boolean = cardTypes.isEmpty() || cardTypes.any { it.value.equals(this.paymentTypeId, ignoreCase = true) }

internal fun List<CheckoutPaymentMethod>?.extractCardFilters(): Pair<List<CardType>, List<CardBrand>> {
    val cardPayment = this?.filterIsInstance<CheckoutPaymentMethod.Card>()?.firstOrNull()
    return cardPayment?.allowedTypes.orEmpty() to cardPayment?.allowedBrands.orEmpty()
}

internal fun CardNumberState.isComplete() = length == maxLength

internal fun IdentificationTypeState.isComplete(
    length: Int,
) = selected?.maxLength == length || selected?.minLength == length
