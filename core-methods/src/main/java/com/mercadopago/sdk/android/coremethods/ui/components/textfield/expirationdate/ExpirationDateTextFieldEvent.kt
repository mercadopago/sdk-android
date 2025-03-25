package com.mercadopago.sdk.android.coremethods.ui.components.textfield.expirationdate

/**
 * ExpirationDateTextFieldEvent is a sealed class that represents
 * the events that can be triggered by the expiration date text field.
 */
interface ExpirationDateTextFieldEvent {
    /**
     * This function informs if the expiration date field has been focused.
     * @param isFocused: expiration date field is focused or not
     */
    data class OnFocusChanged(val isFocused: Boolean) : ExpirationDateTextFieldEvent

    /**
     * This function informs if the expiration date field has been filled.
     * @param isFilled: expiration date field is filled or not
     */
    data class OnInputFilled(val isFilled: Boolean) : ExpirationDateTextFieldEvent

    /**
     * This function informs if expiration date that was typed is valid.
     * @param isValid: if the date typed is valid or not
     */
    data class IsValid(val isValid: Boolean) : ExpirationDateTextFieldEvent

    /**
     * This function informs the expiration date field length.
     * @param length: number of characters typed
     */
    data class OnLengthChanged(val length: Int) : ExpirationDateTextFieldEvent
}
