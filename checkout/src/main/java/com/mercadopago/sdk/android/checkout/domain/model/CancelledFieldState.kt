package com.mercadopago.sdk.android.checkout.domain.model

import com.mercadopago.sdk.android.checkout.core.model.CardType

/**
 * Represents the state of a form field at a specific point in time.
 *
 * This data class combines a form field identifier with its current state,
 * allowing tracking of field validation status during form interaction or
 * at cancellation time.
 *
 * @property field The identifier of the form field (e.g., card number, expiration date)
 * @property state The current state of the field (e.g., valid, empty, incomplete, invalid)
 */
data class CancelledFieldState(
    val field: Field,
    val state: State,
)

/**
 * The form field identifiers.
 */
enum class Field {
    /** Card number field */
    CARD_NUMBER,

    /** Card holder name field */
    CARD_HOLDER,

    /** Card expiration date field */
    EXPIRATION_DATE,

    /** Card security code (CVV) field */
    SECURITY_CODE,

    /** Document identification field */
    DOCUMENT,
}

/**
 * The state of a field at cancellation time.
 */
sealed class State {
    /**
     * The field was filled with a valid value.
     */
    data object Valid : State()

    /**
     * The field was left empty.
     */
    data object Empty : State()

    /**
     * The field was partially filled.
     */
    data object Incomplete : State()

    /**
     * The field contained an invalid value.
     */
    data object Invalid : State()

    /**
     * The card brand is not accepted by the seller.
     * @property brand The card brand that is not accepted (e.g., "visa", "mastercard")
     */
    data class CardBrandNotAccepted(val brand: String) : State()

    /**
     * The card type is not accepted by the seller.
     * @property cardType The card type that is not accepted (e.g., credit card, debit card)
     */
    data class CardTypeNotAccepted(val cardType: CardType?) : State()
}
