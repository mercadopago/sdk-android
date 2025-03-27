package com.mercadopago.sdk.android.coremethods.ui.components.textfield.cardnumber

/**
 * CardNumberTextFieldEvent is a sealed class that represents the
 * events that can be triggered by the card number text field.
 */
interface CardNumberTextFieldEvent {
    /**
     * Informs if the card number bin has been filled.
     * @param cardBin: bin of the card number if it is available. This will return null if the bin is deleted.
     */
    data class OnBinChanged(val cardBin: String?) : CardNumberTextFieldEvent

    /**
     * Informs the card number field length.
     * @param length: number of characters typed
     */
    data class OnLengthChanged(val length: Int) : CardNumberTextFieldEvent

    /**
     * Informs if the card number field has been focused.
     * @param isFocused: if the card number field is focused or not
     */
    data class OnFocusChanged(val isFocused: Boolean) : CardNumberTextFieldEvent

    /**
     * This function informs if card number that was typed is valid.
     * @param isValid: if the number typed is valid or not
     */
    data class IsValid(val isValid: Boolean) : CardNumberTextFieldEvent

    /**
     * Informs if the card number field has been focused.
     * @param lastFourDigits: when the last four digits of the card number are filled.
     * This will only be returned when the card number is valid and passes the Luhn algorithm validation.
     */
    data class OnLastFourDigitsFilled(val lastFourDigits: String) : CardNumberTextFieldEvent
}
